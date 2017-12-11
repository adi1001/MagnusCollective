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

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rathore.socialtelecast.ParticularGroupMember.EMAIL_LIST_KEY;

/*
 * This class is used to display list of groupname.
 */

public class createGroupAdapter extends RecyclerView.Adapter<createGroupAdapter.Holder> {

    private final Context context;
    private final FragmentManager fm;
    public HashMap<String, ArrayList<String>> DetailList = new HashMap<>();
    private HashMap<String, String> map;
    private FragmentTransaction transaction;


    public createGroupAdapter(Context context, HashMap<String, ArrayList<String>> categoryDetailList, FragmentManager fm) {

        this.context = context;
        this.DetailList = categoryDetailList;
        this.fm = fm;
    }

    @Override
    public createGroupAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.groupname, parent, false);
        createGroupAdapter.Holder holder = new createGroupAdapter.Holder(view);
        holder.mCreateGroupTxt = (TextView) view.findViewById(R.id.createGroupNameTxt);
        holder.mCreateGroupLyt = (LinearLayout) view.findViewById(R.id.createGroupLinearLyt);
        return holder;
    }

    @Override
    public void onBindViewHolder(final createGroupAdapter.Holder holder, final int position) {

        holder.mCreateGroupTxt.setText((String) DetailList.keySet().toArray()[position]);
        holder.mCreateGroupLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startParticularGroupMemberActivity(DetailList.get(DetailList.keySet().toArray()[position]));
            }
        });

    }

    @Override
    public int getItemCount() {
        return DetailList.size();
    }

    private void startParticularGroupMemberActivity(ArrayList<String> emailList) {
        Intent memberListIntent = new Intent(context, ParticularGroupMember.class);
        memberListIntent.putStringArrayListExtra(EMAIL_LIST_KEY, emailList);
        context.startActivity(memberListIntent);
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.createGroupNameTxt)
        TextView mCreateGroupTxt;
        @BindView(R.id.createGroupLinearLyt)
        LinearLayout mCreateGroupLyt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

