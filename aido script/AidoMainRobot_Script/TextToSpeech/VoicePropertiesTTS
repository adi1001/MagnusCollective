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

package properties;

import com.Aido.texttospeech.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/*This class was developed by Ingen Dynamics Inc. for use in Aido Robotics.
 * This class broadcasts the speech which was converted from text.
 */
public class VoiceProperties {


    public static final Map<String, Integer> PROP_VOICE_HASH;
    public static String PROP_SPEECHTOTEXT_MESSSAGEFIELD = "message";
    public static String PROP_TTS_RECEIVER_INTENTID = "com.aido.texttospeech";
    public static String PROP_TTS_INTENTID = "com.aido.tts";
    public static String TTS_MESSSAGEFIELD = "message";

    static {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put("Hi, I am emma, one of the available high quality text to speech voices. Select download now, to install my voice. Back to. ",
                R.raw.emma);

        aMap.put("Aido", R.raw.aido);
        aMap.put("aidoping", R.raw.ping);
        aMap.put("crazylaugh", R.raw.crazylaugh);
        aMap.put("textmessage", R.raw.textmessage);
        aMap.put("ifeel", R.raw.file1);
        aMap.put("seeappliances", R.raw.file2);
        aMap.put("suffringfrom", R.raw.file3);
        aMap.put("traffic", R.raw.file4);
        aMap.put("weather", R.raw.file5);
        aMap.put("searchrecipe", R.raw.file6);
        aMap.put("nearbyhospitals", R.raw.file7);
        aMap.put("howtomake", R.raw.file8);
        aMap.put("surroundings", R.raw.file9);
        aMap.put("manypeople", R.raw.file10);
        aMap.put("cold", R.raw.filem14);
        aMap.put("yourhealth", R.raw.filem15);
        aMap.put("humid", R.raw.filem16);
        aMap.put("doyouhaveenoughjuice", R.raw.filem17);
        aMap.put("newapps", R.raw.filem18);
        aMap.put("seeallnotes", R.raw.filem20);
        aMap.put("somedrinks", R.raw.filem21);
        aMap.put("bills", R.raw.filem22);
        aMap.put("settimer", R.raw.filem23);
        aMap.put("seehealthreminder", R.raw.filem24);
        aMap.put("seemedication", R.raw.filem25);
        aMap.put("sideeffects", R.raw.filem26);
        aMap.put("takemedicine", R.raw.filem27);
        aMap.put("prescribedmedicine", R.raw.filem24);
        aMap.put("symptoms", R.raw.filem29);
        aMap.put("homereminder", R.raw.filem30);
        aMap.put("homereceipts", R.raw.filem31);
        aMap.put("vendordirectory", R.raw.filem32);
        aMap.put("wine", R.raw.filem36);

        aMap.put("drink", R.raw.file37);


        Map<String, Integer> treeMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return compareInts(s2.length(), s1.length());
                    }
                }
        );

        treeMap.putAll(aMap);


        PROP_VOICE_HASH = Collections.unmodifiableMap(aMap);

    }


    public static int compareInts(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

}

