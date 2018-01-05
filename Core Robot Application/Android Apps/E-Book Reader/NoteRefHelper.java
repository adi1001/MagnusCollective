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

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.thrift.TException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* This class is note helper class.A reference to a note on the server. This class provides helper
 * methods to receive the note itself, its notebook and content.*/
public class NoteRefHelper {

    private static final Map<String, LinkedNotebook> LINKED_NOTEBOOK_CACHE = new HashMap<>();

    private NoteRefHelper() {
    }

    public static EvernoteNoteStoreClient getNoteStore(com.rathore.evernoteapi.NoteRef noteRef) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
        EvernoteSession session = getSession();

        return getSession().getEvernoteClientFactory().getNoteStoreClient();

    }

    //If this note is linked, then it loads the corresponding notebook for the linked notebook.
    //@return The note's notebook from server.
    public static LinkedNotebook getLinkedNotebook(String notebookGuid) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
        if (LINKED_NOTEBOOK_CACHE.containsKey(notebookGuid)) {
            return LINKED_NOTEBOOK_CACHE.get(notebookGuid);
        }

        List<LinkedNotebook> linkedNotebooks = getSession().getEvernoteClientFactory().getNoteStoreClient().listLinkedNotebooks();
        for (LinkedNotebook linkedNotebook : linkedNotebooks) {
            LINKED_NOTEBOOK_CACHE.put(linkedNotebook.getGuid(), linkedNotebook);
        }

        return LINKED_NOTEBOOK_CACHE.get(notebookGuid);
    }
    //Checks the user session login.
    public static EvernoteSession getSession() {
        EvernoteSession session = EvernoteSession.getInstance();

        if (!session.isLoggedIn()) {
            throw new IllegalArgumentException("session not logged in");
        }

        return session;
    }

}

