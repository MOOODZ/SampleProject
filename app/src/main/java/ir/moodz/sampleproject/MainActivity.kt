package ir.moodz.sampleproject

import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Telephony
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.moodz.sampleproject.data.receiver.SmsBroadcastReceiver
import ir.moodz.sampleproject.presentation.recevie_sms.ReceiveFragment
import ir.moodz.sampleproject.util.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    private val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)


        val submittedNumber by SharedPreferences(
            this,"number"
        )

        // For receiving the SMS
        smsBroadcastReceiver = SmsBroadcastReceiver()
        val listener = object : SmsBroadcastReceiver.Listener {

            override fun onTextReceived(text: SmsBroadcastReceiver.SmsDetail) {

                if (text.number == submittedNumber){

                    sendNotification(
                        getString(R.string.number_is, text.number),
                        text.message
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        playSong()
                    }


                }
            }
        }
        smsBroadcastReceiver!!.setListener(listener)




    }

    suspend fun playSong(){

        MediaPlayer.create(this, R.raw.song).apply {
            isLooping = false
            start()
            delay(15.seconds)
            stop()
            release()
        }

    }

    override fun onStart() {
        super.onStart()
        registerReceiver(smsBroadcastReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsBroadcastReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}

fun FragmentActivity.sendNotification(title:String, detail:String){

        val notification = NotificationCompat.Builder(
            applicationContext,
            "sms channel"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(detail)
            .build()
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1,notification)

}