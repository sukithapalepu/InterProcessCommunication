package com.example.interprocesscommunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final int JOB_1 = 1;
    private final int JOB_2 = 2;
    private final int JOB_1_RESPONSE = 3;
    private final int JOB_2_RESPONSE = 4;
    boolean isBind =false;
    Messenger messenger = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        textView = (TextView)findViewById(R.id.textView);
    }

    //This is called when the connection with the service has been
    //established, giving us the service object we can use to
    //   interact with the service.
    ServiceConnection serviceConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            isBind = true;
        }
        //This is called when the connection with the service has been
        // unexpectedly disconnected
        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
            isBind = false;

        }
    };
    public void getMessage(View view) {
        Message msg;
        String button_text = (String) ((Button) view).getText();
        if (button_text.equals("GET FIRST MESSAGE")) {
            //This is for first button
            msg = Message.obtain(null, JOB_1);
            msg.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if (button_text.equals("GET SECOND MESSAGE")) {
            //This is for second button
            msg = Message.obtain(null, JOB_2);
            msg.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        isBind = false;
        messenger = null;
        super.onStop();
    }
    class ResponseHandler extends Handler {
        @Override
        public void handleMessage( Message msg) {
            String message;
            switch (msg.what)
            {
                //This is the first response
                case JOB_1_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
                    break;
                //This is the second response
                case JOB_2_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
                    break;


            }
            super.handleMessage(msg);
        }
    }
}
