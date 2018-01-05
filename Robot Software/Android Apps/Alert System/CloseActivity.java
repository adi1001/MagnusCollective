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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*This Activity was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This Activity is used to close the application.
 */

public class CloseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close);
        Intent intentbr = getIntent();
        if(intentbr!=null){
            if(intentbr.getAction().equals("com.rathore.aidoalertsystem.CloseActivity"));
            String data = intentbr.getStringExtra("data");
            if(data!=null){
                if(data.equals("close it")){
                    finishAffinity();


                    System.exit(0);
                }
            }}
    }
}

