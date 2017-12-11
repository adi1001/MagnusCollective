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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

/* This class is used to describe the any particular note */

public class NoteRef implements Parcelable {

    private final String mNoteGuid;
    private final String mNotebookGuid;
    private final String mTitle;
    private int mData;

    //Create a new instance of the Parcelable class,
    // instantiating it from the given Parcel whose data had previously been written by.
    public static final Parcelable.Creator<NoteRef> CREATOR = new Parcelable.Creator<NoteRef>() {

        @Override
        public NoteRef createFromParcel(Parcel source) {
            return new NoteRef(source.readString(), source.readString(), source.readString());
        }

        @Override
        public NoteRef[] newArray(int size) {
            return new NoteRef[size];
        }
    };
    //Write the details about the notes and notes title.
    public NoteRef(@NonNull String noteGuid, @Nullable String notebookGuid, @NonNull String title) {

        mNoteGuid = noteGuid;
        mNotebookGuid = notebookGuid;
        mTitle = title;
    }

    private NoteRef(Parcel in, String mNoteGuid, String mNotebookGuid, String mTitle) {

        mData = in.readInt();
        this.mNoteGuid = mNoteGuid;
        this.mNotebookGuid = mNotebookGuid;
        this.mTitle = mTitle;
    }

    @NonNull
    public String getGuid() {
        return mNoteGuid;
    }

    public String getNotebookGuid() {
        return mNotebookGuid;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public Note loadNote(boolean withContent, boolean withResourcesData, boolean withResourcesRecognition,
                         boolean withResourcesAlternateData) throws TException, EDAMUserException, EDAMSystemException, EDAMNotFoundException {

        EvernoteNoteStoreClient noteStore = NoteRefHelper.getNoteStore(this);
        if (noteStore == null) {
            return null;
        }

        return noteStore.getNote(mNoteGuid, withContent, withResourcesData, withResourcesRecognition, withResourcesAlternateData);
    }

    public Note loadNotePartial() throws EDAMUserException, TException, EDAMSystemException, EDAMNotFoundException {
        return loadNote(false, false, false, false);
    }

    public Note loadNoteFully() throws EDAMUserException, TException, EDAMSystemException, EDAMNotFoundException {
        return loadNote(true, true, true, true);
    }

    public Notebook loadNotebook() throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
        if (mNotebookGuid == null) {
            return null;
        }

        EvernoteNoteStoreClient noteStore = NoteRefHelper.getNoteStore(this);
        if (noteStore == null) {
            return null;
        }

        return noteStore.getNotebook(mNotebookGuid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //The Parcel in which the object should be written.
    //Additional flags about how the object should be written.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNoteGuid);
        dest.writeString(mNotebookGuid);
        dest.writeString(mTitle);
    }
}

