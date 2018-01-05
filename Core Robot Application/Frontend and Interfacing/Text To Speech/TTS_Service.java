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

package com.Aido.texttospeech;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;

import TextToSpeech.TextToSpeechFuncs;
import properties.VoiceProperties;

/* This class was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 * This class broadcasts the speech which was converted from text.
 */
public class TextToSpeechService extends Service {

    TextToSpeechFuncs ttsAidoFun;
    TextToSpeechReceiver ttsReceiver;

    public TextToSpeechService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // creating a 'TextToSpeechFuncs' object.
        ttsAidoFun = new TextToSpeechFuncs(this) {

            @Override
            protected void onReady() {
                super.onReady();
                ttsReceiver = new TextToSpeechReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        super.onReceive(context, intent);

                        if (intent.getAction().equalsIgnoreCase(VoiceProperties.PROP_TTS_INTENTID)) {

                            // Receives a broadcast text and stores into a string 'message'
                            String message = intent.getStringExtra(VoiceProperties.TTS_MESSSAGEFIELD);
                            // String 'message' is passed into the text to speech object
                            // for Aido to speak the same.
                            ttsAidoFun.speak(message);


                        }

                    }
                };

                IntentFilter filter = new IntentFilter();
                filter.addAction(VoiceProperties.PROP_TTS_INTENTID);
                registerReceiver(ttsReceiver, filter);

            }

            @Override
            protected void onStart() {
                super.onStart();
                // audioManager is used to set the volume.
                AudioManager audioManager =
                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        0);

                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                        0);

            }


            @Override
            protected void inprogress() {
                super.inprogress();
                AudioManager audioManager =
                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                // audioManager is used to set the volume.
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        0);

                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                        0);

            }

            @Override
            protected void onComplete() {
                super.onComplete();
                // Delay fucntion is called.
                SetDelay sd = new SetDelay(500);
                sd.setOnDelayCompletedListener(new SetDelay.OnDelayCompletedListener() {
                    @Override
                    public void onDelayCompleted() {
                        Intent intent = new Intent();
                        intent.setAction(VoiceProperties.PROP_TTS_RECEIVER_INTENTID);
                        intent.putExtra(VoiceProperties.PROP_SPEECHTOTEXT_MESSSAGEFIELD, "start");
                        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        sendBroadcast(intent);

                    }
                });


            }
        };


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        // Close text to speech object else capture the error.
        try {
            ttsAidoFun.shutdown();
        } catch (Exception ex) {

        }
        // Close text to speech receiver object else capture the error.
        try {
            unregisterReceiver(ttsReceiver);
        } catch (Exception ex) {

        }
        super.onDestroy();
    }


}

