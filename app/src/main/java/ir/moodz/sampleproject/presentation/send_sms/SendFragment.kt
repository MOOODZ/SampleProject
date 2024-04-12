package ir.moodz.sampleproject.presentation.send_sms

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import ir.moodz.sampleproject.R
import ir.moodz.sampleproject.databinding.FragmentSendBinding
import ir.moodz.sampleproject.util.BindingFragment

@AndroidEntryPoint
class SendFragment : BindingFragment<FragmentSendBinding>() {

    private val sendSmsViewModel : SendSmsViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSendBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().requestPermissions(
            arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
                ),
            0
        )


        sendSmsViewModel.smsStatus.observe(viewLifecycleOwner) { status ->

            when (status) {

                is SendSmsViewModel.SmsStatus.Sending -> binding.progressBar.isVisible = true
                is SendSmsViewModel.SmsStatus.Error -> {
                    Toast.makeText(
                        requireContext(),
                        status.error,
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressBar.isVisible = false
                }
                is SendSmsViewModel.SmsStatus.Sent -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.message_sent,
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressBar.isVisible = false

                }

            }

        }

        binding.btnNavigate.setOnClickListener {findNavController().navigate(
            R.id.action_sendFragment_to_receiveFragment
        )}

        binding.btnSend.setOnClickListener {

            if (requireActivity().checkSelfPermission(Manifest.permission.SEND_SMS) == PERMISSION_GRANTED){

                sendSmsViewModel.sendSMS(
                    binding.edtMessage.text.toString(),
                    binding.edtPhone.text.toString()

                )

            }

        }


    }

}