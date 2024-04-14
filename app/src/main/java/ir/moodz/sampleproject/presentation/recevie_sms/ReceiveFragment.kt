package ir.moodz.sampleproject.presentation.recevie_sms

import android.app.AlertDialog
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.viewbinding.ViewBinding
import ir.moodz.sampleproject.R
import ir.moodz.sampleproject.data.receiver.SmsBroadcastReceiver
import ir.moodz.sampleproject.databinding.FragmentReceiveBinding
import ir.moodz.sampleproject.sendNotification
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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // For saving the number locally
        var submittedNumber by SharedPreferences(
            requireContext(),
            "number"
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

    }



}