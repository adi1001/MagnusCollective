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

package com.rathore.socialtelecast;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * This class is used to show the list of all the group name and its has two button which is
 * used to create the groupname and another to create a file into google drive.
 * using google Api to connect the google drive.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int CREATE_GROUP_REQUEST_CODE = 101;
    GoogleApiClient mGoogleApiClient;
    private DbHelper db;
    private Button btnNext;
    private RecyclerView recyclerViewDetail;
    private Button btnCreateFileMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DbHelper(MainActivity.this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)


                .build();

        btnCreateFileMain = (Button) findViewById(R.id.btnCreatefile);

        btnCreateFileMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ipos = new Intent(getApplicationContext(), CreateDriveFileActivity.class);
                startActivity(ipos);
            }
        });


        setGroupAdapter();

        btnNext = (Button) findViewById(R.id.btnNextMainACT);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBTN = new Intent(MainActivity.this, createGroup.class);
                startActivityForResult(iBTN, CREATE_GROUP_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
        }
        try {
            connectionResult.startResolutionForResult(this, 1);
        } catch (IntentSender.SendIntentException e) {
            // Unable to resolve, message user appropriately
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editgroup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mEditGroup) {
            Toast.makeText(this, "Edit Group", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Set the adapter in the recycler view.
    private void setGroupAdapter() {
        if (db != null) {
            HashMap<String, ArrayList<String>> detailList = db.getGroupDetails();
            recyclerViewDetail = (RecyclerView) findViewById(R.id.recyclerMain);
            createGroupAdapter adapater = new createGroupAdapter(this, detailList, getSupportFragmentManager());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerViewDetail.setLayoutManager(mLayoutManager);
            recyclerViewDetail.setItemAnimator(new DefaultItemAnimator());
            recyclerViewDetail.setAdapter(adapater);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mGoogleApiClient.connect();
            } else if (requestCode == CREATE_GROUP_REQUEST_CODE) {
                setGroupAdapter();
            }
        }
    }

    // when google drive is connected which enables the user to send the data.
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /*
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

}

