package ir.moodz.sampleproject.presentation.recevie_sms

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import ir.moodz.sampleproject.R
import ir.moodz.sampleproject.data.repository.SmsListener
import ir.moodz.sampleproject.databinding.FragmentReceiveBinding
import ir.moodz.sampleproject.util.BindingFragment
import ir.moodz.sampleproject.util.SharedPreferences

class ReceiveFragment : BindingFragment<FragmentReceiveBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentReceiveBinding::inflate

    private var receiver: SmsListener? = null
    val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")

    companion object{
        const val SUBMITTED_NUMBER = "number"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        var submittedNumber by SharedPreferences (
            requireContext(),
            SUBMITTED_NUMBER
        )

        binding.btnSubmit.setOnClickListener {

            val inputNumber = binding.edtNumber.text
            if (inputNumber.isBlank() || inputNumber.length < 11){
                return@setOnClickListener
            }
            submittedNumber = inputNumber.toString()
            Toast.makeText(
                requireContext(),
                R.string.number_submitted,
                Toast.LENGTH_SHORT
            ).show()

        }


        receiver = SmsListener()
        receiver!!.receivedMessageLiveData.observe(viewLifecycleOwner){ result ->

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.number_is , result.senderNumber))
                .setMessage(result.body)
                .show()
            println(
                "Result is $result"
            )

        }

        /*val readSmsPermissionIsGranted = requireActivity().
        checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        if (readSmsPermissionIsGranted) {
            readMessages(requireContext())?.let { lastMessage ->

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.number_is , lastMessage.phoneNumber))
                    .setMessage(lastMessage.message)
                    .show()

            }

        }else{

            Toast.makeText(
                requireContext(),
                "Permission not granted",
                Toast.LENGTH_SHORT
            ).show()
        }*/


    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(receiver)
    }
    data class MessageDetail(
        val phoneNumber:String,
        val message:String
    )

    private fun readMessages(context: Context): MessageDetail? {
        val selection = "type = ?"
        val selectionArgs = arrayOf("2")
        val sortOrder = "body DESC LIMIT 1"

        val cursor = context.contentResolver.query(
            Uri.parse("content://sms"),
            null,
            selection,
            selectionArgs,
            sortOrder
        )

        return cursor?.use {
            if (it.moveToFirst()) {
                val indexMessage = it.getColumnIndex("body")
                val indexSender = it.getColumnIndex("address")
                MessageDetail(
                    phoneNumber = it.getString(indexSender),
                    message = it.getString(indexMessage)
                )
            } else {
                null
            }


        }


    }




}