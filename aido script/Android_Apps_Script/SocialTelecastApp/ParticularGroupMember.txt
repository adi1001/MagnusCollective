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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

 /*
  * This class is used to shows the list of group of email.
  * Choose the file to send in to google drive and also share this to all group member using email.
  */

public class ParticularGroupMember extends MainActivity {

    public static final String EMAIL_LIST_KEY = "email_list";
    private TextView mTvList;
    private RecyclerView mRecyclerPGM;
    private Button btnPGMA;
    private FloatingActionButton btnFloatPGMA;
    private EditText editTextPGM;
    private View view;

    AppCompatImageView imgView;


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);

        Button btnSend = (Button) findViewById(R.id.btnParticularGroup);

        final ArrayList<String> emailList = getEmailListFromIntent();
        if (emailList != null && !emailList.isEmpty()) {
            String[] stockArr = emailList.toArray(new String[emailList.size()]);

        }

        //choose the file from drive and share with the other user through email.
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PickFileWithOpenerActivity.class);
                intent.putStringArrayListExtra("groupList", emailList);
                intent.setAction("ParticularGroupMember");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_group_member);

        mTvList = (TextView) findViewById(R.id.tvEmailList);
        mRecyclerPGM = (RecyclerView) findViewById(R.id.recyclerParticularGroup);
        btnPGMA = (Button) findViewById(R.id.btnParticularGroup);
        final ArrayList<String> emailList = getEmailListFromIntent();
        if (emailList != null && !emailList.isEmpty()) {
            setGroupAdapter(emailList);
        }

    }

    private ArrayList<String> getEmailListFromIntent() {
        if (getIntent() != null && getIntent().hasExtra(EMAIL_LIST_KEY)) {
            return getIntent().getStringArrayListExtra(EMAIL_LIST_KEY);
        }
        return new ArrayList<>();
    }

    //set the adapter in the recyclerview to list out the group email id.
    private void setGroupAdapter(ArrayList<String> emailList) {
        AdapterParticularGroupMember adapter = new AdapterParticularGroupMember(this, emailList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerPGM.setLayoutManager(mLayoutManager);
        mRecyclerPGM.setItemAnimator(new DefaultItemAnimator());
        mRecyclerPGM.setAdapter(adapter);
    }

}

