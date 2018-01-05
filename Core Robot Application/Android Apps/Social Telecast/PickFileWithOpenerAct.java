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

import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.util.ArrayList;

/*
 * This class is used to take the files from the drive and share this to all group member.
 */

public class PickFileWithOpenerActivity extends MainActivity {

    private static final String TAG = "PickFileWithOpenerActivity";
    String[] stockArr;

    private static final int REQUEST_CODE_OPENER = 1;
        ArrayList<String> arrayList=new ArrayList<>();
        @Override
            public void onConnected(Bundle connectionHint) {
                super.onConnected(connectionHint);
                Intent intent=getIntent();

                if(intent!=null&&intent.getAction().equals("ParticularGroupMember")){

                    arrayList=intent.getStringArrayListExtra("groupList");

                if (arrayList != null && !arrayList.isEmpty()) {

                    stockArr = arrayList.toArray(new String[arrayList.size()]);
        }

    }
        IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
                .build(getGoogleApiClient());

                try {
                    startIntentSenderForResult(
                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    showMessage("Selected file's ID: " + driveId);
                    String url = "https://drive.google.com/open?id="+ driveId.getResourceId();

                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, stockArr);
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    startActivity(intent);

                }

                finish();

                break;
                default:
                super.onActivityResult(requestCode, resultCode, data);

        }
    }
}

