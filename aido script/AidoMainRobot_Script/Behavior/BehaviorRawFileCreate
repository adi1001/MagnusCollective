/*
    Created on 15/02/17 by Ingen Dynamics Inc. as main Behaviour engine code for Aido Robot.
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

package com.whitesun.aidoface.behavior;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.Aido.aidohomerobot.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aido.UI.ComboBoxClass;
import aido.camera.fileReadWrite;
import aido.common.CommonlyUsed;
import aido.properties.BehaveProperties;
import aido.properties.StorageProperties;

/*
 * This class contains the context for customize menu in Aido settings. this has several inputs
 * which are selected by the user: behaiour title, behaviour file, behaviour duration, category,
 * animation, pan & tilt values for behaviour, behaviour audio, and text to speech output
 */

public class BehaviorRawFileCreate extends AppCompatActivity {

    Context mainContext;


    String behaviorFile = "";
    List<String> listData = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    private EditText etrawbehavetitle;
    private ComboBoxClass spRawBehaveFiles;
    private ComboBoxClass spRawBehaveDuration;
    private ComboBoxClass spRawBehaveCategory;
    private ComboBoxClass spRawBehaveAnimation;
    private ComboBoxClass spRawBehaveMotorPan;
    private ComboBoxClass spRawBehaveMotorTilt;
    private ComboBoxClass spRawBehaveAudio;
    private EditText spRawBehaveTts;
    private Button btRawBehaveSubmit;
    private ListView lvRawBehaveCommands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitybehaviourrawfilecreate);

        mainContext = BehaviorRawFileCreate.this;

        findViews();
    }

    //Acquires the data and displays in the ListView
    private void findViews() {
        lvRawBehaveCommands = (ListView) findViewById(R.id.lvRawBehaveCommands);
        listAdapter = new ArrayAdapter<String>(this, R.layout.simpleliscustomitem, android.R.id.text1, listData);


        // Assign adapter to ListView
        lvRawBehaveCommands.setAdapter(listAdapter);


        etrawbehavetitle = (EditText) findViewById(R.id.etrawbehavetitle);
        etrawbehavetitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (CommonlyUsed.stringIsNullOrEmpty(etrawbehavetitle.getText().toString())) {
                    return;
                }

                behaviorFile = BehaveProperties.getRawBehaviorDir() + etrawbehavetitle.getText().toString();
                spRawBehaveFiles.setSelectedIndex(0);

                fillCommandsinList();
            }
        });

        spRawBehaveFiles = (ComboBoxClass) findViewById(R.id.spRawBehaveFiles);
        //The behavior files are fetched from the storage and appended in sequence to be played.
        fillFiles();

        //The behavior is selected by user from the aido ComboBoxClass
        spRawBehaveFiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spRawBehaveFiles.getSelectedIndex() > 0) {
                    behaviorFile = BehaveProperties.getRawBehaviorDir() + spRawBehaveFiles.getSelectedText();

                    fillCommandsinList();

                    etrawbehavetitle.setText(spRawBehaveFiles.getSelectedText());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spRawBehaveDuration = (ComboBoxClass) findViewById(R.id.spRawBehaveTime);

        fillTime();

        spRawBehaveCategory = (ComboBoxClass) findViewById(R.id.spRawBehaveCategory);

        fillCategory();
        //This select the animation from the lvRawBehaveCommands(ListView) and plays the selected animation.
        spRawBehaveCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillAnimation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spRawBehaveAnimation = (ComboBoxClass) findViewById(R.id.spRawBehaveAnimation);
        spRawBehaveMotorPan = (ComboBoxClass) findViewById(R.id.spRawBehaveMotorPan);
        spRawBehaveMotorTilt = (ComboBoxClass) findViewById(R.id.spRawBehaveMotorTilt);
        fillPanTilt();
        spRawBehaveAudio = (ComboBoxClass) findViewById(R.id.spRawBehaveAudio);
        spRawBehaveTts = (EditText) findViewById(R.id.spRawBehaveTts);
        btRawBehaveSubmit = (Button) findViewById(R.id.btRawBehaveSubmit);

        btRawBehaveSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                List<String> vallist = checkValues();

                if (vallist.size() <= 0) {
                    CommonlyUsed.showMsg(getApplicationContext(), "Invalid values");
                    return;
                }


                String strtowrite = CommonlyUsed.createCSVstringFromList(vallist, ",") + "\n";

                CommonlyUsed.showMsg(getApplicationContext(), "file: " + behaviorFile);
                fileReadWrite.addStringToTextFile(strtowrite, behaviorFile);

                fillCommandsinList();

            }
        });

        lvRawBehaveCommands.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                fileReadWrite.removeLine(behaviorFile, i);
                fillCommandsinList();
                return false;
            }
        });

    }

    //The behavior files are fetched from the storage and appended in sequence to be played/performed.
    void fillFiles() {
        spRawBehaveFiles.appendItem("Select Behavior");

        List<String> files = fileReadWrite.getAllLeafFilesWithin(BehaveProperties.getRawBehaviorDir());


        for (int i = 0; i < files.size(); i++) {
            spRawBehaveFiles.appendItem("" + files.get(i));
        }

        spRawBehaveFiles.setSelectedIndex(-1);

    }

    //This method clears the previous behavior in the list and appends the new one in the list
    void fillCommandsinList() {
        listData.clear();
        if (fileReadWrite.fileExists(behaviorFile)) {
            List<String> lines = fileReadWrite.readFileLinesIntoList(behaviorFile);

            for (int i = 0; i < lines.size(); i++) {
                listData.add(lines.get(i));
            }


        }
        listAdapter.notifyDataSetChanged();
        lvRawBehaveCommands.smoothScrollToPosition(listData.size() - 1);

    }

    //The duration of the behaviour is assigned as per user and converted to appropriate delay
    void fillTime() {

        spRawBehaveDuration.appendItem("Select Time");

        for (int i = 0; i < 500; i++) {
            spRawBehaveDuration.appendItem("" + i);
        }

        spRawBehaveDuration.setSelectedIndex(-1);
    }

    //Based on the selected behavior category is selected by the user eg:behavior: Emotion,Category:Happy or sad etc.
    void fillCategory() {

        spRawBehaveCategory.appendItem("Select Category");

        List<String> categories = fileReadWrite.getAllDirectoryNamesWithin(BehaveProperties.getUiVideosDir());

        for (int i = 0; i < categories.size(); i++) {
            spRawBehaveCategory.appendItem("" + categories.get(i));
        }

        spRawBehaveCategory.setSelectedIndex(-1);
    }

    //This method is used to select the behavior animation.
    void fillAnimation() {
        spRawBehaveAnimation.clearAllItems(true);

        if (CommonlyUsed.stringIsNullOrEmpty(spRawBehaveCategory.getSelectedText())
                || spRawBehaveCategory.getSelectedIndex() < 0


                ) {
            CommonlyUsed.showMsg(mainContext, "Invalid Category");
            return;
        }


        CommonlyUsed.showMsg(mainContext, "selected : " + spRawBehaveCategory.getSelectedText());
        spRawBehaveAnimation.appendItem("Select Animation");

        List<String> animations = fileReadWrite.getAllLeafFilesWithin(BehaveProperties.getUiVideosDir() + File.separator + spRawBehaveCategory.getSelectedText());

        for (int i = 0; i < animations.size(); i++) {
            spRawBehaveAnimation.appendItem("" + animations.get(i));
        }

        spRawBehaveAnimation.setSelectedIndex(-1);
    }

    /*This method is used to set the neck movment of Aido. eg:X-axis:Pan and Y-axis:Tilt.
     Taking feedback from servo motors*/
    void fillPanTilt() {
        spRawBehaveMotorPan.appendItem("Select Pan");
        int start = 120;
        spRawBehaveMotorPan.appendItem("" + "HOME");
        for (start = 120; start >= -120; start -= 10) {
            spRawBehaveMotorPan.appendItem("" + start);
        }

        for (start = 120; start >= -120; start -= 10) {
            spRawBehaveMotorPan.appendItem("abs:" + start);
        }

        spRawBehaveMotorPan.setSelectedIndex(-1);
        spRawBehaveMotorTilt.appendItem("Select Tilt");
        spRawBehaveMotorTilt.appendItem("" + "HOME");

        for (start = 120; start >= -120; start -= 10) {
            spRawBehaveMotorTilt.appendItem("" + start);
        }

        for (start = 120; start >= -120; start -= 10) {
            spRawBehaveMotorTilt.appendItem("abs:" + start);
        }

        spRawBehaveMotorTilt.setSelectedIndex(-1);
    }

    //This method is used check the validit of the behavior created by user.
    List<String> checkValues() {
        List<String> retList = new ArrayList<>();
    //This checks the Title while creating the raw behaviour in customization.
        if (CommonlyUsed.stringIsNullOrEmpty(etrawbehavetitle.getText().toString())) {
            CommonlyUsed.showMsg(mainContext, "Invalid title");
            retList.clear();
            return retList;
        }
    //This checks the Duration of behaviour to be plays.
        if (CommonlyUsed.stringIsNullOrEmpty(spRawBehaveDuration.getSelectedText())
                || spRawBehaveDuration.getSelectedIndex() <= 0) {
            CommonlyUsed.showMsg(mainContext, "Invalid Time");
            retList.clear();
            return retList;
        }

        retList.add(spRawBehaveDuration.getSelectedText());

        if (CommonlyUsed.stringIsNullOrEmpty(spRawBehaveCategory.getSelectedText())
                || spRawBehaveCategory.getSelectedIndex() <= 0) {

        }

        if (CommonlyUsed.stringIsNullOrEmpty(spRawBehaveAnimation.getSelectedText())
                || spRawBehaveAnimation.getSelectedIndex() < 0) {

        }

        String movieName = "0";

    /*This checks if user requested behaviour exists then gets the behaviour animation files from
    particular directory in the storage and adds it in the queue.
    */
        if (spRawBehaveAnimation.getSelectedIndex() > 0) {

            movieName = BehaveProperties.getUIVideosDirLeaf() + spRawBehaveCategory.getSelectedText()
                    + StorageProperties.getSeparator() + spRawBehaveAnimation.getSelectedText();
        }

        retList.add(movieName);

        if (CommonlyUsed.stringIsNullOrEmpty(spRawBehaveMotorPan.getSelectedText())
                || spRawBehaveMotorPan.getSelectedIndex() <= 0) {
            CommonlyUsed.showMsg(mainContext, "Invalid PAN");

        }
    /*This checks if user requested pan value exists then adds it in the queue and performs the
    command during the animation*/
        String pan = "0";
        if (spRawBehaveMotorPan.getSelectedIndex() > 0) {
            pan = spRawBehaveMotorPan.getSelectedText();
        }

        retList.add(pan);

        if (CommonlyUsed.stringIsNullOrEmpty(spRawBehaveMotorTilt.getSelectedText())
                || spRawBehaveMotorTilt.getSelectedIndex() <= 0) {
            CommonlyUsed.showMsg(mainContext, "Invalid Tilt");
        }
     /*This checks if user requested Tilt value exists then adds it in the queue and performs the
     command during the animation*/
        String tiltRotation = "0";
        if (spRawBehaveMotorTilt.getSelectedIndex() > 0) {
            tiltRotation = spRawBehaveMotorTilt.getSelectedText();
        }

        retList.add(tiltRotation);

        if ((CommonlyUsed.stringIsNullOrEmpty(spRawBehaveAudio.getSelectedText())
                || (spRawBehaveAudio.getSelectedIndex() <= 0))
                && CommonlyUsed.stringIsNullOrEmpty(etrawbehavetitle.getText().toString())) {
            CommonlyUsed.showMsg(mainContext, "Invalid Audio");
        }

     //User selected audio files will play during the animation.
        String audioFile = spRawBehaveTts.getText().toString();

        if (CommonlyUsed.stringIsNullOrEmpty(audioFile)) {
            audioFile = spRawBehaveAudio.getSelectedText();
        }

        if (CommonlyUsed.stringIsNullOrEmpty(audioFile)) {
            audioFile = "0";
        }
        retList.add(audioFile);
        retList.add("50");
        return retList;
    }

}

