/*Created on 15/02/17 by Ingen Dynamics Inc. This program is free software:
 *you can redistribute it and/or modify it under the terms of the GNU General
 *Public License as published by the Free Software Foundation,
 *either version 3 of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rathore.aidoalertsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.rathore.aidoalertsystem.Models.Alert;
import com.rathore.aidoalertsystem.Models.Sensor;
import com.rathore.aidoalertsystem.R;
import java.util.ArrayList;

/*This adaptor was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This adaptor is used for custom layout for Dashboard activity.
 */

public class DashboardAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<Alert> list = new ArrayList<>();
    Context context;

    public DashboardAdapter(Context context, ArrayList<Alert> alerList) {
        list = alerList;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alert alert = list.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.dashboardtablelayout,null);
        }
        TextView alertname = (TextView) convertView.findViewById(R.id.alertName);
        TextView alertcategory = (TextView) convertView.findViewById(R.id.alertCategory);
        TextView alertTriggerDate = (TextView) convertView.findViewById(R.id.triggeredDate);
        TextView alertText = (TextView) convertView.findViewById(R.id.alertText);
        TextView alertResolveStatus = (TextView) convertView.findViewById(R.id.resolveStatus);
        alertname.setText(alert.getAlertName());
        alertcategory.setText(alert.getAlertCategory());
        alertTriggerDate.setText(alert.getTriggerdDateAndTime());
        alertname.setTextSize(23);
        alertcategory.setTextSize(23);
        alertTriggerDate.setTextSize(23);
        alertText.setTextSize(23);
        alertResolveStatus.setTextSize(23);


        if(alert.getResolveState()==0){
            alertResolveStatus.setText("Not Resolved");}
        else {
            alertResolveStatus.setText("Resolved");
        }

        String constraint="";
        // Getting sensor values and displaying.
        for(int i=0;i<alert.getSensorList().size();i++){
            Sensor sensor = alert.getSensorList().get(i);
            String sensorname = sensor.getSensorName();
            String sensorExpression1 = sensor.getSensorExpression();
            String sensorconstant = String.valueOf(sensor.getSensorConstant());
            String logicalOperator = sensor.getLogicalOperator();
            if(sensor.getLogicalOperator()!=null&&i!=alert.getSensorList().size()-1){
                constraint = constraint+sensorname+sensorExpression1+sensorconstant+" "+logicalOperator+" ";
            }
            else {
                constraint = constraint+sensorname+sensorExpression1+sensorconstant;
            }
        }
       alertText.setText(constraint);

        return convertView;
    }

}

