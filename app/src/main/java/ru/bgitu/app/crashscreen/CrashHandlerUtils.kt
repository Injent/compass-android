package ru.bgitu.app.crashscreen

import android.app.Activity
import android.content.Intent
import kotlin.concurrent.thread
import kotlin.system.exitProcess

internal class DefaultExceptionHandler(
    private val defaultExceptionHandler: Thread.UncaughtExceptionHandler?,
    private val run: (Throwable) -> Unit
) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        run(e)
        defaultExceptionHandler?.uncaughtException(t, e)
    }
}

fun Activity.handleUncaughtException() {
    val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    val newDefaultExceptionHandler = DefaultExceptionHandler(defaultExceptionHandler) { e ->
        thread(isDaemon = true, priority = Thread.MAX_PRIORITY) {
            e.printStackTrace()
            val intent = Intent(this, CrashActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
            finish()
            exitProcess(2)
        }
    }
    Thread.setDefaultUncaughtExceptionHandler(newDefaultExceptionHandler)
}