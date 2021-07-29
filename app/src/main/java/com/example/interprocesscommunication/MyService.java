package com.example.interprocesscommunication;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService extends Service {
    private final int JOB_1 = 1;
    private final int JOB_2 = 2;
    private final int JOB_1_RESPONSE = 3;
    private final int JOB_2_RESPONSE = 4;
    Messenger messenger = new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            Message MSG;
            String Message;
            Bundle bundle = new Bundle();
            switch (msg.what) {
                case JOB_1:
                    Message = "This is the first message from service...";
                    MSG = android.os.Message.obtain(null, JOB_1_RESPONSE);

                    //Now we have to put data into the message  object
                    bundle.putString("response_message", Message);
                    MSG.setData(bundle);
                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                case JOB_2:
                    Message = "This is the second message from service...";
                    MSG = android.os.Message.obtain(null, JOB_2_RESPONSE);

                    //Now we have to put data into the message  object
                    bundle.putString("response_message", Message);
                    MSG.setData(bundle);
                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:
                    super.handleMessage(msg);
            }
            super.handleMessage(msg);

        }

    }
}