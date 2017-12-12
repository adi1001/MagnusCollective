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

package googlevoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

/* This class was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 * This class is called to convert speech to text. */

public class RecognizeSpeech {

    // Initializing required objects for speech recognition.
    private static boolean mIslistening = false;
    Context mainContext;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    public RecognizeSpeech(Context context) {
        mainContext = context;

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mainContext);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,
                1);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                mainContext.getPackageName());

        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);

        //If mIslistening is false then initiate mIslisteningto true to start listening to the speech
        if (!mIslistening) {

            mIslistening = true;
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        } else {


            onComplete("GOT ERROR 8");
        }


    }

    // On complete destroy object mSpeechRecognizer
    public void onComplete(String result) {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }

    }

    // On close destroy object mSpeechRecognizer
    public void close() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }

    public void onEndOfSpeechCompleted() {

    }


    public void onReadyToListenCompleted() {

    }


    protected class SpeechRecognitionListener implements RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            onReadyToListenCompleted();
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            onEndOfSpeechCompleted();
        }

        @Override
        public void onError(int error) {
            // If error then make mIslistening false.
            onComplete("GOT ERROR " + error);
            mIslistening = false;

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            mIslistening = false;

        }

        @Override
        public void onResults(Bundle results) {
            mIslistening = false;
            ArrayList<String> suggestedWords = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            // matches are the return values of speech recognition engine
            // Use these values for whatever you wish to do
            onComplete(suggestedWords.get(0));
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

    }
}
