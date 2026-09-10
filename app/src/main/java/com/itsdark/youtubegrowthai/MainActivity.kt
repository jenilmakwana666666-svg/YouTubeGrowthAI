package com.itsdark.youtubegrowthai

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {

    private lateinit var topicInput: EditText
    private lateinit var generateButton: Button
    private lateinit var visitChannelButton: Button
    private lateinit var clearButton: Button
    private lateinit var copyAllButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var resultsContainer: LinearLayout

    private val engine = YouTubeGrowthEngine()

    private val activityScope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.Main
        )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )

        topicInput =
            findViewById(R.id.topicInput)

        generateButton =
            findViewById(R.id.generateButton)

        visitChannelButton =
            findViewById(R.id.visitChannelButton)

        clearButton =
            findViewById(R.id.clearButton)

        copyAllButton =
            findViewById(R.id.copyAllButton)

        progressBar =
            findViewById(R.id.progressBar)

        statusText =
            findViewById(R.id.statusText)

        resultsContainer =
            findViewById(R.id.resultsContainer)

        generateButton.setOnClickListener {
            generateContent()
        }

        visitChannelButton.setOnClickListener {
            Log.d("YTGrowthAI", "Visit My Channel button clicked")
            openChannel()
        }

        clearButton.setOnClickListener {
            clearResults()
        }

        copyAllButton.setOnClickListener {
            copyAllResults()
        }
    }

    private fun generateContent() {

        val topic =
            topicInput.text
                .toString()
                .trim()

        if (topic.isEmpty()) {

            topicInput.error =
                "Enter a topic first"

            return
        }

        setLoading(true)

        statusText.text =
            "Creating content..."

        activityScope.launch {

            try {

                val result =
                    withContext(
                        Dispatchers.Default
                    ) {
                        engine.generate(topic)
                    }

                displayResults(result)

                statusText.text =
                    "Ready • No AI model required"

            } catch (e: Exception) {

                statusText.text =
                    "Error: ${e.message}"

            } finally {

                setLoading(false)
            }
        }
    }

    private fun displayResults(
        result: GrowthResult
    ) {

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
                    getColor(
                        R.color.text_primary
                    )
                )

                setPadding(
                    0,
                    20,
                    0,
                    8
                )
            }

        val contentView =
            TextView(this).apply {

                text = content

                textSize = 15f

                setTextColor(
                    getColor(
                        R.color.text_primary
                    )
                )

                setPadding(
                    12,
                    12,
                    12,
                    12
                )
            }

        val copyButton =
            Button(this).apply {

                text = "COPY"

                setOnClickListener {
                    copyText(
                        title,
                        content
                    )
                }
            }

        resultsContainer.addView(
            titleView
        )

        resultsContainer.addView(
            contentView
        )

        resultsContainer.addView(
            copyButton
        )
    }

    private fun openChannel() {

        val channelUrl =
            "https://www.youtube.com/@itsdark_444"

        try {

            val youtubeAppIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(channelUrl)
                )

            youtubeAppIntent.setPackage(
                "com.google.android.youtube"
            )

            startActivity(youtubeAppIntent)

        } catch (e: ActivityNotFoundException) {

            try {

                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(channelUrl)
                    )

                startActivity(browserIntent)

            } catch (e2: ActivityNotFoundException) {

                statusText.text =
                    "Unable to open channel link"
            }
        }
    }

    private fun copyText(
        title: String,
        content: String
    ) {

        val clipboard =
            getSystemService(
                CLIPBOARD_SERVICE
            ) as ClipboardManager

        clipboard.setPrimaryClip(
            ClipData.newPlainText(
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

        for (
            i in 0 until resultsContainer.childCount
        ) {

            val view =
                resultsContainer.getChildAt(i)

            if (view is TextView) {

                builder
                    .append(view.text)
                    .append("\n\n")
            }
        }

        if (builder.isNotBlank()) {

            copyText(
                "YouTube Growth AI",
                builder.toString()
            )
        }
    }

    private fun clearResults() {

        topicInput.text.clear()

        resultsContainer.removeAllViews()

        statusText.text =
            "Ready • No AI model required"
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
    }

    override fun onDestroy() {

        activityScope.cancel()

        super.onDestroy()
    }
}
