package com.technology.codespace.rem.Activity.Patient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.awesome.shorty.AwesomeToast
import com.technology.codespace.rem.Activity.ProfileActivity
import com.technology.codespace.rem.R
import dmax.dialog.SpotsDialog

import kotlinx.android.synthetic.main.activity_patient_username_login.*
import org.json.JSONObject

class PatientUsernameLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_username_login)

        var login_url = "https://rem-codespace.herokuapp.com/authenticate/login"

        profileBtn.setOnClickListener {
            var dialog = SpotsDialog(this,R.style.Custom)

            if(!TextUtils.isEmpty(UsernameText.text.toString()) && UsernameText.text.toString().trim() != "" && !TextUtils.isEmpty(PasswordText.text.toString()) && PasswordText.text.toString().trim() != "") {
                dialog.show()
                var s : StringRequest = object : StringRequest(Request.Method.POST, login_url, Response.Listener {
                    response : String -> var obj = JSONObject(response)
                    var success = obj.getBoolean("success")
                    var message = obj.getString("message")
                    //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    if (success) {
                        var token = obj.getString("token")
                        var sharedPrefs: SharedPreferences = this.getSharedPreferences("loginToken", Context.MODE_PRIVATE)
                        var editor : SharedPreferences.Editor = sharedPrefs.edit()
                        editor.remove("token").commit()
                        editor.putString("token", token)
                        editor.commit()
                        dialog.dismiss()
                        AwesomeToast.success(this,"Logged in!",Toast.LENGTH_LONG).show()
                        val intent = Intent(this,ProfileActivity::class.java)
                        startActivity(intent)
                    }
                }, Response.ErrorListener {  }) {
                    override fun getParams(): Map<String, String> {
                        var params:Map<String, String> = mapOf("email" to UsernameText.text.toString(), "password" to PasswordText.text.toString())
                        return params
                    }
                }
                Volley.newRequestQueue(this).add(s)

            }
            else{
                AwesomeToast.error(this,"Enter Username",Toast.LENGTH_SHORT).show()
            }
        }

        NewUser.setOnClickListener {

            val intent = Intent(this,PatientSignUp1::class.java)
            startActivity(intent)
        }


    }
}
