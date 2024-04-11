package ir.moodz.sampleproject.feature_sms.presentation.recevie_sms

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import ir.moodz.sampleproject.R
import ir.moodz.sampleproject.databinding.FragmentReceiveBinding
import ir.moodz.sampleproject.feature_sms.data.repository.SmsBroadcastReceiver
import ir.moodz.sampleproject.ui_utils.BindingFragment

class ReceiveFragment : BindingFragment<FragmentReceiveBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentReceiveBinding::inflate

    var smsBroadcastReceiver : SmsBroadcastReceiver? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = SmsRetriever.getClient(requireActivity())
        client.startSmsUserConsent(null)


    }
    override fun onStart() {
        super.onStart()
        registerForBroadcast()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }

    private val requestUserConsent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val message = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                if (!message.isNullOrBlank()) {

                    //Todo: We have the message here

                    AlertDialog.Builder(requireContext())
                        .setMessage(message)
                        .show()

                }
            }


        }
    private fun registerForBroadcast() {

        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener{
                override fun onSuccess(intent: Intent?) {

                    requestUserConsent.launch(intent)

                }

                override fun onFailure() {

                    Toast.makeText(
                        requireContext(),
                        R.string.error,
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver , intentFilter)



    }



}