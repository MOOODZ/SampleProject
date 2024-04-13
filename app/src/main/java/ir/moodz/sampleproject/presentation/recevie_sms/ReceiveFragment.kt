package ir.moodz.sampleproject.presentation.recevie_sms

import android.app.AlertDialog
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import ir.moodz.sampleproject.R
import ir.moodz.sampleproject.data.receiver.SmsBroadcastReceiver
import ir.moodz.sampleproject.databinding.FragmentReceiveBinding
import ir.moodz.sampleproject.util.BindingFragment
import ir.moodz.sampleproject.util.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ReceiveFragment : BindingFragment<FragmentReceiveBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentReceiveBinding::inflate

    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    private val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // For saving the number locally
        var submittedNumber by SharedPreferences(
            requireContext(),
            SUBMITTED_NUMBER
        )
        binding.btnSubmit.setOnClickListener {

            val inputNumber = binding.edtNumber.text
            if (inputNumber.isBlank() || inputNumber.length < 11) {
                return@setOnClickListener
            }

            submittedNumber = inputNumber.replaceFirst(Regex("\\d"), "+98")
            Toast.makeText(
                requireContext(),
                R.string.number_submitted,
                Toast.LENGTH_SHORT
            ).show()

        }

        // For receiving the SMS
        smsBroadcastReceiver = SmsBroadcastReceiver()
        val listener = object : SmsBroadcastReceiver.Listener {

            override fun onTextReceived(text: SmsBroadcastReceiver.SmsDetail) {

                if (text.number == submittedNumber){

                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.number_is, text.number))
                        .setMessage(text.message)
                        .show()

                    CoroutineScope(Dispatchers.Main).launch {
                        playSong()
                    }


                }
            }
        }
        smsBroadcastReceiver!!.setListener(listener)


    }
    suspend fun playSong(){

        val mediaPlayer = MediaPlayer.create(context, R.raw.song)
        mediaPlayer.isLooping = false
        mediaPlayer.start()
        delay(15.seconds)
        mediaPlayer.stop()
        mediaPlayer.release()

    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(smsBroadcastReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }

    companion object {
        const val SUBMITTED_NUMBER = "number"
    }


}