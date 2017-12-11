/*  Created on 20/08/17 by Ingen Dynamics Inc.
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

package com.rathore.evernoteapi;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.evernote.client.android.EvernoteOAuthActivity;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginActivity;

import java.util.Arrays;
import java.util.List;

/* This class is used to check the login. If the Login has not done it launch login activity to login the user.
 */
public class LoginChecker implements Application.ActivityLifecycleCallbacks {
    private static final List<Class<? extends Activity>> IGNORED_ACTIVITIES = Arrays.asList(
            LoginActivity.class,
            EvernoteLoginActivity.class,
            EvernoteOAuthActivity.class
    );

    private Intent mCachedIntent;

    //Checks if the user logged in if not then launch the LoginActivity to login the user.
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!EvernoteSession.getInstance().isLoggedIn() && !isIgnored(activity)) {
            mCachedIntent = activity.getIntent();
            LoginActivity.launch(activity);

            activity.finish();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof LoginActivity && EvernoteSession.getInstance().isLoggedIn()) {
            if (mCachedIntent != null) {
                activity.startActivity(mCachedIntent);
                mCachedIntent = null;
            } else {
                activity.startActivity(new Intent(activity, MainActivity.class));
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private boolean isIgnored(Activity activity) {
        return IGNORED_ACTIVITIES.contains(activity.getClass());
    }
}

