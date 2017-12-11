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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Resource;
import com.rathore.evernoteapi.Adapter.ListViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* This class is list the notes details of particular notebook */
public class NoteBooksNotes extends AppCompatActivity {
    EvernoteNoteStoreClient noteStoreClient;
    NoteRef noteRef;
    String noteBookGuide;
    String noteBookName;
    List<NoteRef> noteRefList;
    ArrayList<String> noteList = new ArrayList<>();

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitynotebooksnotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolBarTitle);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        noteRefList = new ArrayList<>();

        //User session login check.
        noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        final ListView listView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();
        if (intent != null) {

            //List out the all notes in the arraylist.
            if (intent.getAction().equals("AllNoteBoks")) {
                noteBookGuide = intent.getStringExtra("noteBookGuide");
                noteBookName = intent.getStringExtra("noteBookName");
                mTitle.setText(noteBookName);

                final ArrayList[] notelist1 = {null};
                new AsyncTask<Void, Void, ArrayList>() {
                    @Override
                    protected ArrayList doInBackground(Void... params) {
                        try {

                            notelist1[0] = listNotes();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return notelist1[0];
                    }

                    @Override
                    protected void onPostExecute(ArrayList list) {
                        super.onPostExecute(list);
                        //Progress bar is visible untill the all note gets loads.
                        progressBar.setVisibility(View.INVISIBLE);
                        if (list.size() > 0) {
                            //Adapter is set here and display in listview.
                            ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext(), list);
                            listView.setAdapter(listViewAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        new GetNoteHtmlTask(getApplicationContext(), noteRefList.get(position));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                }.execute();

            } else {
            }
        } else {

        }

    }

    private ArrayList listNotes() throws Exception {
        //This is used to search the particular notes in a list of notebooks.
        NoteFilter filter = new NoteFilter();
        filter.setOrder(NoteSortOrder.CREATED.getValue());
        filter.setNotebookGuid(noteBookGuide);
        filter.setAscending(true);
        NoteList noteList = noteStoreClient.findNotes(filter, 0, 100);
        List<Note> notes = noteList.getNotes();
        for (Note note : notes) {
            NoteRef noteRef = new NoteRef(note.getGuid(), note.getNotebookGuid(), note.getTitle());
            noteRefList.add(noteRef);
            this.noteList.add(note.getTitle());

            if (note.getContent() != null) {
            }
            if (note.getResourcesSize() > 0) {
                List<Resource> resources = note.getResources();
                for (Resource noteResource : resources) {

                }

            }
        }
        return this.noteList;
    }
}


