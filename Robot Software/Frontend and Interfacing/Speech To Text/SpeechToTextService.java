/*
    Created on 15/02/17 by Ingen Dynamics Inc. as main Behaviour engine code for Aido Robot.
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.Aido.speechtotext;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import googlevoice.RecognizeSpeech;

/* This class was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 * This class broadcasts the text which was converted from speech.
 */

public class SpeechToTextService extends Service {

    //Creating required objects.
    RecognizeSpeech sttAido;
    SpeechToTextReceiver sttReceiver;

    String broadcastIntentId = "com.whitesuntech.speechtotext";
    String receiverId = "com.whitesuntech.stt";
    String messageField = "message";

    public SpeechToTextService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();


        sttReceiver = new SpeechToTextReceiver()

        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                if (intent.getAction().equalsIgnoreCase(receiverId)) {

                    // message received from broadcast is stored in string "message' to
                    // initiate doSpeechRecognition
                    String message = intent.getStringExtra(messageField);
                    // check if 'message' equals start, if its equal to 'start' call doSpeechRecognition
                    if (message.equalsIgnoreCase("start")) {
                        doSpeechRecognition();
                    }


                }

            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(receiverId);
        registerReceiver(sttReceiver, filter);


    }

    /*Calls 'RecognizeSpeech' class and converts the speech to text and broadcasts the
     * same for further processing*/
    void doSpeechRecognition() {

        // Create a 'RecognizeSpeech' object and passes this context
        sttAido = new RecognizeSpeech(this) {
            @Override
            public void onComplete(final String result) {
                super.onComplete(result);
                //Result is broadcast.
                Intent intent = new Intent();
                intent.setAction(broadcastIntentId);
                intent.putExtra(messageField, result);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);

            }


        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        // Close Speech recognition object else capture the error.
        try {
            sttAido.close();
        } catch (Exception ex) {

        }
        // Close Speech to text receiver object else capture the error.
        try {
            unregisterReceiver(sttReceiver);
        } catch (Exception ex) {

        }
        super.onDestroy();
    }


}

