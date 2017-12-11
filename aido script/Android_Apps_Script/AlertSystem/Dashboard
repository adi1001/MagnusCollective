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
package com.rathore.aidoalertsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.rathore.aidoalertsystem.Adapter.DashboardAdapter;
import com.rathore.aidoalertsystem.Database.DatabaseHandler;
import com.rathore.aidoalertsystem.Models.Alert;
import com.rathore.aidoalertsystem.Service.BackgroundService;
import java.util.ArrayList;

/*This Activity was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This Activity is used to show triggered alerts in the dashboard where we can see the status of the
 * alerts, we can resolve the alerts and set the priority. If the alert is not resolved the
 * dashboad will keep triggering the alerts.
 */
public class Dashboard extends AppCompatActivity {

    //Initializing the required objects and variables.
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ListView listView;
    DatabaseHandler databaseHandler;
    ArrayList<Alert> alertArrayList = new ArrayList<>();
    ArrayList<String> alertDialogList;
    ArrayAdapter alertDialogListAdapter;
    AlertDialog.Builder builder;
    ArrayList categoryList;
    ArrayAdapter alertcategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Creating a toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Text view to display Dashboard
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Dashboard");
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Dashboard");
        arrayList.add("View Alerts");
        arrayList.add("Add Alerts");
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.customlistwiewfordrawer, arrayList));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            //Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
              //creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

            //Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
              //creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
        startService(intent);
    }

    //The alerts are acquired from the SQLite database.
    public void getAlertsFromDatabase(){
        alertArrayList = databaseHandler.getTriggerAlert();
    }

    public void init(){
        databaseHandler = new DatabaseHandler(getApplicationContext());
        getAlertsFromDatabase();
        categoryList = new ArrayList<>();
        categoryList.add("RED");
        categoryList.add("YELLOW");
        categoryList.add("GREEN");
        alertcategoryAdapter = new ArrayAdapter(getApplicationContext(),R.layout.customlistview,categoryList);
        alertDialogList = new ArrayList<>();
        alertDialogList.add("Resolve");
        alertDialogList.add("Change Priority");
        alertDialogListAdapter = new ArrayAdapter(Dashboard.this,R.layout.customlistview,alertDialogList);
        listView = (ListView) findViewById(R.id.dashboardListview);
        DashboardAdapter dashboardAdapter = new DashboardAdapter(Dashboard.this,alertArrayList);
        listView.setAdapter(dashboardAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dailogueBoxForSelectedAlert(alertArrayList.get(position));
            }
        });
    }

    /*The method helps us change the status of the alerts if its resolved and also change the
     *priority.
     */
    public void dailogueBoxForSelectedAlert(final Alert alert){
        final Dialog dialog=new Dialog(Dashboard.this);
        dialog.setContentView(R.layout.alertcategorylist);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.setTitle("Resolve Or Change Priority");
        ListView listView =(ListView) dialog.findViewById(R.id.alertcategoryList);
        listView.setAdapter(alertDialogListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String select = alertDialogList.get(position);
                if(select.equals("Resolve")){
                    dialog.dismiss();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Dashboard.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Dashboard.this);
                }

                builder.setTitle("Resolve")
                        .setMessage("Are you sure you want to Resolve this alert?")
                        .setPositiveButton("Resolved", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //update database
                                databaseHandler.updateAlertResolveStatus(alert);
                                init();

                            }
                        })
                        .setNegativeButton("Not Resolved", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                databaseHandler.updateAlertResolveStatusTonotResolved(alert);
                                init();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }
                else {
                    dialog.dismiss();
                    setDialogforalertCategory(alert);
                }


            }
        });
        dialog.show();
    }
    //This method is used to change the priority and is called in the previous method.
    public void setDialogforalertCategory(final Alert alert){
        final Dialog dialog=new Dialog(Dashboard.this);
        dialog.setContentView(R.layout.alertcategorylist);
        dialog.setTitle("Choose Alert Category");
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        ListView listView =(ListView) dialog.findViewById(R.id.alertcategoryList);
        listView.setAdapter(alertcategoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryList.get(position).toString();
                //update database
                databaseHandler.updateAlertCategory(alert,category);
                init();
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                mDrawerLayout.closeDrawers();
                return;
            }
            if(position==1){
                //opens viewalerts activity
                Intent intent = new Intent(getApplicationContext(),ViewAlerts.class);
                startActivity(intent);
                finish();
            }
            if(position==2){
               // opens Mainactivity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Pass the event to ActionBarDrawerToggle
         * If it returns true, then it has handled
         * the nav drawer indicator touch event
         */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}


