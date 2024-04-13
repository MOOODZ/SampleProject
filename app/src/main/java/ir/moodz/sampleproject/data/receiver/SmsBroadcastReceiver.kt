package ir.moodz.sampleproject.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import javax.inject.Inject
import javax.inject.Singleton

class SmsBroadcastReceiver : BroadcastReceiver() {

    private var listener: Listener? = null

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsBundle = intent.extras ?: return
            val pdus = smsBundle["pdus"] as Array<*>? ?: return

            val messages = Array<SmsMessage?>(pdus.size) { null }
            val smsBody = StringBuilder()
            for (i in messages.indices) {
                messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                smsBody.append(messages[i]!!.messageBody)
            }

            val smsSender = messages[0]!!.originatingAddress!!
            listener?.onTextReceived(

                SmsDetail(
                    smsSender,
                    smsBody.toString()
                )
            )

        }
    }

    data class SmsDetail(
        val number: String,
        val message: String
    )

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onTextReceived(text: SmsDetail)
    }
}
