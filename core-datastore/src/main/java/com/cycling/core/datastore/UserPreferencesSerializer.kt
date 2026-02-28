package com.cycling.core.datastore

import androidx.datastore.core.Serializer
import com.cycling.domain.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        return try {
            val bytes = withContext(Dispatchers.IO) {
                input.readBytes()
            }
            if (bytes.isEmpty()) {
                defaultValue
            } else {
                Json.decodeFromString<UserPreferences>(String(bytes))
            }
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).toByteArray())
        }
    }
}
