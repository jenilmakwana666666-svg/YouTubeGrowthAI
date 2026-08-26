package com.itsdark.youtubegrowthai

import android.content.Context
import dev.ffmpegkit.llama.Llama
import dev.ffmpegkit.llama.LlamaConfig
import java.io.File

class LocalModelManager(
    private val context: Context
) {

    companion object {
        private const val MODEL_DIRECTORY = "models"
        private const val DEFAULT_MODEL_NAME = "model.gguf"
    }

    private var loadedModel: Any? = null

    fun getModelDirectory(): File {
        val directory = File(context.filesDir, MODEL_DIRECTORY)

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
        return if (modelExists()) {
            getConfiguredModel().length()
        } else {
            0L
        }
    }

    fun modelStatus(): String {
        return if (modelExists()) {
            "GGUF model ready: ${getConfiguredModel().name}"
        } else {
            "GGUF model not found."
        }
    }

    suspend fun generate(
        prompt: String,
        maxTokens: Int,
        temperature: Float
    ): String {

        if (!modelExists()) {
            throw IllegalStateException(
                "GGUF model not found. Select a model first."
            )
        }

        val model = Llama.loadModel(
            modelPath = modelPath(),
            config = LlamaConfig(
                contextSize = 2048,
                threads = 4
            )
        )

        loadedModel = model

        return try {
            val result = Llama.complete(
                model,
                prompt = prompt,
                systemPrompt = """
                    You are YouTube Growth AI.
                    Generate useful YouTube Shorts content.
                    Answer in natural Hinglish.
                    Follow the requested output sections exactly.
                """.trimIndent(),
                maxTokens = maxTokens,
                temperature = temperature
            )

            result.text
        } finally {
            Llama.releaseModel(model)
            loadedModel = null
        }
    }
}
