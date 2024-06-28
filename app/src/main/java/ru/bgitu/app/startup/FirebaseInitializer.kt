package ru.bgitu.app.startup

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import org.koin.core.component.KoinComponent

class FirebaseInitializer : Initializer<Unit>, KoinComponent {

    override fun create(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(KoinInitializer::class.java)
    }
}
