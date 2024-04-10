package ir.moodz.sampleproject.feature_sms.presentation.send_sms

import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SmsViewModel @Inject constructor(
    private val smsManager: SmsManager
) : ViewModel() {

    private val _smsStatus = MutableLiveData<SmsStatus>()
    val smsStatus : LiveData<SmsStatus> = _smsStatus


    sealed class SmsStatus{
        data object Sending: SmsStatus()
        data object Sent: SmsStatus()
        data class Error (val error: String) : SmsStatus()

    }

    fun sendSMS(message:String , number: String){

        viewModelScope.launch(Dispatchers.IO){

            _smsStatus.postValue(SmsStatus.Sending)

            try {

                smsManager.sendTextMessage(
                    number,
                    null,
                    message,
                    null,
                    null
                )
                delay(2.seconds)
                _smsStatus.postValue(SmsStatus.Sent)

            } catch (e: Exception){
                _smsStatus.postValue(SmsStatus.Error(e.message.toString()))
            }



        }


    }



}