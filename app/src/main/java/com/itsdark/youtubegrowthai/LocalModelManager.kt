package com.itsdark.youtubegrowthai

import android.content.Context
import java.io.File

class LocalModelManager(
    private val context: Context
) {

    companion object {
        private const val MODEL_DIRECTORY = "models"
        private const val DEFAULT_MODEL_NAME = "model.gguf"
    }

    fun getModelDirectory(): File {
        val directory = File(
            context.filesDir,
            MODEL_DIRECTORY
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        return directory
    }

    fun getConfiguredModel(): File {
        return File(
            getModelDirectory(),
            DEFAULT_MODEL_NAME
        )
    }

    fun modelExists(): Boolean {
        val model = getConfiguredModel()
        return model.isFile && model.length() > 0
    }

    fun modelPath(): String {
        return getConfiguredModel().absolutePath
    }

    fun getModelSizeBytes(): Long {
        val model = getConfiguredModel()
        return if (modelExists()) model.length() else 0L
    }

    fun modelStatus(): String {
        return if (modelExists()) {
            "GGUF model ready: ${getConfiguredModel().name}"
        } else {
            "GGUF model not found."
        }
    }

    fun generate(
        prompt: String,
        maxTokens: Int,
        temperature: Float
    ): String {
        throw UnsupportedOperationException(
            "Native llama.cpp GGUF inference is not bundled yet. " +
                "Connect the JNI inference implementation here."
        )
    }
}
