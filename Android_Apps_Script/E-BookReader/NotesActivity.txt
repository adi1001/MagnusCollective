/*  Created on 20/08/17 by Ingen Dynamics Inc.
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

package com.rathore.evernoteapi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.conn.mobile.FileData;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* This class is used to create a new note by user */
public class NotesActivity extends AppCompatActivity {

    static ArrayList<Resource> resourceList = new ArrayList<>();
    EditText notesTitle, notesContent, notebook;
    String notetitle, noteContent, noteBook;
    EvernoteNoteStoreClient noteStoreClient;
    Resource resource;
    String notebookGuid;
    ArrayList<String> namesList, notenamelist;
    ArrayList<Notebook> notebookslist;
    UserStoreClient userStore;
    TextView notebookTitle;
    String name = null;
    InputMethodManager inputManager;
    RequestQueue queue;
    String noteName;
    String mCurrentPhotoPath;
    TextView attachment;
    int attachmentCount = 0;
    ProgressDialog pd;

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath11(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // Document Provider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // External Storage Provider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // Downloads Provider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // Media Provider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(NotesActivity.this);

        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolBarTitle);
        mTitle.setText("SAVE");
        setTitle("SAVE");
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (notebookTitle != null) {
                    noteContent = notesContent.getText().toString();
                    String notebooktitle = notebookTitle.getText().toString();
                    for (int i = 0; i < notebookslist.size(); i++) {

                        if (notebooktitle.equals(notebookslist.get(i).getName())) {


                            try {
                                // create new note.
                                createNote(noteName, noteContent, notebookslist.get(i));

                            } catch (EDAMNotFoundException e) {
                                e.printStackTrace();
                            } catch (TException e) {
                                e.printStackTrace();
                            } catch (EDAMUserException e) {
                                e.printStackTrace();
                            } catch (EDAMSystemException e) {
                                e.printStackTrace();
                            }

                        } else {

                        }
                    }

                } else {
                    Toast.makeText(NotesActivity.this, "Some Fields are missing note not created", Toast.LENGTH_SHORT).show();
                }


            }
            //  }
        });

        attachment = (TextView) findViewById(R.id.attachment);

        inputManager =
                (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        notesTitle = (EditText) findViewById(R.id.noteTitle);
        notesTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    inputManager.hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    noteName = notesTitle.getText().toString();


                }

                return true;
            }
        });
        notebookTitle = (TextView) findViewById(R.id.noteBookTitle);
        notesContent = (EditText) findViewById(R.id.content);

        ImageView addButton = (ImageView) findViewById(R.id.addbuttonnotesActivity);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog box is used to select the file example:pdf,image,video.
                final Dialog dialog = new Dialog(NotesActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.customdialog);
                LinearLayout camera = (LinearLayout) dialog.findViewById(R.id.camera);
                LinearLayout selectfiles = (LinearLayout) dialog.findViewById(R.id.selectFiles);
                LinearLayout selectimages = (LinearLayout) dialog.findViewById(R.id.selectImages);
                LinearLayout selectvideos = (LinearLayout) dialog.findViewById(R.id.selectVideos);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, 1);
                        dialog.dismiss();
                    }
                });
                selectfiles.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                        intent.setType("application/pdf");
                        startActivityForResult(Intent.createChooser(intent, "attach file"), 3);
                        dialog.dismiss();
                    }
                });
                selectimages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "Select image"), 2);
                        dialog.dismiss();
                    }
                });
                selectvideos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        startActivityForResult(Intent.createChooser(intent, "Select video"), 4);
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, "S=s1:U=8f219:E=154308dc976:C=14cd8dc9cd8:P=1cd:A=en-devtoken:V=2:H=1e4d28c7982faf6222ecf55df3a2e84b");
        final ClientFactory factory = new ClientFactory(evernoteAuth);
        try {
            userStore = factory.createUserStoreClient();
        } catch (TTransportException e) {
            e.printStackTrace();
        }

        notenamelist = new ArrayList<>();
        noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        notebookslist = new ArrayList<>();

        listNotebooks();


        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (intent.getAction().equals("FragmentActivity")) {

                notebookTitle.setText(intent.getStringExtra("name"));
            }
            if (intent.getAction().equals("BookList")) {
                notebookTitle.setText(intent.getStringExtra("notebook"));
            } else if (intent.getAction().equals("AllNotes")) {
                notesTitle.setText(intent.getStringExtra("noteName"));
                notebookTitle.setText(intent.getStringExtra("notebook"));
                Bitmap bitmap = (Bitmap) intent.getExtras().get("Bitmap");

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageBitmap(bitmap);

            } else {
            }

        } else {
            notebookTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), BookList.class);
                    intent.putStringArrayListExtra("namelist", namesList);
                    startActivityForResult(intent, 5);

                }
            });
        }


        notebookTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (namesList != null) {
                    Intent intent = new Intent(getApplicationContext(), BookList.class);
                    intent.putStringArrayListExtra("namelist", namesList);
                    startActivityForResult(intent, 5);
                } else {
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NotesActivity.this.finish();
    }

    public void createNote(final String title, String content, final Notebook notebook) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
        pd.setMessage("loading");
        pd.show();
        String nBody = null;
        final Note note = new Note();

        if (title != null) {

            if (content.length() > 0) {

            } else {
                content = " ";
            }
            note.setTitle(title);

            String contentbodyy = "";
            if (resourceList.size() > 0) {
                for (Resource resource : resourceList) {
                    note.addToResources(resource);
                    contentbodyy = "<en-media type=\"file/*\" hash=\"" + EvernoteUtil.bytesToHex(resource.getData().getBodyHash()) + "\"/>" + contentbodyy;

                }
            }
            // Adding resources to note.
            if (resource != null) {
                EvernoteUtil.hash(resource.getData().getBodyHash());
                nBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
                        + "<en-note>"
                        + content + contentbodyy
                        + "</en-note>";
            } else {
                nBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
                        + "<en-note>"
                        + content

                        + "</en-note>";
            }

            note.setContent(nBody);
            if (notebook != null && notebook.isSetGuid()) {
                note.setNotebookGuid(notebook.getGuid());
            }

            //The data is sent to private server.
            noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
                @Override
                public void onSuccess(Note result) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), result.getTitle() + " has been created", Toast.LENGTH_LONG).show();
                    result.setGuid(result.getGuid());
                    String url = "http://10.10.10.1/ApiEbook.php?apicall=createnote&noteName=" + title + "&noteBookName=" + notebook.getName();

                    url = url.replaceAll(" ", "%20");
                    queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(jsonObjReq);

                    onBackPressed();
                }

                @Override
                public void onException(Exception exception) {
                }
            });
        } else {

        }
    }

    public void createNotebook(String noteBookTitle) {
        Notebook notebook = new Notebook();
        notebook.setName(noteBookTitle);
        noteStoreClient.createNotebookAsync(notebook, new EvernoteCallback<Notebook>() {
            @Override
            public void onSuccess(Notebook result) {
                notebookGuid = result.getGuid();
                Log.i("notebook", "created");
            }

            @Override
            public void onException(Exception exception) {
                Log.i("notebook", "not created");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = null;
        if (requestCode == 5) {
            if (Activity.RESULT_OK == resultCode) {
                String ss = data.getExtras().get("name").toString();
                Notebook n = (Notebook) data.getExtras().get("notebook");
                notebookslist.add(n);
                notebookTitle.setText(ss);
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(photo);
                String imagepath = String.valueOf(selectedImage);
                Cursor cursor = getContentResolver().query(tempUri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String path = cursor.getString(idx);
                File finalFile = new File(path);
                attachmentCount = attachmentCount + 1;
                attachment.setText(attachmentCount + "  attachment added successfully");
                setAttributes(finalFile.getAbsolutePath(), selectedImage);
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();
                String imagepath = String.valueOf(selectedImage);
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex);
                String mimetype = getMimeType(picturePath);
                cursor.close();
                attachmentCount = attachmentCount + 1;
                attachment.setText(attachmentCount + "  attachment added successfully");
                setAttributes(picturePath, selectedImage);

            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();
                String imagepath = selectedImage.toString();
                String path = getPath11(getApplicationContext(), selectedImage);
                attachmentCount = attachmentCount + 1;
                attachment.setText(attachmentCount + "  attachment added successfully");
                setAttributes(path, selectedImage);

            }
        }

        // Receiving vedio.
        if (requestCode == 4) {
            if (data != null) {
                selectedImage = data.getData();
                String imagepath = String.valueOf(selectedImage);
                Log.i("vediopath", imagepath);
                Log.i("vediopath1", selectedImage.getPath());

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImage);
                Log.i("vediopath2", selectedImagePath);
                attachmentCount = attachmentCount + 1;
                attachment.setText(attachmentCount + "  attachment added successfully");
                setAttributes(selectedImagePath, selectedImage);
            }
        }
    }
    //This will get the the path for Storage Access Framework Documents, as well as the
    // data field for the MediaStore and other file-based ContentProviders.
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
        Log.i("filepath", filePath);
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

    // This method is used for setting Resource Attribute for note.
    public void setAttributes(String path, Uri uri) {
        InputStream in = null;
        try {
            //Hash the data in the image file. The hash is used to reference the file in the ENML note content. 
            String mimetype = getMimeType(path);
            in = new BufferedInputStream(new FileInputStream(path));

            FileData data1 = new FileData(EvernoteUtil.hash(in), new File(path));
            ResourceAttributes attributes = new ResourceAttributes();

            // Create a new Resource 
            resource = new Resource();
            resource.setData(data1);
            resource.setMime(mimetype);
            resource.setAttributes(attributes);
            resourceList.add(resource);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {

                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getFileNameByUri(Uri uri) {
        String filepath = "";
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DATA}, null, null, null);
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

    public void updatenotebookname(String name) {
        this.name = name;
        Log.i("namee", name);
        notebookTitle = (TextView) findViewById(R.id.noteBookTitle);
        notebookTitle.setText(name);
    }

    public void listNotebooks() {
        //List the note of any particular user.
        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                namesList = new ArrayList<>(result.size());
                for (Notebook notebook : result) {
                    namesList.add(notebook.getName());
                    notebookslist.add(notebook);

                    String notebookNames = TextUtils.join(", ", namesList);

                }
            }

            @Override
            public void onException(Exception exception) {
                Log.i("Errorretrievingnotbok", String.valueOf(exception));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir     /* directory*/
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public String dumpImageMetaData(Uri uri) {
        String displayName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i("tag", "Display Name: " + displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i("tag", "Size: " + size);
            }
        } finally {
            cursor.close();
        }
        return displayName;
    }


}




