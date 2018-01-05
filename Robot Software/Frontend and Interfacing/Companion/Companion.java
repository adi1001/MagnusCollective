/*
    Created on 15/02/17 by Ingen Dynamics Inc.
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
package com.whitesuntech.skypeexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.List;

/*
 * This class is used to set the skype id and user name then it update this in firebase.
 * user triggers a flag to aido to make a skype call on user's skype id after setting up skype name
 * and id in the app. User can see what aido sees after pairing aido with his android device and
 * move the motors using accelerometer. The data between them is in constant sync with firebase.
 */

public class SkypeCall extends AppCompatActivity {

    Button makeCallP1Video;
    Button makeCallP1Audio;
    Button makeCallP1Chat;
    Button btnPairWithAido;

    String prefSkypeName = "aidoskypename";
    String prefAidoId = "aidoId";

    String skypeName = "";
    String aidoId = "";


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference fireDbReference = database.getReference();

    SensorManager sensorManager;
    SensorEventListener sensorlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityskypecall);

        btnPairWithAido = (Button) findViewById(R.id.btnPairWithAido);
        btnPairWithAido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new IntentIntegrator(SkypeCall.this).initiateScan();
            }
        });

        makeCallP1Video = (Button) findViewById(R.id.btnCallUserVideo);
        makeCallP1Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checksetup()) {
                    btnPairWithAido.performClick();
                    return;
                }

                fireDbReference.child(aidoId).child("companion").child("call").setValue("1");

            }
        });

        makeCallP1Audio = (Button) findViewById(R.id.btnCallUserAudio);
        makeCallP1Audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checksetup()) {
                    btnPairWithAido.performClick();
                    return;
                }

                initiateSkypeUri(getApplicationContext(), "skype:" + skypeName + "?call&audio=true");
            }
        });

        makeCallP1Chat = (Button) findViewById(R.id.btnAccelerometerPublish);

        //Checks the firebase connection and paired the app in user device with the aido.
        makeCallP1Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Trying firebase !!", Toast.LENGTH_LONG).show();

                if (!checksetup()) {
                    btnPairWithAido.performClick();
                    return;
                }


                getAccelerometerValues();

            }
        });


        if (!checksetup()) {
            btnPairWithAido.performClick();
        }

        getAccelerometerValues();

        fireDbReference.child(aidoId).child("companion").child("call").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue().toString();


                if (CommonlyUsed.stringIsNullOrEmpty(value)) {
                    return;
                }
                if (value.equalsIgnoreCase("0")) {
                    return;
                }
                if (value.equalsIgnoreCase("1")) {
                    return;
                }
                if (value.equalsIgnoreCase("2")) {
                    CommonlyUsed.messageBox(SkypeCall.this, "Unable to Connect", "There was a problem connecting to Aido. Have you paired this app with Aido ?", "Close");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            sensorManager.unregisterListener(sensorlistener);
        } catch (Exception ex) {
        }
    }
    //checks the device is paired with aido.
    public boolean checksetup() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        skypeName = settings.getString(prefSkypeName, "");
        aidoId = settings.getString(prefAidoId, "");


        if (CommonlyUsed.stringIsNullOrEmpty(skypeName) || CommonlyUsed.stringIsNullOrEmpty(aidoId)) {
            CommonlyUsed.messageBox(SkypeCall.this, "Pairing with Aido", "Aido needs to be paired with this app. Please use Aido Settings (setup) to pair.", "Start");

            return false;
        }

        CommonlyUsed.showMsg(this, "Aido skype=" + skypeName + " , " + "ID=" + aidoId);


        return true;
    }

    public void initiateSkypeUri(Context myContext, String mySkypeUri) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(mySkypeUri);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(myIntent);

        return;
    }
    // Make sure the Skype for Android client is installed.
    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }

    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }

    // According to the movement of user phone the aido neck will move in x and y cordinates.
    public void getAccelerometerValues() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorlistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                fireDbReference.child(aidoId).child("companion").child("acc").child("accx").setValue("" + x);
                fireDbReference.child(aidoId).child("companion").child("acc").child("accy").setValue("" + y);
                fireDbReference.child(aidoId).child("companion").child("acc").child("accz").setValue("" + z);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

        };
        sensorManager.registerListener(sensorlistener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = settings.edit();

                List<String> tokens = CommonlyUsed.splitToList(result.getContents(), "||");
                editor.putString(prefAidoId, tokens.get(0));
                editor.putString(prefSkypeName, tokens.get(1));

                editor.commit();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}

