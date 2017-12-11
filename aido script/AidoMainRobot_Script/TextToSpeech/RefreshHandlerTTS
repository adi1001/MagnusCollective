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
 * when we need a refresh or remove the message and get ready to handle new text.
 */

public class RefreshHandler extends Handler{
	
		//delay listener
		protected OnMessageCompletedListener onMessageCompeletdListener;

	    public void handleMessage(Message msg) {
		      this.removeMessages(0);
		      onMessageCompeletdListener.onMessageCompleted();
	    }


	    public interface OnMessageCompletedListener
		{
			 void onMessageCompleted();
		}
		
		public void setOnDelayCompletedListener(OnMessageCompletedListener l)
		{
			this.onMessageCompeletdListener = l;
		}

	    public void sleep(long delayMillis) {
	      this.removeMessages(0);
	      sendMessageDelayed(obtainMessage(0), delayMillis);
	    }

	  };


