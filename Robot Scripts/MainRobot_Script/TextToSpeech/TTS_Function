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

package TextToSpeech;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import properties.VoiceProperties;

/* This class was developed by Ingen Dynamics Inc. for use in Aido Robotics. This class is called
 * when Aido has to communicate with us based on the questions we ask.
 */
public class TextToSpeechFuncs {

    TextToSpeech textToSpeechFun;
    String speechToTalk = "";

    Context mainContext;

    boolean onGoingSpeech = false;

    /* The below 'TextToSpeechFuncs' is used to set the UK voice, the Value is received and the
     * function is executed.*/
    public TextToSpeechFuncs(Context context) {


        mainContext = context;

        textToSpeechFun = new TextToSpeech(mainContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechFun.setLanguage(Locale.UK);


                    Iterator prerec_voicelist = VoiceProperties.PROP_VOICE_HASH.entrySet().iterator();
                    while (prerec_voicelist.hasNext()) {
                        Map.Entry<String, Integer> pair = (Map.Entry) prerec_voicelist.next();
                        //Text to speech object textToSpeechFun is called and the value is passed.
                        textToSpeechFun.addSpeech(pair.getKey(), mainContext.getPackageName(), pair.getValue());

                    }

                    onReady();

                }
            }
        });


        isTTSSpeaking();

    }

    public static List<String> splitToList(String original, String separator) {
        List<String> nodes = new ArrayList<String>();
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.add(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.add(original);

        return nodes;
    }

    protected void onComplete() {

    }

    // Speak Function below is called when text to speech is required.
    public void speak(String texttospeak) {
        speechToTalk = texttospeak;

        onStart();

        List<String> listofspeechtext = processTextToSpeech(speechToTalk);

        for (int i = 0; i < listofspeechtext.size(); i++) {
            onGoingSpeech = true;
            textToSpeechFun.speak(listofspeechtext.get(i), TextToSpeech.QUEUE_ADD, null);
        }

    }

    // Can be used if speech has to be paused.
    public void pauseSpeech() {
        if (textToSpeechFun != null) {
            textToSpeechFun.stop();
        }

    }

    // Can be used to Shutdown
    public void shutdown() {
        if (textToSpeechFun != null) {
            textToSpeechFun.stop();
            textToSpeechFun.shutdown();
        }

    }

    // Complete text is passed to a list with the respective pair values with seperation.
    List<String> processTextToSpeech(String completetext) {
        List<String> retList = new ArrayList<String>();

        String sep = "|";


        Iterator prerec_voicelist = VoiceProperties.PROP_VOICE_HASH.entrySet().iterator();
        while (prerec_voicelist.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) prerec_voicelist.next();

            completetext = completetext.replaceAll("\\b" + pair.getKey() + "\\b", sep + pair.getKey().replace(" ", "ABPPA**AA") + sep);

        }


        completetext = completetext.replace("ABPPA**AA", " ");

        retList = splitToList(completetext, sep);


        // The List Array is returned
        return retList;
    }

    protected void inprogress() {

    }


    public boolean getTTSspeakingOrNot() {
        return onGoingSpeech;
    }


    protected void isTTSSpeaking() {

        final Handler h = new Handler();

        Runnable r = new Runnable() {

            public void run() {

                if (!textToSpeechFun.isSpeaking() && onGoingSpeech) {
                    if (onGoingSpeech) {
                        onComplete();
                        onGoingSpeech = false;

                    }
                } else {
                    inprogress();
                }
                h.postDelayed(this, 500);
            }
        };

        h.postDelayed(r, 500);
    }


    protected void onStart() {

    }


    protected void onReady() {

    }


}

