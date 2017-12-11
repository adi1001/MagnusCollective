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
package aido.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
* This code selects the personality to be executed based on the user's selection, it also selects
* the behavior according to the priority value given. The personality can be selected from the
* customize option found in the settings menu.The default home screen of the app contains Aido's
* eyes animation which are set to an idle behaviour awaiting the users command from STT
* This code takes the personality input in the form of a .csv file stored in AIDO folder in device
* storage.
*/

//This class sets parameters for priority of behaviours to be executed in a personality

public class AlertBehaviourProperties {


    public static int none                 = -1 ;
    public static int faceRecognition      = 0 ;
    public static int faceDetection        = 1 ;
    public static int faceTracking         = 2 ;
    public static int emotionRecognition   = 3 ;
    public static int temperatureSensor    = 4 ;
    public static int airQuality           = 5 ;
    public static int headProjectorOn      = 6 ;
    public static int headProjectorOff     = 7 ;
    public static int mainProjectorOn      = 8 ;
    public static int mainProjectorOff     = 9 ;
    public static int piCameranOn          = 10 ;
    public static int skypeCallReceive     = 11 ;
    public static int skypeCallMake        = 12 ;
    public static int emailReceived        = 13 ;
    public static int internetConnected    = 14 ;
    public static int batteryLow           = 15 ;
    public static int batteryFull          = 16 ;
    public static int shutDown             = 17 ;
    public static int showingNotifications = 18 ;
    public static int idle                 = 19 ;
    public static int scene                = 20 ;
    public static int shopping             = 21 ;
    public static int ledOn                = 22 ;
    public static int ledOff               = 23 ;
    public static int pressureSensor       = 24 ;
    public static int powerRemaining       = 26 ;
    public static int directRun            = 100 ;




    //This is used to assign the behavior values into the respective variables.
    public static final Map<Integer, String> alertBEHAVIUORHASH;

    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();

        aMap.put(faceRecognition,"facerecognition.txt");
        aMap.put(faceDetection,"facedetection");
        aMap.put(faceTracking,"facetracking");
        aMap.put(emotionRecognition,"emotionrecognition");
        aMap.put(temperatureSensor,"temperaturesensorreading");
        aMap.put(airQuality,"airqualitysensorreading");
        aMap.put(headProjectorOn,"headprojectoron");
        aMap.put(headProjectorOff,"headprojectoroff");
        aMap.put(mainProjectorOn,"mainprojectoron");
        aMap.put(mainProjectorOff,"mainprojectoroff");
        aMap.put(piCameranOn,"picameraon");
        aMap.put(skypeCallReceive,"receivingskypecall");
        aMap.put(skypeCallMake,"callingskype");
        aMap.put(emailReceived,"emailreceived");
        aMap.put(internetConnected,"internetconnected");
        aMap.put(batteryLow,"lowbattery");
        aMap.put(batteryFull,"batteryfull");
        aMap.put(shutDown,"shutdownmsg");
        aMap.put(showingNotifications,"notification");
        aMap.put(idle,"idle.txt");
        aMap.put(scene,"scene");
        aMap.put(directRun,"");
        aMap.put(shopping,"shopping");
        aMap.put(ledOn,"ledaon");
        aMap.put(ledOff,"ledaoff");
        aMap.put(pressureSensor,"pressuresensor");
        aMap.put(airQuality,"airquality");
        aMap.put(powerRemaining,"poweremaining");

        alertBEHAVIUORHASH = Collections.unmodifiableMap(aMap);
    }

    //This is used to set the priority of the user selected behavior.
    public static final Map<Integer, Integer> alertBEHAVIOURPRIORITY;

    static {
        Map<Integer, Integer> aMap = new HashMap<Integer, Integer>();

        aMap.put(shutDown,100);
        aMap.put(batteryLow,50);
        aMap.put(directRun,40);
        aMap.put(internetConnected,31);
        aMap.put(scene,30);
        aMap.put(skypeCallReceive,25);
        aMap.put(skypeCallMake,25);
        aMap.put(emailReceived,24);
        aMap.put(ledOn,24);
        aMap.put(ledOff,24);
        aMap.put(shopping,23);
        aMap.put(showingNotifications,22);
        aMap.put(headProjectorOn,21);
        aMap.put(headProjectorOff,21);
        aMap.put(mainProjectorOn,21);
        aMap.put(mainProjectorOff,21);
        aMap.put(faceRecognition,20);
        aMap.put(emotionRecognition,19);
        aMap.put(faceDetection,18);
        aMap.put(faceTracking,10);
        aMap.put(temperatureSensor,8);
        aMap.put(powerRemaining,8);
        aMap.put(airQuality,8);
        aMap.put(pressureSensor,7);
        aMap.put(piCameranOn,6);
        aMap.put(idle,0);

        alertBEHAVIOURPRIORITY = Collections.unmodifiableMap(aMap);
    }

    //This assigns the values in the list of priority by which it has to be done.
    public static final Map<Integer, String> alertBEHAVIOURVARIABLE;

    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();

        aMap.put(shutDown,"aSHUTDOWNa");
        aMap.put(batteryLow,"aBATTERYLOWa");
        aMap.put(directRun,"aDIRECTRUNa");
        aMap.put(internetConnected,"aINTERNETCONNECTEDa");
        aMap.put(scene,"aSCENEa");
        aMap.put(skypeCallReceive,"aSKYPERECEIVEa");
        aMap.put(skypeCallMake,"aSKYPEMAKEa");
        aMap.put(emailReceived,"aEMAILRECEIVEa");
        aMap.put(showingNotifications,"aSHOWNOTIFIATIONSa");
        aMap.put(headProjectorOn,"aHEADPROJECTORONa");
        aMap.put(headProjectorOff,"aHEADPROJECTOROFFa");
        aMap.put(mainProjectorOn,"aMAINPROJECTORONa");
        aMap.put(mainProjectorOff,"aMAINPROJECTOROFFa");
        aMap.put(faceRecognition,"aFACERECOGa");
        aMap.put(emotionRecognition,"aEMOTIONRECOGa");
        aMap.put(faceDetection,"aFACEDETECTa");
        aMap.put(faceTracking,"aFACETRACKa");
        aMap.put(temperatureSensor,"aTEMPSENSORa");
        aMap.put(airQuality,"aAIRQUALITYa");
        aMap.put(piCameranOn,"aPICAMERAa");
        aMap.put(powerRemaining,"aPOWERREMAINa");
        aMap.put(pressureSensor,"aPRESSURE");
        aMap.put(idle,"aIDLEa");

        alertBEHAVIOURVARIABLE = Collections.unmodifiableMap(aMap);
    }


    //This gets the personality types from the Aido Face and add in priority playlist.
    public static String GetPeronalityPath()
    {
        String name= StorageProperties.getRootDirectory();

        return name;

    }

    //This checks the behavior priority matches with the requested behavior or not.
    public static boolean checkIfBehaviorNameMatchesRequest(int behavecode, String requestedBehavior) {

        if(!alertBEHAVIUORHASH.containsKey(behavecode)) {
            return false;
        }

        String behaviorfile = alertBEHAVIUORHASH.get(behavecode);

        if(behaviorfile.equalsIgnoreCase(requestedBehavior)) {
            return true;
        }

        return false;
    }
    
    //object definitions

    public static  String lastFaceRecognition       = "" ;
    public static  String lastFaceDetection         = "" ;
    public static  String lastEmotionRecognition    = "" ;
    public static  String lastTemperatureSensor     = "" ;
    public static  String lastAirQuality            = "" ;
    public static  String lastPowerRemaining        = "" ;
    public static  String lastPressure              = "" ;
    public static  String lastShowingNotifications  = "" ;

}

