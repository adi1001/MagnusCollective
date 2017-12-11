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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.google.android.gms.drive.DriveId.decodeFromString;

/* Created on 25/08/17 by Ingen Dynamics Inc.
 * This class is used to illustrate how to retrieve and read file contents.
 */

public class RetrieveContentsActivity extends MainActivity {
    private static final String TAG = "RetrieveContentsActivity";
    DriveId id;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Intent  intent=getIntent();
        if(intent!=null){
             id=decodeFromString(intent.getStringExtra("driveid"));


        }
    }

    //Using google Api connection is established.
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), "0ByfSjdPVs9MZTHBmMVdSeWxaNTg")
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {

            new RetrieveDriveFileContentsAsyncTask(
                    RetrieveContentsActivity.this).execute(id);
        }
    };

    final private class RetrieveDriveFileContentsAsyncTask
            extends ApiClientAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {

            String contents = null;

            DriveFile file = id.asDriveFile();
            DriveApi.DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(driveContents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                contents = builder.toString();
            } catch (IOException e) {

            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                showMessage("Error while reading from the file");
                return;
            }
            showMessage("File contents: " + result);
        }

    }


}

