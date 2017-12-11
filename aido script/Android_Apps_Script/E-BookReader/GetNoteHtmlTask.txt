/*  Created on 15/08/17 by Ingen Dynamics Inc.
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

import android.content.Context;
import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteClientFactory;
import com.evernote.client.android.asyncclient.EvernoteHtmlHelper;
import com.squareup.okhttp.Response;

import java.io.IOException;

/*
 * This class gets the content of particular note in response from the evernote server.
 */
public class GetNoteHtmlTask  {
    Context context;
    NoteRef mNoteRef;

    public GetNoteHtmlTask(final Context context, NoteRef noteRef) throws IOException {
        this.context=context;
        mNoteRef = noteRef;

        // Getting  evernote client factory object.
        EvernoteClientFactory clientFactory1=EvernoteSession.getInstance().getEvernoteClientFactory();
        final EvernoteHtmlHelper htmlHelper;

        htmlHelper = clientFactory1.getHtmlHelperDefault();

        new AsyncTask<Void, Void, String>() {
            String result=null;
            @Override
            protected String doInBackground(Void... params) {
                Response response = null;
                try {
                    // Downloads the content of note.
                    response = htmlHelper.downloadNote(mNoteRef.getGuid());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if(response != null){
                    result=htmlHelper.parseBody(response);}
                    else {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s!=null) {

                    ViewHtmlActivity viewHtmlActivity = new ViewHtmlActivity();

                    context.startActivity(viewHtmlActivity.createIntent(context, mNoteRef, s));
                }
                else {
                }

            }
        }.execute();


    }

}

