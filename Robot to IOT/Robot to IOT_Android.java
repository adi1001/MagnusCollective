/*
    Created on 25/08/17 by Ingen Dynamics Inc.
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

package com.example.ajeet.ipfs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* Created on 25/11/17 by Ingen Dynamics Inc.
 * This class is used to perform a handshake between the robot and AI intern performing a
 * transaction by Token transfer for a particular data or information.
 */
public class MainActivity extends Activity {

    private DatabaseReference rootRef;
    private DatabaseReference demoRef;
    private Button btnSend;
    private Spinner mSpinner;
    private TextView mt1;
    private TextView mt2;
    private TextView mt3;
    private TextView mt4;
    private TextView mt5;
    private Button mBalance;
    private Button btnMove;
    private Button mbtnData;
    private ImageView mImgView;
    private TextView mTxtViewFB;
    private String value;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        //Create a Builder that detects nothing and has no violations.
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnSend = (Button) findViewById(R.id.btnMain);
        mBalance = (Button) findViewById(R.id.btnBalance);
        mTxtViewFB = (TextView) findViewById(R.id.datafb);
        mt1 = (TextView) findViewById(R.id.t1);
        mt2 = (TextView) findViewById(R.id.t2);
        mt3 = (TextView) findViewById(R.id.t3);
        mt4 = (TextView) findViewById(R.id.t4);
        mt5 = (TextView) findViewById(R.id.t5);

        getDataAido1();
        getDataAido2();
        getDataIOT();
        getDataAI();
        getDataHuman();

        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        demoRef = rootRef.child("rflags");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestForData();

            }
        });

        mBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataAido1();
                getDataAido2();
                getDataIOT();
                getDataAI();
                getDataHuman();

                getAIData();

            }
        });


    }

    //when the transaction has complete.It Retrive the Token amount from the running script and
    //show the balance.
    public void getDataAido1() {

                HttpURLConnection urlConnection = null;
                String result = "";
                try {
                    URL url = new URL("http://ip_address/getTokenValueAido1.php?");
                    urlConnection = (HttpURLConnection) url.openConnection();

                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        if (in != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";

                            while ((line = bufferedReader.readLine()) != null)
                                result += line;
                        }
                        in.close();
                    }

                    mt1.setText(result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    

    //when the transaction has complete.It Retrive the Token amount from the running script and
    //show the balance.
    public void getDataAido2() {
        
                HttpURLConnection urlConnectionAido2 = null;
                String resultAido2 = "";
                try {
                    URL url = new URL("http://ip_address/getTokenValueAido2.php?");
                    urlConnectionAido2 = (HttpURLConnection) url.openConnection();

                    int code = urlConnectionAido2.getResponseCode();
                    if (code == 200) {
                        InputStream in = new BufferedInputStream(urlConnectionAido2.getInputStream());
                        if (in != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";

                            while ((line = bufferedReader.readLine()) != null)
                                resultAido2 += line;
                        }
                        in.close();
                    }

                    mt2.setText(resultAido2);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    

    //when the transaction has complete.It Retrive the Token amount from the running script and
    //show the balance.
    public void getDataIOT() {

                HttpURLConnection urlConnectionIOT = null;
                String resultHuman = "";
                try {
                    URL url = new URL("http://ip_address/getTokenValueIOT.php?");
                    Log.i("data", url.toString());
                    urlConnectionIOT = (HttpURLConnection) url.openConnection();

                    int code = urlConnectionIOT.getResponseCode();
                    if (code == 200) {
                        InputStream in = new BufferedInputStream(urlConnectionIOT.getInputStream());
                        if (in != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";

                            while ((line = bufferedReader.readLine()) != null)
                                resultHuman += line;
                        }
                        in.close();
                    }

                    mt3.setText(resultHuman);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    

    //when the transaction has complete.It Retrive the Token amount from the running script and
    //show the balance.


    public void getDataAI() {

                HttpURLConnection urlConnectionAI = null;
                String resultAI = "";
                try {
                    URL url = new URL("http://ip_address/getTokenValueAI.php?");
                    Log.i("data", url.toString());
                    urlConnectionAI = (HttpURLConnection) url.openConnection();

                    int code = urlConnectionAI.getResponseCode();
                    if (code == 200) {
                        InputStream in = new BufferedInputStream(urlConnectionAI.getInputStream());
                        if (in != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";

                            while ((line = bufferedReader.readLine()) != null)
                                resultAI += line;
                        }
                        in.close();
                    }

                    mt4.setText(resultAI);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

     

    //when the transaction has complete.It Retrive the Token amount from the running script and
    //show the balance.
    public void getDataHuman() {

                HttpURLConnection urlConnectionHuman = null;
                String resultIOT = "";
                try {
                    URL url = new URL("http://ip_address/getTokenValueHuman.php?");
                    Log.i("data", url.toString());
                    urlConnectionHuman = (HttpURLConnection) url.openConnection();

                    int code = urlConnectionHuman.getResponseCode();
                    if (code == 200) {
                        InputStream in = new BufferedInputStream(urlConnectionHuman.getInputStream());
                        if (in != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";

                            while ((line = bufferedReader.readLine()) != null)
                                resultIOT += line;
                        }
                        in.close();
                    }

                    mt5.setText(resultIOT);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            

    }

    //The selected robot would send request for the data.
    public void requestForData() {

        //database reference pointing to demo node
        mSpinner = (Spinner) findViewById(R.id.spinnerMain);
        demoRef = rootRef.child("rflags");

        String Robots = mSpinner.getSelectedItem().toString();

        demoRef.child("riot").setValue("IOT");

        }
    //Retrive url from the firebase and read the data from it.
    public void getAIData() {

        demoRef.child("IoT_data").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("hcodeinfo").getValue(String.class);

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("");
                progressDialog.setMessage("Refreshing...");
                progressDialog.show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        HttpURLConnection urlConnectionAI = null;
        String resultAI = "";
        try {
            URL url = new URL(value);
            Log.i("data", url.toString());
            urlConnectionAI = (HttpURLConnection) url.openConnection();

            int code = urlConnectionAI.getResponseCode();
            if (code == 200) {
                InputStream in = new BufferedInputStream(urlConnectionAI.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        resultAI += line + "\n";
                }
                in.close();
            }

            mTxtViewFB.setText(resultAI);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}

