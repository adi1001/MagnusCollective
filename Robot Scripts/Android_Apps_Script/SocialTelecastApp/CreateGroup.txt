/*
    Created on 25/08/17 by Ingen Dynamics Inc.
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

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is used to create the groupname inside which list of email-ids are present to
 * share the contents with groups of users.
 */

public class createGroup extends MainActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String etGroupName, etGroupMember, btnADD;
    private EditText mGroupName;
    private EditText mGroupMemberEmail;
    private Button mBtnAdd;
    private Button mBtnDone;
    private TextView mTvGroupMember;
    private TextView mTvGroupName;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        db = new DbHelper(createGroup.this);

        mGroupName = (EditText) findViewById(R.id.edGroupName);
        mGroupMemberEmail = (EditText) findViewById(R.id.edGroupMemberEmail);
        mTvGroupName = (TextView) findViewById(R.id.tvGroupName);
        mTvGroupMember = (TextView) findViewById(R.id.tvGroupMemberEmail);
        mBtnAdd = (Button) findViewById(R.id.btnAddEmail);
        mBtnDone = (Button) findViewById(R.id.btnDone);

        click();

    }

    private void click() {

        mBtnAdd.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAddEmail:
                addGroupDetail();
                break;

            case R.id.btnDone:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    //save group name and all email into the DbHelper database.
    private void addGroupDetail() {

        etGroupName = mGroupName.getText().toString().trim();
        etGroupMember = mGroupMemberEmail.getText().toString().trim();
        btnADD = mBtnAdd.getText().toString().trim();

        if (!etGroupName.equalsIgnoreCase("")) {

            if (!etGroupMember.equalsIgnoreCase("")) {

                if (!btnADD.equalsIgnoreCase("")) {

                    db.addHome(etGroupName, etGroupMember, btnADD);

                    mGroupMemberEmail.setText("");

                    Toast.makeText(this, "Insert Sucessfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(createGroup.this, "Enter some email id", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(createGroup.this, "Enter the group name", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

