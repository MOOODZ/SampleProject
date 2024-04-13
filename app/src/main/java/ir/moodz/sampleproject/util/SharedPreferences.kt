package ir.moodz.sampleproject.util

import android.content.Context
import androidx.activity.ComponentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferences(
    private val context: Context,
    private val key: String,
    private val defaultValue: String = "",
) : ReadWriteProperty<Any?, String> {


    private val sharedPreferences by lazy {
        context.getSharedPreferences("app_prefs", ComponentActivity.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}

fun Context.sharedPreferences(key: String) = SharedPreferences(this, key)
