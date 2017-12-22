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

import android.content.Intent;
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

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.rathore.evernoteapi.Adapter.NotebookListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* This class show all the list of notebooks which have created by the user.*/


public class AllNoteBooks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static Map<String, ArrayList> map = new HashMap<>();
    static ArrayList<Map<String, String>> noteandbook = new ArrayList<>();
    EvernoteNoteStoreClient noteStoreClient;
    UserStoreClient userStore;
    ArrayList<String> nameList, noteNameList, noteNameAllList;
    Map<String, String> noteMapping = new HashMap<String, String>();
    ArrayList<String> noteBookGuid = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymainbook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolBarTitle);

        mTitle.setText("All NoteBook");
        setTitle("All NoteBook");

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        final ListView listview = (ListView) findViewById(R.id.allNoteBooksLists);

        nameList = new ArrayList<>();
        noteNameAllList = new ArrayList<>();

        final Map[] map1 = {null};
        noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        ImageView addButon = (ImageView) findViewById(R.id.addButtons);
        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), NotesActivity.class);
                startActivity(intent1);
            }
        });

        //List of all notebooks which have created by user.
        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {

                for (Notebook notebook : result) {
                    nameList.add(notebook.getName());
                    noteBookGuid.add(notebook.getGuid());
                }

                progressBar.setVisibility(View.INVISIBLE);

                NotebookListViewAdapter listViewAdapter = new NotebookListViewAdapter(getApplicationContext(), nameList);
                listview.setAdapter(listViewAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), NoteBooksNotes.class);
                        intent.setAction("AllNoteBoks");
                        intent.putExtra("noteBookName", nameList.get(position));
                        intent.putExtra("noteBookGuide", noteBookGuid.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onException(Exception exception) {

            }
        });


    }

    private Map<String, ArrayList> listNotes() throws Exception {

        // List the notes in the user's account
        System.out.println("Listing notes:");

        // First, get a list of all notebooks
        List<Notebook> notebooks = noteStoreClient.listNotebooks();

        for (Notebook notebook : notebooks) {
            Log.i("Notebook: ", notebook.getName());
            nameList.add(notebook.getName());

            NoteFilter filter = new NoteFilter();
            filter.setNotebookGuid(notebook.getGuid());
            filter.setOrder(NoteSortOrder.CREATED.getValue());
            filter.setAscending(true);

            NoteList noteList = noteStoreClient.findNotes(filter, 0, 100);
            List<Note> notes = noteList.getNotes();

            noteNameList = new ArrayList<>();

            for (Note note : notes) {
                noteMapping = new HashMap<String, String>();
                String notetitle = note.getTitle();
                String notebookname = notebook.getName();
                noteMapping.put(notetitle, notebookname);
                noteNameList.add(note.getTitle());
                noteNameAllList.add(note.getTitle());
                noteandbook.add(noteMapping);

            }

            map.put(notebook.getName(), noteNameList);

        }

        return map;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.allNotes) {

            //This will open the AllNote class and show the list of AllNotes
            Intent intent = new Intent(getApplicationContext(), AllNotes.class);
            startActivity(intent);


        } else if (id == R.id.noteBook) {
            //This will open the AllNoteBooks class and show the list of AllNotebook
            Intent intent = new Intent(getApplicationContext(), AllNoteBooks.class);
            startActivity(intent);

        } else if (id == R.id.sync) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

