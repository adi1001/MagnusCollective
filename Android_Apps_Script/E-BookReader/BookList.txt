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

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

import java.util.ArrayList;

/* This class is used to create new notebook by user and created note book is add up in the list */

public class BookList extends ListActivity {


    ArrayList<String> listArr;
    EvernoteNoteStoreClient noteStoreClient;
    Button mBtnOK, cancel;
    String noteTitle;
    Notebook notebooks = new Notebook();
    String name, guid, mEtText;

    NotesActivity notes = new NotesActivity();

    // dialog to create new notebook
    public void addnewbook(View view) {

        final Dialog dialog = new Dialog(BookList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.newnotebook);
        dialog.setTitle("Create New NoteBook");
        mBtnOK = (Button) dialog.findViewById(R.id.btnOK);
        final EditText editText = (EditText) dialog.findViewById(R.id.newNoteBook);
        mEtText = editText.getText().toString();
        cancel = (Button) dialog.findViewById(R.id.cancel);

        noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        mBtnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (editText.getText().toString() != null) {
                    final Notebook notebook = new Notebook();

                    notebook.setName(editText.getText().toString());
                    notebook.setDefaultNotebook(false);

                    new AsyncTask<Void, Void, Notebook>() {
                        @Override
                        protected Notebook doInBackground(Void... params) {
                            Notebook notebook1 = null;
                            try {
                                notebook1 = noteStoreClient.createNotebook(notebook);
                            } catch (EDAMUserException e) {
                                e.printStackTrace();
                            } catch (EDAMSystemException e) {
                                e.printStackTrace();
                            } catch (TException e) {
                                e.printStackTrace();
                            }
                            return notebook1;
                        }

                        @Override
                        protected void onPostExecute(Notebook notebook) {
                            super.onPostExecute(notebook);
                            if (notebook != null) {

                                if (listArr != null) {
                                    listArr.add(notebook.getName());
                                    notebooks = notebook;
                                    setListAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinneritem, listArr));
                                }

                            } else {

                            }
                            dialog.dismiss();
                        }

                    }.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter notebook name", Toast.LENGTH_LONG).show();
                }
            }


            // if(mEtText!=null) {

//           Intent intent1 = new Intent(getApplicationContext(), NotesActivity.class);
//           intent1.putExtra("notebook", mEtText);
//           intent1.setAction("BookList");
//           intent1.putExtra("noteBookGuide", guid);
//           startActivity(intent1);
//
//       }
//                else {
//           Toast.makeText(getApplicationContext(),"please enter name",Toast.LENGTH_SHORT).show();
//       }
            //  }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklistitem);
        listArr = new ArrayList<String>();
        final Intent intent = getIntent();

        listArr = intent.getStringArrayListExtra("namelist");
        noteTitle = intent.getStringExtra("noteName");

        Button addnotebook = (Button) findViewById(R.id.addNoteBook);

        setListAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinneritem, listArr));

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    //Get the data from listview when user click on a list item, the item is selected using an
    // onListItemClick listener.
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        String selected = listArr.get(position);
        Intent intent = new Intent();
        intent.putExtra("name", selected);
        intent.putExtra("notebook", notebooks);
        setResult(Activity.RESULT_OK, intent);

        finish();

    }
}

