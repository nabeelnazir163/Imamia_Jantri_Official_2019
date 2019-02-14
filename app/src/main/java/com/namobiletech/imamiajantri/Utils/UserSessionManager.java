package com.namobiletech.imamiajantri.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.namobiletech.imamiajantri.UI.Activities.MainActivity;
import com.namobiletech.imamiajantri.UI.Activities.SigninScreen;

import java.util.HashMap;

public class UserSessionManager {

    //Shared Pref Reference
    SharedPreferences user_login_pref;

    //Editor reference for Shared preference
    Editor userlogin_editor;

    //Context
    Context mContext;

    //Shared Preference mode
    int PRIVATE_MODE = 0;

    //shared_pref file name
    private static final String LOGIN_PREFERNCE_NAME = "LoginPref";

    //All sharef preference keys
    private static final String IS_USER_LOGIN = "IsUserLogin";

    //Username ( It will be access from outside )
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";

    //email ( It will be access from outside )
    public static final String KEY_PASSWORD = "password";

    //Constructor
    public UserSessionManager(Context context)
    {
//        this.mContext = context;
        this.mContext = context;
        user_login_pref = mContext.getSharedPreferences(LOGIN_PREFERNCE_NAME, PRIVATE_MODE);
        userlogin_editor = user_login_pref.edit();

    }

    //Create Login Session
    public void CreateUSerLoginSession(String email, String password, String name)
    {
        //Storing Login Value as true
        userlogin_editor.putBoolean(IS_USER_LOGIN, true);

        //Storing name in pref
        userlogin_editor.putString(KEY_EMAIL, email);
        userlogin_editor.putString(KEY_NAME, name);

        //Storing emial in pref
        userlogin_editor.putString(KEY_PASSWORD, password);

        //commit chnages
        userlogin_editor.apply();

    }

    /**
     *Check if user is logged in or not
     */

    public boolean isUserLoggedIn()
    {
        return user_login_pref.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean CheckLogin()
    {

        //check login status
        if(!this.isUserLoggedIn()){

//            //user is logged in so redirect it to MainActivity
//            Intent i = new Intent(mContext, SigninScreen.class);
//
//            //Closing all the activites form stack
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            //Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            //starting register activity
//            mContext.startActivity(i);


            return false;

        }

        return true;
    }

    /**
     * Get stored session data
     */

    public HashMap<String, String> getUserDetails()
    {

        //use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<>();

        // username
        user.put(KEY_EMAIL, user_login_pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, user_login_pref.getString(KEY_NAME, null));

        //user emial_id
        user.put(KEY_PASSWORD, user_login_pref.getString(KEY_PASSWORD, null));

        //return user
        return user;
    }

    /**
     * clear session details
     */
    public void Logoutuser()
    {

        //Clear all user data from Shared Preferences
        userlogin_editor.clear();
        userlogin_editor.commit();
/*
//        After logout redirect user to login activity
        Intent i = new Intent(mContext, SigninScreen.class);

        //Closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //start Login Activity
        mContext.startActivity(i);*/

    }
}
