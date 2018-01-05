/*Created on 15/02/17 by Ingen Dynamics Inc. This program is free software:
 *you can redistribute it and/or modify it under the terms of the GNU General
 *Public License as published by the Free Software Foundation,
 *either version 3 of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rathore.aidoalertsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.rathore.aidoalertsystem.Service.BackgroundService;

/*This Receiver was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This Receiver is used to re-start the service.
 */

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
        Intent i = new Intent(context, BackgroundService.class);    // Start your service class
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);

    }
    }
}

