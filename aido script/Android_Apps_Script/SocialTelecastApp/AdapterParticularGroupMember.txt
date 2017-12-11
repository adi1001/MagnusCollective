/*
    Created on 20/08/17 by Ingen Dynamics Inc.
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * This class is a adapter class to list the group name using recyclerview.
 */

class AdapterParticularGroupMember extends RecyclerView.Adapter<AdapterParticularGroupMember.Holder> {


    private final Context context;
    private final ArrayList<String> emailList;

    public AdapterParticularGroupMember(Context context, ArrayList<String> categoryDetailList) {
        
        Log.d("Length", "" + categoryDetailList.size());
        this.context = context;
        this.emailList = categoryDetailList;
    }


    @Override
    public AdapterParticularGroupMember.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.groupname, parent, false);
        AdapterParticularGroupMember.Holder holder = new AdapterParticularGroupMember.Holder(view);
        holder.tvEmailAdpt = (TextView) view.findViewById(R.id.createGroupNameTxt);
        holder.linearLytPgmAct = (LinearLayout) view.findViewById(R.id.createGroupLinearLyt);
        
        return holder;

    }

    @Override
    public void onBindViewHolder(AdapterParticularGroupMember.Holder holder, int position) {
        holder.tvEmailAdpt.setText(emailList.get(position));
    }

    @Override
    public int getItemCount() {
        
        return emailList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvEmailId)
        TextView tvEmailAdpt;
        @BindView(R.id.linearLyttParticularGroupMemb)
        LinearLayout linearLytPgmAct;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

