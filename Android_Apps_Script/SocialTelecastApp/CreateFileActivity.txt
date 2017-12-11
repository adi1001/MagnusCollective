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
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.google.android.gms.drive.DriveId.decodeFromString;

/*
 * This class is used to create files in the google drive and send image, video,image and text file.
 */

public class CreateFileActivity extends MainActivity {

    public static final int TAKE_PICTURE_REQUEST_CODE = 100;
    private static final String TAG = "CreateFileActivity";
    String encodeString;

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }
                    showMessage("Created a file with content: " + result.getDriveFile().getDriveId());
                    encodeString = result.getDriveFile().getDriveId().encodeToString();

                    if (encodeString != null) {

                        retrivecontent();
                    } else {
                    }
                }
            };

    File finalFile;
    // writes the content to the drive.This will writes the image,text and video.
    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }

                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            MetadataChangeSet changeSet;
                            // write content to DriveContents
                            if (finalFile != null) {
                                InputStream input = null;
                                try {
                                    input = new FileInputStream(finalFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                OutputStream outputStream = driveContents.getOutputStream();
                                byte[] buffer = new byte[16 * 1014];
                                int read = 0;

                                try {
                                    while ((read = input.read(buffer)) > 0) {
                                        outputStream.write(buffer, 0, read);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                changeSet = new MetadataChangeSet.Builder()
                                        .setTitle("image file")
                                        .setMimeType("image/*")
                                        .setStarred(true).build();
                            } else {
                                //write Text
                                OutputStream outputStream = driveContents.getOutputStream();
                                Writer writer = new OutputStreamWriter(outputStream);

                                try {
                                    writer.write("Hello World!");
                                    writer.close();
                                } catch (IOException e) {
                                    Log.e(TAG, e.getMessage());
                                }

                                changeSet = new MetadataChangeSet.Builder()
                                        .setTitle("New file")
                                        .setMimeType("text/plain")
                                        .setStarred(true).build();
                            }

                            // create a file on root folder
                            Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                    .createFile(getGoogleApiClient(), changeSet, driveContents)
                                    .setResultCallback(fileCallback);

                        }
                    }.start();
                }
            };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);

        takepicture();

    }

    // Illustrate how to retrieve and read file contents.
    public void retrivecontent() {

        final String[] contents = {null};
        final DriveId id = decodeFromString(encodeString);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                DriveFile file = id.asDriveFile();
                DriveApi.DriveContentsResult driveContentsResult =
                        file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
                if (!driveContentsResult.getStatus().isSuccess()) {
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
                    contents[0] = builder.toString();
                } catch (IOException e) {
                }
                driveContents.discard(getGoogleApiClient());
                return contents[0];
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();

    }

    public void editcontent() {
        final String[] contents = {null};

        final DriveId id = decodeFromString(encodeString);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                DriveFile file = id.asDriveFile();
                DriveApi.DriveContentsResult driveContentsResult =
                        file.open(getGoogleApiClient(), DriveFile.MODE_WRITE_ONLY, null).await();
                if (!driveContentsResult.getStatus().isSuccess()) {
                }
                DriveContents driveContents = driveContentsResult.getDriveContents();
                OutputStream outputStream = driveContents.getOutputStream();
                try {
                    outputStream.write("content edit".getBytes());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                com.google.android.gms.common.api.Status status =
                        driveContents.commit(getGoogleApiClient(), null).await();
                return status.getStatus().isSuccess();
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
            }
        }.execute();
    }

    public void takepicture() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE_REQUEST_CODE) {

            File file = new File(data.getData().getPath());
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            finalFile = new File(path);
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);

        }
    }
}

