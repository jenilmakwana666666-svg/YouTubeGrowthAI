package com.itsdark.youtubegrowthai

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() {

    // ---------- EXISTING VIEWS (unchanged) ----------
    private lateinit var topicInput: EditText
    private lateinit var generateButton: Button
    private lateinit var visitChannelButton: Button
    private lateinit var clearButton: Button
    private lateinit var copyAllButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var resultsContainer: LinearLayout

    // ---------- NEW VIEWS (10 new features) ----------
    private lateinit var categorySpinner: Spinner
    private lateinit var toneSpinner: Spinner
    private lateinit var recentTopicsButton: Button
    private lateinit var trendingHashtagsButton: Button
    private lateinit var regenerateButton: Button
    private lateinit var shareButton: Button
    private lateinit var exportButton: Button
    private lateinit var favoritesButton: Button
    private lateinit var themeToggleButton: Button
    private lateinit var settingsButton: Button

    // Root/header/label views kept only for the theme-toggle feature
    private lateinit var rootScroll: View
    private lateinit var rootContainer: LinearLayout
    private lateinit var optionsCard: LinearLayout
    private lateinit var topicCard: LinearLayout
    private lateinit var headerTitle: TextView
    private lateinit var headerSubtitle: TextView
    private lateinit var optionsLabel: TextView
    private lateinit var topicLabel: TextView
    private lateinit var resultsLabel: TextView
    private lateinit var footerText: TextView

    private val engine = YouTubeGrowthEngine()

    private val activityScope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.Main
        )

    private lateinit var prefs: SharedPreferences

    private var lastTopic: String = ""
    private var lastCategory: String = "General"
    private var lastTone: String = "Default"

    private val categories =
        listOf(
            "General",
            "Gaming",
            "Tech",
            "Vlog",
            "Comedy",
            "Anime"
        )

    private val tones =
        listOf(
            "Default",
            "Funny",
            "Serious",
            "Motivational",
            "Educational"
        )

    private val trendingHashtags =
        listOf(
            "#Shorts",
            "#YouTubeShorts",
            "#Viral",
            "#Trending",
            "#ForYou",
            "#Explore",
            "#Fyp",
            "#itsdark",
            "#itsdark444",
            "#ShortsFeed",
            "#Reels",
            "#ViralVideo"
        )

    companion object {
        private const val PREFS_NAME = "growth_ai_prefs"
        private const val KEY_RECENT_TOPICS = "recent_topics"
        private const val KEY_FAVORITES = "favorites"
        private const val KEY_LIGHT_THEME = "light_theme"
        private const val MAX_RECENT_TOPICS = 10
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )

        prefs =
            getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        bindViews()
        setupSpinners()
        setupClickListeners()

        val isLight =
            prefs.getBoolean(KEY_LIGHT_THEME, false)

        applyTheme(isLight)
    }

    private fun bindViews() {

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

        categorySpinner =
            findViewById(R.id.categorySpinner)

        toneSpinner =
            findViewById(R.id.toneSpinner)

        recentTopicsButton =
            findViewById(R.id.recentTopicsButton)

        trendingHashtagsButton =
            findViewById(R.id.trendingHashtagsButton)

        regenerateButton =
            findViewById(R.id.regenerateButton)

        shareButton =
            findViewById(R.id.shareButton)

        exportButton =
            findViewById(R.id.exportButton)

        favoritesButton =
            findViewById(R.id.favoritesButton)

        themeToggleButton =
            findViewById(R.id.themeToggleButton)

        settingsButton =
            findViewById(R.id.settingsButton)

        rootScroll =
            findViewById(R.id.rootScroll)

        rootContainer =
            findViewById(R.id.rootContainer)

        optionsCard =
            findViewById(R.id.optionsCard)

        topicCard =
            findViewById(R.id.topicCard)

        headerTitle =
            findViewById(R.id.headerTitle)

        headerSubtitle =
            findViewById(R.id.headerSubtitle)

        optionsLabel =
            findViewById(R.id.optionsLabel)

        topicLabel =
            findViewById(R.id.topicLabel)

        resultsLabel =
            findViewById(R.id.resultsLabel)

        footerText =
            findViewById(R.id.footerText)
    }

    private fun setupSpinners() {

        val categoryAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categories
            )

        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        categorySpinner.adapter = categoryAdapter

        val toneAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                tones
            )

        toneAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        toneSpinner.adapter = toneAdapter
    }

    private fun setupClickListeners() {

        generateButton.setOnClickListener {
            generateContent()
        }

        visitChannelButton.setOnClickListener {
            openChannel()
        }

        clearButton.setOnClickListener {
            clearResults()
        }

        copyAllButton.setOnClickListener {
            copyAllResults()
        }

        regenerateButton.setOnClickListener {
            regenerateContent()
        }

        recentTopicsButton.setOnClickListener {
            showRecentTopicsDialog()
        }

        trendingHashtagsButton.setOnClickListener {
            showTrendingHashtagsDialog()
        }

        shareButton.setOnClickListener {
            shareResults()
        }

        exportButton.setOnClickListener {
            exportAsFile()
        }

        favoritesButton.setOnClickListener {
            showFavoritesDialog()
        }

        themeToggleButton.setOnClickListener {
            toggleTheme()
        }

        settingsButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AboutActivity::class.java
                )
            )
        }
    }

    // =========================================================
    // EXISTING GENERATE FLOW (unchanged behavior by default)
    // =========================================================

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

        val category =
            categorySpinner.selectedItem
                ?.toString()
                ?: "General"

        val tone =
            toneSpinner.selectedItem
                ?.toString()
                ?: "Default"

        setLoading(true)

        statusText.text =
            "Creating content..."

        activityScope.launch {

            try {

                val result =
                    withContext(
                        Dispatchers.Default
                    ) {
                        engine.generate(
                            topic = topic,
                            tone = tone,
                            category = category
                        )
                    }

                displayResults(result)

                lastTopic = topic
                lastCategory = category
                lastTone = tone

                regenerateButton.isEnabled = true

                saveRecentTopic(topic)

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

    // =========================================================
    // FEATURE 2: REGENERATE
    // =========================================================

    private fun regenerateContent() {

        if (lastTopic.isEmpty()) {
            return
        }

        setLoading(true)

        statusText.text =
            "Regenerating..."

        activityScope.launch {

            try {

                val seed =
                    (System.currentTimeMillis() % 100000).toInt()
                        .let { if (it == 0) 1 else it }

                val result =
                    withContext(
                        Dispatchers.Default
                    ) {
                        engine.generate(
                            topic = lastTopic,
                            tone = lastTone,
                            category = lastCategory,
                            variationSeed = seed
                        )
                    }

                displayResults(result)

                statusText.text =
                    "Ready • New variation generated"

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

        val buttonRow =
            LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
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

        // FEATURE 5: FAVORITES / BOOKMARK
        val favoriteButton =
            Button(this).apply {

                text = "☆ SAVE"

                setOnClickListener {
                    saveFavorite(
                        title,
                        content
                    )
                }
            }

        buttonRow.addView(copyButton)
        buttonRow.addView(favoriteButton)

        resultsContainer.addView(
            titleView
        )

        resultsContainer.addView(
            contentView
        )

        resultsContainer.addView(
            buttonRow
        )
    }

    // =========================================================
    // VISIT MY CHANNEL (unchanged)
    // =========================================================

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

    private fun copyAllResults(): String {

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

        return builder.toString()
    }

    private fun clearResults() {

        topicInput.text.clear()

        resultsContainer.removeAllViews()

        regenerateButton.isEnabled = false

        lastTopic = ""

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

        regenerateButton.isEnabled =
            !loading && lastTopic.isNotEmpty()
    }

    // =========================================================
    // FEATURE 1: HISTORY / RECENT TOPICS
    // =========================================================

    private fun saveRecentTopic(topic: String) {

        val array =
            JSONArray(
                prefs.getString(KEY_RECENT_TOPICS, "[]")
            )

        val updated = mutableListOf<String>()

        updated.add(topic)

        for (i in 0 until array.length()) {

            val existing = array.getString(i)

            if (
                !existing.equals(topic, ignoreCase = true) &&
                !updated.contains(existing)
            ) {
                updated.add(existing)
            }
        }

        val trimmed =
            updated.take(MAX_RECENT_TOPICS)

        val newArray = JSONArray()

        trimmed.forEach {
            newArray.put(it)
        }

        prefs.edit()
            .putString(KEY_RECENT_TOPICS, newArray.toString())
            .apply()
    }

    private fun showRecentTopicsDialog() {

        val array =
            JSONArray(
                prefs.getString(KEY_RECENT_TOPICS, "[]")
            )

        if (array.length() == 0) {

            statusText.text =
                "No recent topics yet"

            return
        }

        val items =
            Array(array.length()) {
                array.getString(it)
            }

        AlertDialog.Builder(this)
            .setTitle("Recent Topics")
            .setItems(items) { _, which ->

                topicInput.setText(items[which])
                topicInput.setSelection(items[which].length)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    // =========================================================
    // FEATURE 9: TRENDING HASHTAGS
    // =========================================================

    private fun showTrendingHashtagsDialog() {

        val items = trendingHashtags.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Trending Hashtags — tap to copy")
            .setItems(items) { _, which ->

                val clipboard =
                    getSystemService(
                        CLIPBOARD_SERVICE
                    ) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "Hashtag",
                        items[which]
                    )
                )

                statusText.text =
                    "${items[which]} copied"
            }
            .setNegativeButton("Close", null)
            .show()
    }

    // =========================================================
    // FEATURE 3: SHARE
    // =========================================================

    private fun shareResults() {

        val content = collectResultsText()

        if (content.isBlank()) {

            statusText.text =
                "Generat
