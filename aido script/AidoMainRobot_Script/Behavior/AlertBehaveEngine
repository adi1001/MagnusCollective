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

package aido.UI;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.TextureView;

import aido.TextToSpeech.BroadcastTTS;
import aido.properties.AlertBehaviourProperties;
import aido.properties.BehaveProperties;

/*
 * This code is used to check if the demanded behaviour priority is higher than the default
 * behaviour priority. If the priority is higher than the default demoplay behaviour, then the
 * default behaviour is interrupted and the behaviour with higher priority is executed.
 * A behaviour can also be directly executed by selecting it from the main menu located at the
 * Aido main screen, the code for this is also present below.
 */

public class AlertBehaveEngine {

    Context context;
    String piIP;
    TextureView videoView;
    SurfaceTexture surfaceTexture;
    String demoFile;
    BroadcastTTS ttsAido;

    DemoPlay dp;

    /*Its take the pi ipAddress:10.10.10.1 ,surface structure and Text to speech values from aidoface class
    play accordingly.*/
    public AlertBehaveEngine(Context context, String piIPAddress, TextureView videoViews, SurfaceTexture st, BroadcastTTS ttsAido) {

        this.context = context;
        piIP = piIPAddress;
        this.videoView = videoViews;
        surfaceTexture = st;
        this.ttsAido = ttsAido;
    }

    //This method resets the TTS  based on the priority.
    public void reset() {
        if (dp != null) {
            if (!dp.isPlaying()) {
                dp.setPriority(-1);
            }
        }

    }

    //This method shows the notification of the current behavior.
    public void registerNotification(int alertBehaveID) {
        int priority = AlertBehaviourProperties.alertBEHAVIOURPRIORITY.get(alertBehaveID);

        if (dp != null) {
            if (priority >= dp.getPriority()) {
                dp.interrupt();

                //default demoplay video is stoped and plays the new behaviour/animation.
                demoFile = BehaveProperties.getRawBehaviorDir() + AlertBehaviourProperties.alertBEHAVIUORHASH.get(alertBehaveID);
                dp = new DemoPlay(context, piIP, videoView, surfaceTexture, demoFile, ttsAido, priority, alertBehaveID);
                dp.play();

            }
        } else {
            demoFile = BehaveProperties.getRawBehaviorDir() + AlertBehaviourProperties.alertBEHAVIUORHASH.get(alertBehaveID);
            dp = new DemoPlay(context, piIP, videoView, surfaceTexture, demoFile, ttsAido, priority, alertBehaveID);
            dp.play();

        }

    }

    /*This method is to access the behavior file from the particular directory from the storage and
    play based on priority. */
    public void directRun(String behaviorFile) {
        int priority = 50;

        if (dp != null) {
            if (priority >= dp.getPriority()) {
                dp.interrupt();
                demoFile = behaviorFile;
                dp = new DemoPlay(context, piIP, videoView, surfaceTexture, demoFile, ttsAido, priority, AlertBehaviourProperties.directRun);
                dp.play();
            }
        } else {
            demoFile = behaviorFile;
            dp = new DemoPlay(context, piIP, videoView, surfaceTexture, demoFile, ttsAido, priority, AlertBehaviourProperties.directRun);
            dp.play();
        }

    }

    //If the demo play is not null then it plays the current animation/behaviour.
    public boolean isPlaying() {
        if (dp != null) {
            return dp.isPlaying();
        }
        return false;
    }

    //If the demoplay object is not null then it plays the user selected behaviour.
    public Integer currentBehavior() {
        if (dp != null) {
            if (dp.isPlaying()) {
                return dp.getBehaveid();
            }
        }
        return AlertBehaviourProperties.none;
    }

    //If the demoplay is not null then it plays the default behaviour
    public String getDemoFile() {
        if (dp != null) {
            if (dp.isPlaying()) {
                return dp.getDemoFile();
            }
        }
        return demoFile;
    }


    public void setSurface(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
        if (dp != null) {
            dp.setSurface(surfaceTexture);
        }
    }

}

