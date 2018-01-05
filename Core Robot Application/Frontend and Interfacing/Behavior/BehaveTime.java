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
package aido.UI;

/*
 * This class includes code to add new behaviours and edit existing behaviours in the customize menu
 * found at Aido settings.
 * It requires input variable time, which describes duration which the behaviour is to run.
 */

public class BehaveTime {

    long guidance = 0;

    double factor = 0;

    public BehaveTime(int guidance) {
        this.guidance = guidance;
        this.guidance = this.guidance * 1000; // millis
        factor = this.guidance / 8;
    }

    //Duration until which the behavior animation plays.
    public long getAnticipateTime(int override) {
        if (override > 0) {
            return (long) guidance * override / 100;
        }
        return (long) (factor);
    }

    public long getEaseInTime(int override) {
        if (override > 0) {
            return (long) guidance * override / 100;
        }
        return (long) (factor * 0.5d);
    }

    public long getKeyStageTime(int override) {
        if (override > 0) {
            return (long) guidance * override / 100;
        }
        return (long) (factor * 5.0d);
    }

    public long getEaseOutTime(int override) {
        if (override > 0) {
            return (long) guidance * override / 100;
        }

        return (long) (factor * 0.5);
    }

    public long getFollowThroughTime(int override) {
        if (override > 0) {
            return (long) guidance * override / 100;
        }

        return (long) (factor);
    }

}

