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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * This class is used to store the database of all groupname and emails of each group member of all
 * the user to whom you can share.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SocialTelecast";
    //table name
    private static final String TABLE_GROUPNAME = "groupname";

    private static final String KEY_GROUP_ID = "key_group_id";
    private static final String KEY_GROUP_NAME = "key_group_name";
    private static final String KEY_GROUPMEMBER_EMAIL = "key_group_email";
    private static final String KEY_BTN_ADD = "key_btn_add";


    private final Context context;
    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public DbHelper open() {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        mDbHelper.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_GROUPNAME = "CREATE TABLE " + TABLE_GROUPNAME + "("
                + KEY_GROUP_ID + " INTEGER PRIMARY KEY ,"
                + KEY_GROUP_NAME + " TEXT NOT NULL ,"
                + KEY_GROUPMEMBER_EMAIL + " INTEGER,"
                + KEY_BTN_ADD + " TEXT NOT NULL" + ")";


        db.execSQL(CREATE_TABLE_GROUPNAME);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GROUPNAME);

    }
    //This method is used to store the groupname and all email id of individual group.
    public void addHome(String name, String groupid, String btnAdd) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, name);
        values.put(KEY_GROUPMEMBER_EMAIL, groupid);
        values.put(KEY_BTN_ADD, btnAdd);

        db.insert(TABLE_GROUPNAME, null, values);
    }

    //To delete the groupname.
    public void deleteGroupName(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GROUPNAME + " WHERE " + KEY_GROUP_ID + "='" + id + "'");
        db.close();
    }

    //To updates the groupname.
    public void updateGroup(String name, String groupid) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_GROUP_NAME, name);

        db.update(TABLE_GROUPNAME, values, KEY_GROUP_ID + " = ?", new String[]
                {String.valueOf(groupid)});
    }

    public HashMap<String, ArrayList<String>> getGroupDetails() {
        HashMap<String, ArrayList<String>> groupDetailList = new HashMap<>();

        // Query to Select All from database
        String selectQuery = "SELECT  * FROM " + TABLE_GROUPNAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String groupName = cursor.getString(cursor.getColumnIndex(KEY_GROUP_NAME));
                String email = cursor.getString(cursor.getColumnIndex(KEY_GROUPMEMBER_EMAIL));
                if (groupDetailList.containsKey(groupName)) {
                    ArrayList<String> emailList = groupDetailList.get(groupName);
                    emailList.add(email);
                } else {
                    ArrayList<String> emailList = new ArrayList<>();
                    emailList.add(email);
                    groupDetailList.put(groupName, emailList);
                }

            } while (cursor.moveToNext());
        }
        return groupDetailList;
    }


}

