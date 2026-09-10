package com.itsdark.youtubegrowthai

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AboutActivity : Activity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_about
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

        openChannelFromAboutButton.setOnClickListener {
            openChannel()
        }

        rateAppButton.setOnClickListener {
            openPlayStoreListing()
        }

        backFromAboutButton.setOnClickListener {
            finish()
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
