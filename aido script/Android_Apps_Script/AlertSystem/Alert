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
package com.rathore.aidoalertsystem.Models;

import java.util.ArrayList;

/*This class was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This is an alert class with getters and setters.
 */

public class Alert {

    int alertId;
    String alertName;
    String alertCategory;
    ArrayList<Sensor> sensorList = new ArrayList<>();
    public int getResolveState() {
        return resolveState;
    }
    public void setResolveState(int resolveState) {
        this.resolveState = resolveState;
    }
    int resolveState;

    public String getTriggerdDateAndTime() {
        return triggerdDateAndTime;
    }

    public void setTriggerdDateAndTime(String triggerdDateAndTime) {
        this.triggerdDateAndTime = triggerdDateAndTime;
    }

    String triggerdDateAndTime;


    public int getAlertId() {
        return alertId;
    }
    public  void addSensor(Sensor sensor){
        this.sensorList.add(sensor);
    }
    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }
    public String getAlertName() {
        return alertName;
    }
    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }
    public String getAlertCategory() {
        return alertCategory;
    }
    public void setAlertCategory(String alertCategory) {
        this.alertCategory = alertCategory;
    }
    public ArrayList<Sensor> getSensorList() {
        return sensorList;
    }
    public void setSensorList(ArrayList<Sensor> sensorList) {
        this.sensorList = sensorList;
    }
}

