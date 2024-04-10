package ir.moodz.sampleproject.feature_sms.presentation.recevie_sms

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import ir.moodz.sampleproject.databinding.FragmentReceiveBinding
import ir.moodz.sampleproject.ui_utils.BindingFragment

class ReceiveFragment : BindingFragment<FragmentReceiveBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentReceiveBinding::inflate




}