package com.psi.nusaku.Utils

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseUser
import com.psi.nusaku.Model.User

class SharedPref (context: Context) {

    private var constant: Constant= Constant();
    private var preferences: SharedPreferences

    init{
        preferences = context.getSharedPreferences(constant.NAME, constant.MODE)
    }

    fun SaveUser(displayName: String, email: String){
        val editor = preferences.edit()
        editor.putBoolean(constant.SESSION_ISLOGIN, true)
        editor.putString(constant.SESSION_USERNAME, displayName)
        editor.putString(constant.SESSION_EMAIL, email)
        editor.commit()
    }

    fun GetUser(): User {
        var username = preferences.getString(constant.SESSION_USERNAME, "")
        var email = preferences.getString(constant.SESSION_EMAIL, "")
        return User(username, email)
    }

    fun Clear() {
        val editor = preferences.edit()
        editor.clear()
    }
}