package digi.coders.thecapsico.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.mukesh.OtpView;

public class OtpReceiver extends BroadcastReceiver{

    private static OtpView otpView_otp;
    void setotpView(OtpView otpView){
        OtpReceiver.otpView_otp=otpView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages= Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage:smsMessages){
            String message_body=smsMessage.getMessageBody();
//            String getOtp=message_body.split(":")[1];
//            otpView_otp.setText(getOtp);
        }
    }
}
