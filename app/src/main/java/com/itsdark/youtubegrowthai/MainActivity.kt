package com.itsdark.youtubegrowthai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {

    private lateinit var topicInput: EditText
    private lateinit var generateButton: Button
    private lateinit var clearButton: Button
    private lateinit var copyAllButton: Button
    private lateinit var modelButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var resultsContainer: LinearLayout

    private lateinit var engine: YouTubeGrowthEngine
    private lateinit var modelManager: LocalModelManager

    private val activityScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)

    companion object {
        private const val PICK_MODEL = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        topicInput = findViewById(R.id.topicInput)
        generateButton = findViewById(R.id.generateButton)
        clearButton = findViewById(R.id.clearButton)
        copyAllButton = findViewById(R.id.copyAllButton)
        modelButton = findViewById(R.id.modelButton)
        progressBar = findViewById(R.id.progressBar)
        statusText = findViewById(R.id.statusText)
        resultsContainer = findViewById(R.id.resultsContainer)

        engine = YouTubeGrowthEngine(this)
        modelManager = LocalModelManager(this)

        updateModelStatus()

        modelButton.setOnClickListener {
            selectModel()
        }

        generateButton.setOnClickListener {
            generateContent()
        }

        clearButton.setOnClickListener {
            clearResults()
        }

        copyAllButton.setOnClickListener {
            copyAllResults()
        }
    }

    private fun selectModel() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        startActivityForResult(intent, PICK_MODEL)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != PICK_MODEL ||
            resultCode != RESULT_OK ||
            data?.data == null
        ) {
            return
        }

        val uri = data.data!!

        statusText.text = "Copying model..."

        Thread {
            try {

                val destination =
                    modelManager.getConfiguredModel()

                contentResolver.openInputStream(uri).use { input ->

                    if (input == null) {
                        throw Exception("Cannot open selected file")
                    }

                    destination.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                runOnUiThread {
                    updateModelStatus()
                    statusText.text = "GGUF model ready"
                }

            } catch (e: Exception) {

                runOnUiThread {
                    statusText.text =
                        "Model error: ${e.message}"
                }
            }
        }.start()
    }

    private fun updateModelStatus() {

        statusText.text =
            if (modelManager.modelExists()) {
                "Model ready: ${modelManager.getConfiguredModel().name}"
            } else {
                "No GGUF model. Tap SELECT MODEL."
            }
    }

    private fun generateContent() {

        val topic =
            topicInput.text.toString().trim()

        if (topic.isEmpty()) {
            topicInput.error = "Enter a topic first"
            return
        }

        if (!modelManager.modelExists()) {
            statusText.text =
                "First select a GGUF model."
            return
        }

        setLoading(true)
        statusText.text = "AI generating..."

        activityScope.launch {

            try {

                val result =
                    withContext(Dispatchers.Default) {
                        engine.generate(topic)
                    }

                displayResults(result)

                statusText.text =
                    "Generation complete"

            } catch (e: Exception) {

                statusText.text =
                    "Error: ${e.message ?: "Generation failed"}"

            } finally {
                setLoading(false)
            }
        }
    }

    private fun displayResults(result: GrowthResult) {

        resultsContainer.removeAllViews()

        addResultSection(
            "Titles",
            result.titles
        )

        addResultSection(
            "Description",
            result.description
        )

        addResultSection(
            "Hook",
            result.hook
        )

        addResultSection(
            "Hashtags",
            result.hashtags
        )

        addResultSection(
            "Keywords",
            result.keywords
        )

        addResultSection(
            "Thumbnail Text",
            result.thumbnailText
        )

        addResultSection(
            "CTA",
            result.cta
        )

        addResultSection(
            "Growth Tips",
            result.growthTips
        )

        addResultSection(
            "Alternative Titles",
            result.alternativeTitles
        )
    }

    private fun addResultSection(
        title: String,
        content: String
    ) {

        val titleView =
            TextView(this).apply {
                text = title
                textSize = 20f
                setTextColor(
                    getColor(R.color.text_primary)
                )
                setPadding(0, 20, 0, 8)
            }

        val contentView =
            TextView(this).apply {
                text = content
                textSize = 15f
                setTextColor(
                    getColor(R.color.text_primary)
                )
                setPadding(12, 12, 12, 12)
            }

        val copyButton =
            Button(this).apply {
                text = "COPY"

                setOnClickListener {
                    copyText(title, content)
                }
            }

        resultsContainer.addView(titleView)
        resultsContainer.addView(contentView)
        resultsContainer.addView(copyButton)
    }

    private fun copyText(
        title: String,
        content: String
    ) {

        val clipboard =
            getSystemService(
                CLIPBOARD_SERVICE
            ) as android.content.ClipboardManager

        clipboard.setPrimaryClip(
            android.content.ClipData.newPlainText(
                title,
                content
            )
        )

        statusText.text =
            "$title copied"
    }

    private fun copyAllResults() {

        val builder =
            StringBuilder()

        for (i in 0 until resultsContainer.childCount) {

            val view =
                resultsContainer.getChildAt(i)

            if (view is TextView) {

                builder.append(view.text)
                builder.append("\n\n")
            }
        }

        copyText(
            "YouTube Growth AI",
            builder.toString()
        )
    }

    private fun clearResults() {

        topicInput.text.clear()
        resultsContainer.removeAllViews()

        updateModelStatus()
    }

    private fun setLoading(
        loading: Boolean
    ) {

        progressBar.visibility =
            if (loading)
                View.VISIBLE
            else
                View.GONE

        generateButton.isEnabled =
            !loading

        clearButton.isEnabled =
            !loading

        copyAllButton.isEnabled =
            !loading

        modelButton.isEnabled =
            !loading
    }

    override fun onDestroy() {

        activityScope.coroutineContext.cancel()

        super.onDestroy()
    }
}
