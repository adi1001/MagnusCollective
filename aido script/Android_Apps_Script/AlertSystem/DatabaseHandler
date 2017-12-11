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
package com.rathore.aidoalertsystem.Database;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import com.rathore.aidoalertsystem.Models.Alert;
import com.rathore.aidoalertsystem.Models.Sensor;
import com.rathore.aidoalertsystem.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/*This class was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 *This database helper class is used save and update alerts in database.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

  String prev = "";
  static int notification = 0;

  private static final int DATABASE_VERSION = 1;

  // Database Name
  public static final String DATABASE_NAME = "AlertSystemDB";

  // Alert table name
  public static final String TABLE_ALERT = "Alert_Table";

  // Sensor table name
  private static final String TABLE_SENSOR = "Alert";

  // Alert  Table Columns names
  private static final String KEY_ID = "id";
  private static final String KEY_NAME = "AlertName";
  private static final String KEY_CATEGORY = "AlertCategory";
  private static final String KEY_RESOLVE_STATUS = "AlertResolveStatus";
  private static final String KEY_TRIGGRED_STATUS = "AlertTriggredStatus";
  private static final String KEY_DATE_AND_TIME_OF_TRIGGER = "AlertTriggredDateAndTime";
  private static final String KEY_TIME_OF_TRIGGER = "AlertTriggredTime";
  private static final String KEY_CREATED_TIME_AND_DATE = "AlertCreatedTimeAndDate";

  // Sensor Table Columns names
  private static final String KEY_SENSOR_ID = "id";
  private static final String KEY_ALERT_ID = "alert_id";
  private static final String KEY_SENSOR_NAME = "Sensor_Name";
  private static final String KEY_EXPRESSION = "Sensor_Expression";
  private static final String KEY_CONSTANT = "Sensor_Constant";
  private static final String KEY_RELATION_TO = "Sensor_Relation_to";

  NotificationManager mNotificationManager;

  Context context;


  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;

  }

  //Creates a alert and sensor table in the database.
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_ALERT_TABLE = "CREATE TABLE " + TABLE_ALERT + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_RESOLVE_STATUS + " INTEGER,"
        + KEY_TRIGGRED_STATUS + " INTEGER," + KEY_CREATED_TIME_AND_DATE + " TEXT,"
        + KEY_DATE_AND_TIME_OF_TRIGGER + " TEXT," + KEY_CATEGORY + " TEXT" + ")";
    db.execSQL(CREATE_ALERT_TABLE);

    String CREATE_SENSOR_TABLE = "CREATE TABLE " + TABLE_SENSOR + "("
        + KEY_SENSOR_ID + " INTEGER PRIMARY KEY," + KEY_ALERT_ID + " INTEGER ," + KEY_SENSOR_NAME
        + " TEXT,"
        + KEY_EXPRESSION + " TEXT," + KEY_RELATION_TO + " TEXT," + KEY_CONSTANT + " INTEGER " + ")";
    db.execSQL(CREATE_SENSOR_TABLE);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
    //onCreate(db);

  }

  /**
   * All CRUD(Create, Read, Update, Delete) Operations
   */

  // Adding new Alert
  public long addAlert(Alert alert, String dateAndTime) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, alert.getAlertName());
    values.put(KEY_CATEGORY, alert.getAlertCategory());
    values.put(KEY_TRIGGRED_STATUS, 0);
    values.put(KEY_RESOLVE_STATUS, 0);
    values.put(KEY_CREATED_TIME_AND_DATE, dateAndTime);

    // Inserting Row
    long newRowId = db.insert(TABLE_ALERT, null, values);
    db.close(); // Closing database connection
    return newRowId;
  }

  // Adding new sensor
  public void addSensor(Sensor sensor, long alertid, int i) {

    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_SENSOR_NAME, sensor.getSensorName());
    values.put(KEY_EXPRESSION, sensor.getSensorExpression());
    values.put(KEY_CONSTANT, sensor.getSensorConstant());
    values.put(KEY_ALERT_ID, alertid);
    values.put(KEY_RELATION_TO, sensor.getLogicalOperator());
    long newRowId = db.insert(TABLE_SENSOR, null, values);
    // Closing database connection
    db.close();
  }

  // Getting All Alerts
  public ArrayList<Alert> getAllAlert() {
    ArrayList<Alert> alertList = new ArrayList<Alert>();
    ArrayList<Sensor> sensorList = new ArrayList<>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_ALERT + " INNER JOIN " + TABLE_SENSOR
        + " ON Alert_Table.id = Alert.alert_id";

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    int alertIdIndex = cursor.getColumnIndex(KEY_ID);
    int alertNameIndex = cursor.getColumnIndex(KEY_NAME);
    int alertCategoryIndex = cursor.getColumnIndex(KEY_CATEGORY);
    int sensorNameIndex = cursor.getColumnIndex(KEY_SENSOR_NAME);
    int sensorConstantIndex = cursor.getColumnIndex(KEY_CONSTANT);
    int sensorExpressionIndex = cursor.getColumnIndex(KEY_EXPRESSION);
    int sensoralertidIndex = cursor.getColumnIndex(KEY_ALERT_ID);
    int sensorIdIndex = cursor.getColumnIndex(KEY_SENSOR_ID);
    int alertResolveIndex = cursor.getColumnIndex(KEY_RESOLVE_STATUS);
    int logical_operator_index = cursor.getColumnIndex(KEY_RELATION_TO);

    if (cursor.getCount() > 0 && cursor != null) {

      // looping through all rows and adding to list

      //alert Details
      int previousAlertId = -1;
      ArrayList<Sensor> sensors = new ArrayList<Sensor>();
      ;
      ArrayList<Alert> alertsList = new ArrayList<Alert>();
      Alert alert = new Alert();
      while (cursor.moveToNext()) {
        int alertid = cursor.getInt(alertIdIndex);
        String alertName = cursor.getString(alertNameIndex);
        String alertCategory = cursor.getString(alertCategoryIndex);
        int resolveStatus = cursor.getInt(alertResolveIndex);

        //sensor details
        int sensorId = cursor.getInt(sensorIdIndex);
        String sensorName = cursor.getString(sensorNameIndex);
        String sesnorExperossion = cursor.getString(sensorExpressionIndex);
        String sensorLogicalOperator = cursor.getString(logical_operator_index);

        int constant = cursor.getInt(sensorConstantIndex);
        int sensorAlertId = cursor.getInt(sensoralertidIndex);

        if (previousAlertId == -1 || previousAlertId == sensorAlertId) {
          previousAlertId = sensorAlertId;

          if (!alertList.contains(alert)) {

            alert.setAlertCategory(alertCategory);
            alert.setAlertName(alertName);
            alert.setAlertId(sensorAlertId);
            alert.setResolveState(resolveStatus);

            alertList.add(alert);
          }


        } else {

          previousAlertId = sensorAlertId;

          alert = new Alert();
          alert.setAlertCategory(alertCategory);
          alert.setAlertName(alertName);
          alert.setAlertId(sensorAlertId);
          alert.setResolveState(resolveStatus);

          alertList.add(alert);
        }

        Sensor sensor = new Sensor();
        sensor.setSensorConstant(constant);
        sensor.setSensorExpression(sesnorExperossion);
        sensor.setSensorName(sensorName);
        sensor.setLogicalOperator(sensorLogicalOperator);

        alert.addSensor(sensor);


      }

      for (Alert alert1 : alertList) {

        ArrayList<Sensor> sensorList1 = alert1.getSensorList();

        for (Sensor sensor1 : sensorList1) {
        }
      }


    } else {
    }
    return alertList;
  }

  // Updating single Alert
  public int updateAlert(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    String triggredDate = getDateTime();

    ContentValues values = new ContentValues();
    values.put(KEY_TRIGGRED_STATUS, 1);
    values.put(KEY_DATE_AND_TIME_OF_TRIGGER, triggredDate);

    // updating row
    int rowno = db.update(TABLE_ALERT, values, KEY_ID + " = " + id, null);
    return rowno;
  }

  // Updating single Alert
  public int updateAlert1(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    String triggredDate = getDateTime();

    ContentValues values = new ContentValues();
    values.put(KEY_TRIGGRED_STATUS, 0);

    // updating row
    int rowno = db.update(TABLE_ALERT, values, KEY_ID + " = " + id, null);
    return rowno;
  }

  public int updateAlertResolveStatus(Alert alert) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_RESOLVE_STATUS, 1);

    // updating row
    int rowno = db.update(TABLE_ALERT, values, KEY_ID + " = " + alert.getAlertId(), null);

    return rowno;
  }

  public int updateAlertResolveStatusTonotResolved(Alert alert) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_RESOLVE_STATUS, 0);

    // updating row
    int rowno = db.update(TABLE_ALERT, values, KEY_ID + " = " + alert.getAlertId(), null);
    return rowno;
  }

  public int updateAlertCategory(Alert alert, String category) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_CATEGORY, category);

    // updating row
    int rowno = db.update(TABLE_ALERT, values, KEY_ID + " = " + alert.getAlertId(), null);
    alert.setAlertCategory(category);
    return rowno;
  }

  private String getDateTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public ArrayList<Alert> getTriggerAlert() {
    ArrayList<Alert> alertList = new ArrayList<Alert>();
    ArrayList<Sensor> sensorList = new ArrayList<>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_ALERT + "  INNER JOIN " + TABLE_SENSOR
        + " ON Alert_Table.id = Alert.alert_id WHERE Alert_Table.AlertTriggredStatus == 1 ";

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    int alertIdIndex = cursor.getColumnIndex(KEY_ID);
    int alertNameIndex = cursor.getColumnIndex(KEY_NAME);
    int alertCategoryIndex = cursor.getColumnIndex(KEY_CATEGORY);
    int alertDateAndTime = cursor.getColumnIndex(KEY_DATE_AND_TIME_OF_TRIGGER);
    int alertResolveIndex = cursor.getColumnIndex(KEY_RESOLVE_STATUS);
    int alertTRiggeredStatus = cursor.getColumnIndex(KEY_TRIGGRED_STATUS);
    int sensorNameIndex = cursor.getColumnIndex(KEY_SENSOR_NAME);
    int sensorConstantIndex = cursor.getColumnIndex(KEY_CONSTANT);
    int sensorExpressionIndex = cursor.getColumnIndex(KEY_EXPRESSION);
    int sensoralertidIndex = cursor.getColumnIndex(KEY_ALERT_ID);
    int sensorIdIndex = cursor.getColumnIndex(KEY_SENSOR_ID);
    int logical_operator_index = cursor.getColumnIndex(KEY_RELATION_TO);
    // looping through all rows and adding to list

    //alert Details
    int previousAlertId = -1;
    ArrayList<Sensor> sensors = new ArrayList<Sensor>();
    ;
    ArrayList<Alert> alertsList = new ArrayList<Alert>();
    Alert alert = new Alert();
    while (cursor.moveToNext()) {
      int alertid = cursor.getInt(alertIdIndex);
      String alertName = cursor.getString(alertNameIndex);
      String alertCategory = cursor.getString(alertCategoryIndex);
      String alertdateTime = cursor.getString(alertDateAndTime);
      int resolveStatus = cursor.getInt(alertResolveIndex);

      //sensor details
      int sensorId = cursor.getInt(sensorIdIndex);
      String sensorName = cursor.getString(sensorNameIndex);
      String sesnorExperossion = cursor.getString(sensorExpressionIndex);
      int constant = cursor.getInt(sensorConstantIndex);
      int sensorAlertId = cursor.getInt(sensoralertidIndex);
      String sensorLogicalOperator = cursor.getString(logical_operator_index);

      if (previousAlertId == -1 || previousAlertId == sensorAlertId) {
        previousAlertId = sensorAlertId;

        if (!alertList.contains(alert)) {

          alert.setAlertCategory(alertCategory);
          alert.setAlertName(alertName);
          alert.setAlertId(sensorAlertId);
          alert.setTriggerdDateAndTime(alertdateTime);
          alert.setResolveState(resolveStatus);

          alertList.add(alert);
        }


      } else {

        previousAlertId = sensorAlertId;

        alert = new Alert();
        alert.setAlertCategory(alertCategory);
        alert.setAlertName(alertName);
        alert.setAlertId(sensorAlertId);
        alert.setTriggerdDateAndTime(alertdateTime);
        alert.setResolveState(resolveStatus);

        alertList.add(alert);
      }

      Sensor sensor = new Sensor();
      sensor.setSensorConstant(constant);
      sensor.setSensorExpression(sesnorExperossion);
      sensor.setSensorName(sensorName);
      sensor.setLogicalOperator(sensorLogicalOperator);

      alert.addSensor(sensor);

    }

    for (Alert alert1 : alertList) {

      ArrayList<Sensor> sensorList1 = alert1.getSensorList();

      for (Sensor sensor1 : sensorList1) {
      }
    }

    return alertList;
  }

  public void checkCondtion(int gasconstant, int humidityconstant, int pressureconstant,
      int tempconstant) throws ScriptException {
    ArrayList<ArrayList> listOfList = new ArrayList<>();
    ArrayList list = new ArrayList<>();
    String selectQuery = "SELECT  * FROM " + TABLE_SENSOR;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    int sensorNameIndex = cursor.getColumnIndex(KEY_SENSOR_NAME);
    int sensorConstantIndex = cursor.getColumnIndex(KEY_CONSTANT);
    int sensorExpressionIndex = cursor.getColumnIndex(KEY_EXPRESSION);
    int sensoralertidIndex = cursor.getColumnIndex(KEY_ALERT_ID);
    int sensorIdIndex = cursor.getColumnIndex(KEY_SENSOR_ID);
    int logical_operator_index = cursor.getColumnIndex(KEY_RELATION_TO);
    int previousAlertId = -1;
    String previousCategory = "";

    String category = "";
    int resolvestatuss;
    int sensorAlertId = 0;
    while (cursor.moveToNext()) {
      String condition = "";

      boolean booleanValue = false;
      //sensor details
      int sensorId = cursor.getInt(sensorIdIndex);
      String sensorName = cursor.getString(sensorNameIndex);
      String sesnorExperossion = cursor.getString(sensorExpressionIndex);
      int constant = cursor.getInt(sensorConstantIndex);
      sensorAlertId = cursor.getInt(sensoralertidIndex);

      category = getAlertCategory(sensorAlertId);
      String sensorLogicalOperator = cursor.getString(logical_operator_index);
      if (previousAlertId == -1 || previousAlertId == sensorAlertId) {
        previousAlertId = sensorAlertId;
        previousCategory = category;

        if (sensorName.equals("Temperature")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, tempconstant);
        }
        if (sensorName.equals("Gas")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, gasconstant);

        }
        if (sensorName.equals("Humidity")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, humidityconstant);
        }
        if (sensorName.equals("Pressure")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, pressureconstant);
        }

        list.add(booleanValue);
        if (sensorLogicalOperator != null) {

          if (sensorLogicalOperator.equals("AND")) {

            list.add("&&");
          }
          if (sensorLogicalOperator.equals("OR")) {
            list.add("||");
          }


        }

      } else {
        if (list.get(list.size() - 1).equals("&&") || list.get(list.size() - 1).equals("||")) {
          list.remove(list.size() - 1);
        }
        listOfList.add(list);
        resolvestatuss = getAlertResolvestatus(previousAlertId);
        triggredState(previousCategory, list, previousAlertId, resolvestatuss);
        list = new ArrayList();

        previousAlertId = sensorAlertId;
        previousCategory = category;
        if (sensorName.equals("Temperature")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, tempconstant);
        }
        if (sensorName.equals("Gas")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, gasconstant);
        }
        if (sensorName.equals("Humidity")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, humidityconstant);
        }
        if (sensorName.equals("Pressure")) {
          booleanValue = conditionCheck(constant, sesnorExperossion, pressureconstant);
        }

        list.add(booleanValue);
        if (sensorLogicalOperator != null) {
          if (sensorLogicalOperator.equals("AND")) {

            list.add("&&");
          }
          if (sensorLogicalOperator.equals("OR")) {
            list.add("||");
          }
        }


      }
      if (cursor.isLast()) {
        if (list.get(list.size() - 1).equals("&&") || list.get(list.size() - 1).equals("||")) {
          list.remove(list.size() - 1);

        }
        listOfList.add(list);
        resolvestatuss = getAlertResolvestatus(previousAlertId);
        triggredState(category, list, previousAlertId, resolvestatuss);
      }


    }


  }


  public boolean conditionCheck(int constant, String expression, int firebasevalue) {

    boolean value = false;
    if (expression.equals(">=")) {
      value = firebasevalue >= constant;

    } else if (expression.equals("<=")) {
      value = firebasevalue <= constant;
    } else if (expression.equals("==")) {

      value = firebasevalue == constant;
    } else if (expression.equals("<")) {
      value = firebasevalue < constant;
    } else if (expression.equals(">")) {
      value = firebasevalue > constant;
    }

    return value;

  }

  //get Alert Category
  public String getAlertCategory(int alertId) {
    String category = null;
    String selectQuery = "SELECT * FROM " + TABLE_ALERT + " WHERE Alert_Table.id = " + alertId;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    int alertCategoryIndex = cursor.getColumnIndex(KEY_CATEGORY);

    category = cursor.getString(alertCategoryIndex);

    return category;
  }

  // Used to get alert resolved status.
  public int getAlertResolvestatus(int alertId) {
    int category;
    String selectQuery = "SELECT * FROM " + TABLE_ALERT + " WHERE Alert_Table.id = " + alertId;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    int alertresolveIndex = cursor.getColumnIndex(KEY_RESOLVE_STATUS);

    category = cursor.getInt(alertresolveIndex);

    return category;
  }

  // This method decides if the alert should be triggered.
  public void triggredState(String category, ArrayList list, int alertId, int resolvestatuss) {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
    StringBuffer sb = new StringBuffer();

    for (int j = 0; j < list.size(); j++) {
      String condition = list.get(j).toString();
      sb.append(list.get(j));

    }
    String finalCondition = sb.toString();
    Boolean result = null;
    try {
      result = (Boolean) engine.eval(finalCondition);
    } catch (ScriptException e) {
      e.printStackTrace();
    }
    //Alert notification interval timer.
    if (result && resolvestatuss == 0) {
      if (gettriggereddate(alertId) == null && gettriggeredstatus(alertId) == 0) {
        updateAlert(alertId);
        notification = notification + 1;
        fireNotification(category, notification);
      } else {
        String date = gettriggereddate(alertId);
        Date date1 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();

        try {
          convertedDate = dateFormat.parse(date);

        } catch (ParseException e) {
          e.printStackTrace();
        }
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long diff = date1.getTime() - convertedDate.getTime();
        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;
        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;
        long elapsedMinutes = diff / minutesInMilli;
        //different = different % minutesInMilli;

        if (elapsedMinutes >= 30) {
          updateAlert(alertId);
          notification = notification + 1;
          fireNotification(category, notification);
        }
      }

    } else {
    }
    sb.delete(0, sb.length());
  }

  public void fireNotification(String category, int notificationId) {
    if (category.equals("RED")) {
      Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification);
      NotificationCompat.Builder mBuilder =
          (NotificationCompat.Builder) new NotificationCompat.Builder(context)
              .setSmallIcon(R.drawable.notification)
              .setDefaults(Notification.DEFAULT_SOUND)
              .setStyle(new NotificationCompat.BigPictureStyle()
                  .bigPicture(bitmap).setSummaryText("This is Red alert"))
              .setContentTitle("Aido Alert Notification")
              .setContentText(" This is Red alert");
      mNotificationManager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      mNotificationManager.notify(notificationId, mBuilder.build());

    }

    if (category.equals("YELLOW")) {

      NotificationCompat.Builder mBuilder =
          (NotificationCompat.Builder) new NotificationCompat.Builder(context)
              .setSmallIcon(R.drawable.notification)
              .setDefaults(Notification.DEFAULT_SOUND)
              .setContentTitle("Aido Alert Notification")
              .setContentText(" This is Yellow alert");
      mNotificationManager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      mNotificationManager.notify(notificationId, mBuilder.build());

    }

    if (category.equals("GREEN")) {

      NotificationCompat.Builder mBuilder =
          (NotificationCompat.Builder) new NotificationCompat.Builder(context)
              .setSmallIcon(R.drawable.notification)
              .setContentTitle("Aido Alert Notification")
              .setContentText(" This is Green alert");
      mNotificationManager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      mNotificationManager.notify(notificationId, mBuilder.build());

    }

  }

  // Gets the triggered alerts date and time.
  public String gettriggereddate(int alertid) {
    String triggerddate;
    String selectQuery = "SELECT * FROM " + TABLE_ALERT + " WHERE Alert_Table.id = " + alertid;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    int alerttriggertdate = cursor.getColumnIndex(KEY_DATE_AND_TIME_OF_TRIGGER);
    int triggeredstatus = cursor.getColumnIndex(KEY_TRIGGRED_STATUS);
    triggerddate = cursor.getString(alerttriggertdate);
    if (triggerddate != null) {
      return triggerddate;
    } else {
      return triggerddate;
    }

  }

  public int gettriggeredstatus(int alertid) {
    int triggerdsttus;
    String selectQuery = "SELECT * FROM " + TABLE_ALERT + " WHERE Alert_Table.id = " + alertid;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    int triggeredstatus = cursor.getColumnIndex(KEY_TRIGGRED_STATUS);
    triggerdsttus = cursor.getInt(triggeredstatus);
    return triggerdsttus;

  }
}

