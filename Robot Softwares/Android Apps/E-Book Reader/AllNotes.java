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

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.User;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;
import com.rathore.evernoteapi.Adapter.ListViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* This will show all notes and using evernote api to send the notes to evernotes server.*/

public class AllNotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static Map<String, ArrayList> map = new HashMap<>();
    static ArrayList<Map<String, String>> noteandbook = new ArrayList<>();
    EvernoteNoteStoreClient noteStoreClient;
    List<NoteRef> mNoteRefList;
    UserStoreClient userStore;
    ArrayList<String> namesList, notenamelist, noteNameAllList;
    Map<String, String> notemapping = new HashMap<String, String>();
    Bitmap bitmap;
    ArrayList<String> notNameList = new ArrayList<>();
    String query, query1;
    User user;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks internet connection
        boolean connectionstatus = isOnline();
        if (connectionstatus == true) {

            if (!EvernoteSession.getInstance().isLoggedIn()) {

                //This will check user Login have completed.
                Toast.makeText(getApplicationContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar = (ProgressBar) findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);

            // Setting the toolbar and title.
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolBarTitle);
            mTitle.setText("All Notes");
            setTitle("AllNotes");

            // Setting up drawer layout
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
            navigationView.setNavigationItemSelectedListener(this);

           // Set the drawer toggle as the DrawerListener
            drawer.setDrawerListener(mDrawerToggle);

            // Set the list's click listener
            mNoteRefList = new ArrayList<>();

           // To search the list of notebooks using search manager.
            Intent intent = getIntent();
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                query = intent.getStringExtra(SearchManager.QUERY);
                //use the query to search your data somehow
                query1 = "intitle:" + query;
            }

            final ListView listview = (ListView) findViewById(R.id.allNotesList);

            namesList = new ArrayList<>();
            noteNameAllList = new ArrayList<>();

            ImageView addButon = (ImageView) findViewById(R.id.addButton);

            //This will go to NotesActivity where you can create new note.
            addButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getApplicationContext(), NotesActivity.class);
                    startActivity(intent1);
                }
            });

            //This gets the user information from evernote server.
            new AsyncTask<Void, Void, String>() {
                User user = new User();

                @Override
                protected String doInBackground(Void... params) {

                    EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, "s1:U=93da6:E=164df80b61b:C=15d87cf8718:P=1cd:A=en-devtoken:V=2:H=02a8e90d1346762e018dac28599bd00a");
                    ClientFactory factory = new ClientFactory(evernoteAuth);
                    try {
                        userStore = factory.createUserStoreClient();
                        user = userStore.getUser();
                        Log.i("email", user.getUsername());
                    } catch (TTransportException e) {
                        e.printStackTrace();
                    } catch (TException e) {
                        e.printStackTrace();
                    } catch (EDAMUserException e) {
                        e.printStackTrace();
                    } catch (EDAMSystemException e) {
                        e.printStackTrace();
                    }

                    boolean versionOk = false;
                    try {
                        versionOk = userStore.checkVersion("Evernote EDAMDemo (Java)",
                                com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
                                com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR);
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                    if (!versionOk) {
                        System.err.println("Incompatible Evernote client protocol version");
                        System.exit(1);
                    }

                    return "asd";
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                }
            }.execute();

            final Map[] map1 = {null};

            //This generate the note and store as client.
            noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

            final ArrayList[] notelist1 = {null};

            new AsyncTask<Void, Void, ArrayList>() {
                @Override
                protected ArrayList doInBackground(Void... params) {
                    try {

            //This gets the all notes from evernote server.
                    notelist1[0] = listNotes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return notelist1[0];
                }

                @Override
                protected void onPostExecute(ArrayList list) {
                    super.onPostExecute(list);
                    progressBar.setVisibility(View.INVISIBLE);
                    if (list != null) {
                        if (list.size() > 0) {

                            ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext(), list);
                            listview.setAdapter(listViewAdapter);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        //Shows content of perticular note.
                                        new GetNoteHtmlTask(getApplicationContext(), mNoteRefList.get(position));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }

            }.execute();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
        }
    }

    // This will put note on arraylist and arrange in ascending order.
    private ArrayList listNotes() throws Exception {

        NoteFilter filter = new NoteFilter();

        filter.setOrder(NoteSortOrder.CREATED.getValue());
        filter.setAscending(true);

        NoteList noteList = noteStoreClient.findNotes(filter, 0, 100);
        List<Note> notes = noteList.getNotes();
        for (Note note : notes) {

            NoteRef noteRef = new NoteRef(note.getGuid(), note.getNotebookGuid(), note.getTitle());
            mNoteRefList.add(noteRef);
            notNameList.add(note.getTitle());

        }

        return notNameList;
    }

    @Override
    public void onBackPressed() {
        // Drawer will close after clicking the back button.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        finish();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.allNotes) {

            //This will open the AllNote class and show the list of AllNotes
            Intent intent = new Intent(getApplicationContext(), AllNotes.class);
            startActivity(intent);

        } else if (id == R.id.noteBook) {

            //This will open the AllNoteBooks class and show the list of AllNotebooks
            Intent intent = new Intent(getApplicationContext(), AllNoteBooks.class);
            startActivity(intent);

        } else if (id == R.id.sync) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //This establish the connection and check the connection is made.
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

}

