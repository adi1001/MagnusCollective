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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rathore.aidoalertsystem.Adapter.RecylerviewAdapter;
import com.rathore.aidoalertsystem.Database.DatabaseHandler;
import com.rathore.aidoalertsystem.Models.Alert;
import com.rathore.aidoalertsystem.Models.Sensor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*This Activity was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This Activity is used to set a condition which triggers alerts.
 */

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    RequestQueue queue;
    ArrayList<Alert> alerts = new ArrayList<>();
    DatabaseHandler databaseHandler;
    InputMethodManager inputManager;
    Button alertCategory;
    EditText alertName;
    ArrayList categoryList;
    ArrayAdapter alertcategoryAdapter;
    ArrayList<Sensor> sensorsList = new ArrayList<>();
    Button add;
    Alert alert = new Alert();
    //This method is used to select the priority.
    public void alertCategoryOnClick(View view){
        setDialogforalertCategory();
    }
    //addButtonClick is used to add a new alert with the specific conditions.
    public void addButtonOnClick(View view){
        String constraint="";

        if(alert.getAlertName() != null && alert.getAlertCategory() != null){
            long alertId = databaseHandler.addAlert(alert,getDateTime());
            for(int i = 0;i <  alert.getSensorList().size() ;i++){
              //values of a specific conditions for a specific sensor is collected.
              if(alert.getSensorList().get(i).getSensorName() != null && alert.getSensorList().get(i).getSensorExpression() != null){
                  if(alert.getSensorList().get(i).getLogicalOperator()!=null) {
                      constraint = constraint+alert.getSensorList().get(i).getSensorName()+alert.getSensorList().get(i).getSensorExpression()+ alert.getSensorList().get(i).getSensorConstant()+" "+alert.getSensorList().get(i).getLogicalOperator()+" ";
                  }
                  else
                  {
                      constraint = constraint+alert.getSensorList().get(i).getSensorName()+alert.getSensorList().get(i).getSensorExpression()+ alert.getSensorList().get(i).getSensorConstant();
                  }
                  databaseHandler.addSensor(alert.getSensorList().get(i),alertId,i);
                  Intent intent = new Intent(getApplicationContext(),ViewAlerts.class);
                  startActivity(intent);
              }
            }

            //updates the alerts to the database.
            String url = "http://10.10.10.1/ApiAlert.php?apicall=createalert&name="+alert.getAlertName()+"&category="+alert.getAlertCategory()+"&constraintt="+constraint;
            url=url.replaceAll(" ", "%20");
            queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                   // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) ;
            queue.add(jsonObjReq);
        }
        else {
            if(alert.getAlertName() ==null){
            Toast.makeText(getApplicationContext(),"Please Enter Alert Name ",Toast.LENGTH_SHORT).show();}
            else {
                Toast.makeText(getApplicationContext(),"Please Choose Alert Category ",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add New Alert");
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
                //getActionBar().setTitle("mTitle");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }
        };

        //Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        databaseHandler = new DatabaseHandler(getApplicationContext());
        inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

        add = (Button) findViewById(R.id.add);
        //This gets the alert name.
        alertName = (EditText) findViewById(R.id.alertName);
        alertName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    inputManager.hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    String name = alertName.getText().toString();
                    alert.setAlertName(name);
                }
                return true;
            }
        });

        for(int i = 0;i < 3;i++){
            Sensor sensor = new Sensor();
            this.alert.addSensor(sensor);
        }
        //alertCategory is used to set the priority.
        alertCategory = (Button) findViewById(R.id.alertCategory);
        categoryList = new ArrayList<>();
        categoryList.add("RED");
        categoryList.add("YELLOW");
        categoryList.add("GREEN");
        alertcategoryAdapter = new ArrayAdapter(getApplicationContext(),R.layout.customlistview,categoryList);
        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecylerviewAdapter(MainActivity.this,this.alert.getSensorList());
        mRecyclerView.setAdapter(mAdapter);

    }
    //Dialogue box to set the priority.
    public void setDialogforalertCategory(){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.alertcategorylist);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);

        dialog.setTitle("Choose Alert Category");
        ListView listView =(ListView) dialog.findViewById(R.id.alertcategoryList);
      //  Button ok = (Button) dialog.findViewById(R.id.ok);
        listView.setAdapter(alertcategoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryList.get(position).toString();
                alert.setAlertCategory(category);
                alertCategory.setText(category);
                dialog.dismiss();
            }
        });
        dialog.show();
}
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();

            }
            if(position==1){
                Intent intent = new Intent(getApplicationContext(),ViewAlerts.class);
                startActivity(intent);
                finish();
            }
            if(position==2){
                mDrawerLayout.closeDrawers();
                return;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
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

