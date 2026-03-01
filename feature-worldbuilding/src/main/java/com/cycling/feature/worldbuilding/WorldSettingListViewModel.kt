package com.cycling.feature.worldbuilding

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting
import com.cycling.domain.usecase.worldsetting.AddWorldSettingUseCase
import com.cycling.domain.usecase.worldsetting.DeleteWorldSettingUseCase
import com.cycling.domain.usecase.worldsetting.GetWorldSettingsByBookIdUseCase
import com.cycling.domain.usecase.worldsetting.UpdateWorldSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "WorldSettingListVM"

@HiltViewModel
class WorldSettingListViewModel @Inject constructor(
    getWorldSettingsByBookIdUseCase: GetWorldSettingsByBookIdUseCase,
    private val addWorldSettingUseCase: AddWorldSettingUseCase,
    private val updateWorldSettingUseCase: UpdateWorldSettingUseCase,
    private val deleteWorldSettingUseCase: DeleteWorldSettingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: Long = savedStateHandle["bookId"] ?: run {
        Log.e(TAG, "bookId not found in SavedStateHandle, using default value 0")
        0L
    }

    private val _state = MutableStateFlow(WorldSettingListState())
    val state: StateFlow<WorldSettingListState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorldSettingListEffect>()
    val effect = _effect.asSharedFlow()

    private val settingsFlow = getWorldSettingsByBookIdUseCase(bookId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            settingsFlow.collect { settings ->
                val currentQuery = _state.value.searchQuery
                _state.value = _state.value.copy(
                    settings = settings,
                    filteredSettings = if (currentQuery.isNotBlank()) {
                        settings.filter { 
                            it.name.contains(currentQuery, ignoreCase = true) ||
                            it.description.contains(currentQuery, ignoreCase = true)
                        }
                    } else {
                        settings
                    },
                    isLoading = false
                )
            }
        }
    }

    fun onIntent(intent: WorldSettingListIntent) {
        when (intent) {
            WorldSettingListIntent.ShowAddDialog -> showAddDialog()
            WorldSettingListIntent.HideAddDialog -> hideAddDialog()
            is WorldSettingListIntent.ShowEditDialog -> showEditDialog(intent.setting)
            WorldSettingListIntent.HideEditDialog -> hideEditDialog()
            is WorldSettingListIntent.ShowDeleteDialog -> showDeleteDialog(intent.setting)
            WorldSettingListIntent.HideDeleteDialog -> hideDeleteDialog()
            is WorldSettingListIntent.AddSetting -> addSetting(intent.setting)
            is WorldSettingListIntent.UpdateSetting -> updateSetting(intent.setting)
            WorldSettingListIntent.ConfirmDelete -> confirmDelete()
            is WorldSettingListIntent.SearchSettings -> searchSettings(intent.query)
            is WorldSettingListIntent.FilterByType -> filterByType(intent.type)
        }
    }

    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddDialog = true)
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddDialog = false)
    }

    private fun showEditDialog(setting: WorldSetting) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            settingToEdit = setting
        )
    }

    private fun hideEditDialog() {
        _state.value = _state.value.copy(
            showEditDialog = false,
            settingToEdit = null
        )
    }

    private fun showDeleteDialog(setting: WorldSetting) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            settingToDelete = setting
        )
    }

    private fun hideDeleteDialog() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            settingToDelete = null
        )
    }

    private fun addSetting(setting: WorldSetting) {
        executeOperation(
            operation = {
                val newSetting = setting.copy(bookId = bookId)
                addWorldSettingUseCase(newSetting)
            },
            onSuccess = {
                _state.value = _state.value.copy(showAddDialog = false)
                _effect.emit(WorldSettingListEffect.ShowToast("设定创建成功"))
            },
            onError = { message ->
                _effect.emit(WorldSettingListEffect.ShowError("创建设定失败: $message"))
            }
        )
    }

    private fun updateSetting(setting: WorldSetting) {
        executeOperation(
            operation = {
                val updatedSetting = setting.copy(
                    updatedAt = System.currentTimeMillis()
                )
                updateWorldSettingUseCase(updatedSetting)
            },
            onSuccess = {
                _state.value = _state.value.copy(
                    showEditDialog = false,
                    settingToEdit = null
                )
                _effect.emit(WorldSettingListEffect.ShowToast("设定更新成功"))
            },
            onError = { message ->
                _effect.emit(WorldSettingListEffect.ShowError("更新设定失败: $message"))
            }
        )
    }

    private fun confirmDelete() {
        _state.value.settingToDelete?.let { setting ->
            executeOperation(
                operation = { deleteWorldSettingUseCase(setting.id) },
                onSuccess = {
                    _state.value = _state.value.copy(
                        showDeleteDialog = false,
                        settingToDelete = null
                    )
                    _effect.emit(WorldSettingListEffect.ShowToast("设定删除成功"))
                },
                onError = { message ->
                    _effect.emit(WorldSettingListEffect.ShowError("删除设定失败: $message"))
                }
            )
        }
    }

    private inline fun executeOperation(
        crossinline operation: suspend () -> Unit,
        crossinline onSuccess: suspend () -> Unit,
        crossinline onError: suspend (String) -> Unit
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                operation()
                _state.value = _state.value.copy(isLoading = false)
                onSuccess()
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(WorldSettingListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                onError(e.message ?: "未知错误")
            }
        }
    }

    private fun searchSettings(query: String) {
        val filtered = if (query.isNotBlank()) {
            _state.value.settings.filter { 
                it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
        } else {
            _state.value.settings
        }
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredSettings = filtered
        )
    }

    private fun filterByType(type: SettingType?) {
        _state.value = _state.value.copy(selectedType = type)
    }

    fun navigateBack() {
        viewModelScope.launch {
            _effect.emit(WorldSettingListEffect.NavigateBack)
        }
    }
}
