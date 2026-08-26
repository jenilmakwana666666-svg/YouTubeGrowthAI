package com.itsdark.youtubegrowthai

import android.content.Context

class YouTubeGrowthEngine(
    context: Context
) {

    private val modelManager = LocalModelManager(context)

    suspend fun generate(topic: String): GrowthResult {

        require(topic.isNotBlank()) {
            "Topic cannot be empty."
        }

        if (!modelManager.modelExists()) {
            throw IllegalStateException(
                "GGUF model not found. Please select a GGUF model first."
            )
        }

        val prompt = PromptBuilder.build(
            topic.trim(),
            OutputLanguage.HINGLISH
        )

        val rawOutput = modelManager.generate(
            prompt = prompt,
            maxTokens = 512,
            temperature = 0.7f
        )

        return ResultParser.parse(rawOutput)
    }
}
