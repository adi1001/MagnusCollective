/*  Created on 15/02/17 by Ingen Dynamics Inc. as main Behaviour engine code for Aido Robot.
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

import java.io.File;

/*
 * The following class selects the animation/behaviour to be played on the behaviour UI by fetching
 * the corresponding video file from the root directory of android storage. The class browses
 * through the folders present in Aido directory to play/perform particular behaviours/animations.
 */

// This class fetches video/animation from the device storage and plays.
public class BehaveProperties {

    public static String getUiVideosDir() {
        String name= StorageProperties.getRootDirectory() + "uivideos" + StorageProperties.getSeparator();
        File tempf = new File(name);

        if(!tempf.exists()) {
            tempf.mkdirs();
        }
        return name;
    }

    //This fetches the video file from the device storage where all the aido animation videos are stored.
    public static String getUIVideosDirLeaf() {
        String name=   "uivideos" + StorageProperties.getSeparator();

        return name;
    }

    //This is used to get the user selected options from the customization stored in the raw file directory.
    public static String getRawBehaviourDirLeaf() {
        String name=  "rawbehavior" + StorageProperties.getSeparator();

        return name;
    }

    //The following class checks if the user selected behaviour exists in raw behaviour file directory.
    public static String getRawBehaviorDir() {

        String name= StorageProperties.getRootDirectory() + getRawBehaviourDirLeaf();
                File tempf = new File(name);

        if(!tempf.exists()) {
            tempf.mkdirs();
        }
        return name;
    }

}

