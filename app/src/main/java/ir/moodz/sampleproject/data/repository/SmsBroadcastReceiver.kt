package ir.moodz.sampleproject.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class SmsListener : BroadcastReceiver() {

    private val _receivedMessageLiveData = MutableLiveData<ReceivedMessage>()
    val receivedMessageLiveData: LiveData<ReceivedMessage> = _receivedMessageLiveData

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras ?: return

            try {
                val pdus = bundle["pdus"] as Array<ByteArray>? ?: return
                val messages = Array(pdus.size) { index ->
                    SmsMessage.createFromPdu(pdus[index])
                }

                for (message in messages) {
                    val sender = message.originatingAddress
                    val body = message.messageBody
                    _receivedMessageLiveData.postValue(
                        ReceivedMessage(
                            sender ?: "",
                            body
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    data class ReceivedMessage(
        val senderNumber: String,
        val body: String
    )
}
