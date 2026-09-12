package com.itsdark.youtubegrowthai

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class AboutActivity : Activity() {

    companion object {
        const val PREFS_NAME = "growth_ai_prefs"
        const val KEY_API_KEY = "youtube_api_key"
    }

    private lateinit var prefs: SharedPreferences

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_about
        )

        prefs =
            getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val appVersionText: TextView =
            findViewById(R.id.appVersionText)

        val versionName =
            try {
                packageManager
                    .getPackageInfo(packageName, 0)
                    .versionName
            } catch (e: Exception) {
                null
            }

        appVersionText.text =
            if (versionName != null)
                "Version $versionName"
            else
                ""

        val openChannelFromAboutButton: Button =
            findViewById(R.id.openChannelFromAboutButton)

        val rateAppButton: Button =
            findViewById(R.id.rateAppButton)

        val backFromAboutButton: Button =
            findViewById(R.id.backFromAboutButton)

        val apiKeyInput: EditText =
            findViewById(R.id.apiKeyInput)

        val saveApiKeyButton: Button =
            findViewById(R.id.saveApiKeyButton)

        apiKeyInput.setText(
            prefs.getString(KEY_API_KEY, "")
        )

        openChannelFromAboutButton.setOnClickListener {
            openChannel()
        }

        rateAppButton.setOnClickListener {
            openPlayStoreListing()
        }

        backFromAboutButton.setOnClickListener {
            finish()
        }

        saveApiKeyButton.setOnClickListener {

            val key =
                apiKeyInput.text
                    .toString()
                    .trim()

            prefs.edit()
                .putString(KEY_API_KEY, key)
                .apply()

            Toast.makeText(
                this,
                if (key.isEmpty())
                    "API key cleared"
                else
                    "API key saved",
                Toast.LENGTH_SHORT
            ).show()
        }
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

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(channelUrl)
                    )
                )

            } catch (e2: ActivityNotFoundException) {
                // No app available to open the link; silently ignore.
            }
        }
    }

    private fun openPlayStoreListing() {

        val marketUri =
            Uri.parse("market://details?id=$packageName")

        try {

            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    marketUri
                )
            )

        } catch (e: ActivityNotFoundException) {

            try {

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://play.google.com/store/apps/details?id=$packageName"
                        )
                    )
                )

            } catch (e2: ActivityNotFoundException) {
                // No browser or Play Store available; silently ignore.
            }
        }
    }
}
