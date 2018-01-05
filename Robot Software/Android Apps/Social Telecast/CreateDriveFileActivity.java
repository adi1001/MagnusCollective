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


import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.android.gms.drive.DriveId.decodeFromString;

/*
 * This code is used to create file in google drive and share the file eg: video,txt,image, etc.
 * with others. There are two fields one in which the file name is entered and in another field in
 * which message is sent to others as an attachment.
 */

public class CreateDriveFileActivity extends MainActivity {

    private static final String TAG = "CreateFileActivity";
    private EditText txtCreateFileDr, fileName;
    File finalFile;
    Button selectOption;
    String inputText = null, fileNamee = null;
    String encodeString;

    //This method creates the file in google drive with content.
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

                    } else {

                    }
                }
            };
    //This method is used to attach the file example:image,text file. while creating the new file in drive.
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
                            MetadataChangeSet changeSet = null;

                            // write image to DriveContents.
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
                                        .setTitle(fileNamee)

                                        .setMimeType("image/*")
                                        .setStarred(true).build();
                            } else {
                                //write Text to google drive.
                                if (inputText != null) {
                                    OutputStream outputStream = driveContents.getOutputStream();
                                    Writer writer = new OutputStreamWriter(outputStream);
                                    try {
                                        writer.write(inputText);
                                        writer.close();
                                    } catch (IOException e) {
                                        Log.e(TAG, e.getMessage());
                                    }

                                    changeSet = new MetadataChangeSet.Builder()
                                            .setTitle(fileNamee)
                                            .setMimeType("text/plain")
                                            .setStarred(true).build();
                                } else {

                                }
                            }

                            // create a file on root folder in google drive.
                            Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                    .createFile(getGoogleApiClient(), changeSet, driveContents)
                                    .setResultCallback(fileCallback);

                        }
                    }.start();
                }
            };

    MainActivity mainActivity = new MainActivity();

    //This method identifies type of data and reads it in particular format.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    //this method is not used/yet to be implemented
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if
                (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {

                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void Done(View view) {

        inputText = txtCreateFileDr.getText().toString();

    }

    public void listOfFiles(View view) {
        Intent intent = new Intent(getApplicationContext(), PickFileWithOpenerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.createfiledrive);

        selectOption = (Button) findViewById(R.id.selectFileCreateFileDriveAct);

    }

    //This method is used to open the dialog box to select the file eg:picture,gallery,video.
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        Log.i("oncreate", "called");
        setContentView(R.layout.createfiledrive);

        selectOption = (Button) findViewById(R.id.selectFileCreateFileDriveAct);
        txtCreateFileDr = (EditText) findViewById(R.id.edCreateFileDrive);
        fileName = (EditText) findViewById(R.id.fileName);

        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Camera", "Select image", "Select video", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateDriveFileActivity.this);
                builder.setTitle("Add file");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Camera")) {
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            startActivityForResult(i, 1);
                        } else if (items[item].equals("Select image")) {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setType("image/*");
                            startActivityForResult(Intent.createChooser(i, "Select image"), 2);

                        } else if (items[item].equals("Select video")) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("video/*");
                            startActivityForResult(Intent.createChooser(intent, "Select video"), 3);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

    }
    //This illustrates how to retrieve and read file contents.
    public void retrivecontent() {

        final String[] contents = {null};

        final DriveId id = decodeFromString(encodeString);
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                DriveFile file = id.asDriveFile();
                DriveResource.MetadataResult metadataResult = file.getMetadata(getGoogleApiClient()).await();
                String contentlink = metadataResult.getMetadata().getWebContentLink();

                if (metadataResult != null && metadataResult.getStatus().isSuccess()) {
                    String link = metadataResult.getMetadata().getWebContentLink();

                } else {

                }

                DriveApi.DriveContentsResult driveContentsResult =
                        file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
                if (!driveContentsResult.getStatus().isSuccess()) {

                }
                DriveContents driveContents = driveContentsResult.getDriveContents();

                return contentlink;
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

    //This method is used to open the camera.
    public void takepicture() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = null;

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {

            if (requestCode == 1) {

                selectedImage = data.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(photo);
                String imagepath = String.valueOf(selectedImage);

                Cursor cursor = getContentResolver().query(tempUri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String path = cursor.getString(idx);
                finalFile = new File(path);
                Drive.DriveApi.newDriveContents(getGoogleApiClient())
                        .setResultCallback(driveContentsCallback);


            }

            if (requestCode == 2) {

                selectedImage = data.getData();
                String imagepath = String.valueOf(selectedImage);
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex);
                String mimetype = getMimeType(picturePath);

                finalFile = new File(picturePath);

                cursor.close();

                Drive.DriveApi.newDriveContents(getGoogleApiClient())
                        .setResultCallback(driveContentsCallback);
            }

            if (requestCode == 3) {
                selectedImage = data.getData();
                String imagepath = String.valueOf(selectedImage);

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImage);

                Drive.DriveApi.newDriveContents(getGoogleApiClient())
                        .setResultCallback(driveContentsCallback);

            }


        } else {
        }
    }
   //This is used to get the corresponding path to a file from the given content
    public String getPath(Uri uri) {

        String filePath = null;

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat) {
            filePath = generateFromKitkat(uri);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";


            Cursor cursor = getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getFileNameByUri(Uri uri) {

        String filepath = "";//default fileName

        File file;

        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images
                    .ImageColumns.DATA, MediaStore.Images.Media.DATA}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        } else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            filepath = uri.getPath();
        }
        return filepath;

    }

    public String getPDFPath(Uri uri) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        inputText = txtCreateFileDr.getText().toString();
        fileNamee = fileName.getText().toString();
        if (inputText != null) {
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
        } else {
        }
    }
}

