package com.maansiauto.panelconfigurator.util

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * MAANSI AUTO contact details used by the "Send to MAANSI team" buttons.
 *
 * Fill these in with the real details:
 *  - EMAIL: the quotation team's email address, e.g. "quotes@yourcompany.com"
 *  - WHATSAPP: number with country code and digits only, e.g. "919876543210"
 *
 * While a value is blank, that button is hidden and only the general
 * "Share" button (WhatsApp, Gmail, etc. via the Android share sheet) is shown.
 */
object TeamContact {
    const val EMAIL: String = ""
    const val WHATSAPP: String = ""
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Panel requirement", text))
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}

/** Opens the Android share sheet so the user can pick WhatsApp, Gmail, SMS, etc. */
fun shareText(context: Context, subject: String, text: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        context.startActivity(Intent.createChooser(send, "Share requirement").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No app found to share with", Toast.LENGTH_SHORT).show()
    }
}

/** Opens the email app with the team address, subject and requirement text filled in. */
fun emailTeam(context: Context, subject: String, text: String) {
    val mail = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(TeamContact.EMAIL))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(mail)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

/** Opens a WhatsApp chat with the team number with the requirement text pre-filled. */
fun whatsappTeam(context: Context, text: String) {
    val uri = Uri.parse("https://wa.me/${TeamContact.WHATSAPP}?text=${Uri.encode(text)}")
    val view = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(view)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Could not open WhatsApp", Toast.LENGTH_SHORT).show()
    }
}
