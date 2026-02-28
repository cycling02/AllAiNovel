package com.cycling.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.dataStore
import com.cycling.domain.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_preferences.pb",
    serializer = UserPreferencesSerializer
)

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val userPreferencesFlow: Flow<UserPreferences> = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences())
            } else {
                throw exception
            }
        }

    suspend fun updateDarkTheme(darkTheme: Boolean) {
        context.userDataStore.updateData { currentPreferences ->
            currentPreferences.copy(darkTheme = darkTheme)
        }
    }

    suspend fun updateDynamicColors(dynamicColors: Boolean) {
        context.userDataStore.updateData { currentPreferences ->
            currentPreferences.copy(dynamicColors = dynamicColors)
        }
    }

    suspend fun updateDefaultApiConfigId(apiConfigId: Long?) {
        context.userDataStore.updateData { currentPreferences ->
            currentPreferences.copy(defaultApiConfigId = apiConfigId)
        }
    }

    suspend fun updateAll(preferences: UserPreferences) {
        context.userDataStore.updateData { preferences }
    }
}
