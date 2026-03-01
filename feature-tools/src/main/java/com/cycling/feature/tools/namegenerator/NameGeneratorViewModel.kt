package com.cycling.feature.tools.namegenerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.NameFavorite
import com.cycling.domain.model.NameType
import com.cycling.domain.usecase.namefavorite.AddFavoriteUseCase
import com.cycling.domain.usecase.namefavorite.DeleteFavoriteUseCase
import com.cycling.domain.usecase.namefavorite.GetFavoritesUseCase
import com.cycling.domain.usecase.namegenerator.FactionType
import com.cycling.domain.usecase.namegenerator.GenerateChineseNameUseCase
import com.cycling.domain.usecase.namegenerator.GenerateFactionNameUseCase
import com.cycling.domain.usecase.namegenerator.GeneratePlaceNameUseCase
import com.cycling.domain.usecase.namegenerator.Gender
import com.cycling.domain.usecase.namegenerator.PlaceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameGeneratorViewModel @Inject constructor(
    private val generateChineseNameUseCase: GenerateChineseNameUseCase,
    private val generatePlaceNameUseCase: GeneratePlaceNameUseCase,
    private val generateFactionNameUseCase: GenerateFactionNameUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NameGeneratorState())
    val state: StateFlow<NameGeneratorState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NameGeneratorEffect>()
    val effect = _effect.asSharedFlow()

    private val favoritesFlow = getFavoritesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            favoritesFlow.collect { favorites ->
                _state.value = _state.value.copy(favorites = favorites)
            }
        }
    }

    fun onIntent(intent: NameGeneratorIntent) {
        when (intent) {
            NameGeneratorIntent.GenerateNames -> generateNames()
            is NameGeneratorIntent.SelectNameType -> selectNameType(intent.type)
            is NameGeneratorIntent.SelectGender -> selectGender(intent.gender)
            is NameGeneratorIntent.SelectCharCount -> selectCharCount(intent.count)
            is NameGeneratorIntent.SelectPlaceType -> selectPlaceType(intent.type)
            is NameGeneratorIntent.SelectFactionType -> selectFactionType(intent.type)
            is NameGeneratorIntent.SetGenerateCount -> setGenerateCount(intent.count)
            is NameGeneratorIntent.CopyName -> copyName(intent.name)
            is NameGeneratorIntent.AddToFavorites -> addToFavorites(intent.name, intent.type)
            is NameGeneratorIntent.RemoveFromFavorite -> removeFromFavorite(intent.id)
            NameGeneratorIntent.ToggleFavorites -> toggleFavorites()
            NameGeneratorIntent.LoadFavorites -> loadFavorites()
            NameGeneratorIntent.ClearError -> clearError()
        }
    }

    private fun generateNames() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isGenerating = true, error = null)

            val currentState = _state.value
            val names = when (currentState.selectedNameType) {
                NameType.PERSON_NAME -> generateChineseNameUseCase(
                    gender = currentState.gender,
                    charCount = currentState.charCount,
                    count = currentState.generateCount
                )
                NameType.PLACE_NAME -> generatePlaceNameUseCase(
                    type = currentState.placeType,
                    count = currentState.generateCount
                )
                NameType.FACTION_NAME -> generateFactionNameUseCase(
                    type = currentState.factionType,
                    count = currentState.generateCount
                )
            }

            _state.value = _state.value.copy(
                generatedNames = names,
                isGenerating = false
            )
        }
    }

    private fun selectNameType(type: NameType) {
        _state.value = _state.value.copy(
            selectedNameType = type,
            generatedNames = emptyList()
        )
    }

    private fun selectGender(gender: Gender) {
        _state.value = _state.value.copy(gender = gender)
    }

    private fun selectCharCount(count: Int) {
        _state.value = _state.value.copy(charCount = count)
    }

    private fun selectPlaceType(type: PlaceType) {
        _state.value = _state.value.copy(placeType = type)
    }

    private fun selectFactionType(type: FactionType) {
        _state.value = _state.value.copy(factionType = type)
    }

    private fun setGenerateCount(count: Int) {
        _state.value = _state.value.copy(generateCount = count.coerceIn(1, 20))
    }

    private fun copyName(name: String) {
        viewModelScope.launch {
            _effect.emit(NameGeneratorEffect.CopyToClipboard(name))
            _effect.emit(NameGeneratorEffect.ShowToast("已复制: $name"))
        }
    }

    private fun addToFavorites(name: String, type: NameType) {
        viewModelScope.launch {
            val isAlreadyFavorite = _state.value.favorites.any {
                it.name == name && it.type == type
            }

            if (isAlreadyFavorite) {
                _effect.emit(NameGeneratorEffect.ShowToast("已在收藏中"))
                return@launch
            }

            val favorite = NameFavorite(
                name = name,
                type = type
            )
            addFavoriteUseCase(favorite)
            _effect.emit(NameGeneratorEffect.ShowToast("已收藏: $name"))
        }
    }

    private fun removeFromFavorite(id: Long) {
        viewModelScope.launch {
            deleteFavoriteUseCase(id)
            _effect.emit(NameGeneratorEffect.ShowToast("已取消收藏"))
        }
    }

    private fun toggleFavorites() {
        _state.value = _state.value.copy(showFavorites = !_state.value.showFavorites)
    }

    private fun loadFavorites() {
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
