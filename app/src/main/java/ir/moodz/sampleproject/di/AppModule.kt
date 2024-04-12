package ir.moodz.sampleproject.di

import android.content.BroadcastReceiver
import android.telephony.SmsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.moodz.sampleproject.data.repository.SmsListener
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSmsManager() : SmsManager{

        return SmsManager.getDefault()

    }

}