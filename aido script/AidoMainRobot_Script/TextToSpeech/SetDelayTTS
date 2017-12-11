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

import android.os.Handler;
import android.os.Message;

/* This class was developed by Ingen Dynamics Inc. for use in Aido Robotics. This class is called
 * when we need a delay in response.
 */
public class SetDelay {

    // delay listener
    protected OnDelayCompletedListener onDelayCompeletdListener;
    RefreshHandler refrshHandler;
    int delayInMilliSecs;


    // Delay can be set in milliSecs
    public SetDelay(int milliSecs) {
        // TODO Auto-generated constructor stub
        refrshHandler = new RefreshHandler();
        delayInMilliSecs = milliSecs;
        refrshHandler.sleep(delayInMilliSecs);

    }

    public void setOnDelayCompletedListener(OnDelayCompletedListener l) {
        this.onDelayCompeletdListener = l;
    }

    public interface OnDelayCompletedListener {
        public void onDelayCompleted();
    }

    class RefreshHandler extends Handler {

        //delay listener
        public void handleMessage(Message msg) {
            this.removeMessages(0);
            onDelayCompeletdListener.onDelayCompleted();

        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }

    }

    ;
}

