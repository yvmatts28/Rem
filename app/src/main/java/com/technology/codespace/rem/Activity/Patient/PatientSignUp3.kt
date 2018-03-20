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
import kotlinx.android.synthetic.main.activity_patient_sign_up3.*
import org.json.JSONObject

class PatientSignUp3 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_sign_up3)

        var reg_url = "https://rem-codespace.herokuapp.com/authenticate/registerPatient"
        var login_url = "https://rem-codespace.herokuapp.com/authenticate/login"
        var data = intent.extras
        var name = intent.extras.getString("Name")
        var username = intent.extras.getString("Username")
        var password = intent.extras.getString("Password")
        var email = intent.extras.getString("Email")
        var location = intent.extras.getString("Location")
        bioText.setText("My name is $name")
        saveBtn.setOnClickListener {
            if(!TextUtils.isEmpty(bioText.text.toString()) && bioText.text.toString() !="" && bioText.text.toString().count()>4)
            {
                    var dialog = SpotsDialog(this,R.style.Custom2)
                dialog.show()
                var s:StringRequest = object : StringRequest(Request.Method.POST,reg_url,
                         Response.Listener { response: String ->

                             var obj :JSONObject = JSONObject(response)
                             var status = obj.getBoolean("success")
                             var message = obj.getString("message")

                             //Toast.makeText(this, message, Toast.LENGTH_LONG).show()/*
//                             val intent = Intent(this,PatientSignUp2::class.java)
//                             startActivity(intent)*/
                             if(status)
                             {
                                 var s1: StringRequest = object:StringRequest(Request.Method.POST,login_url,
                                         Response.Listener {  response: String ->
                                             var obj1 : JSONObject = JSONObject(response)

                                             var status = obj1.getBoolean("success")
                                             var message = obj1.getString("message")
                                             Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                                             if (status) {
                                                 var token = obj1.getString("token")
                                                 var sharedPrefs: SharedPreferences = this.getSharedPreferences("loginToken", Context.MODE_PRIVATE)
                                                 var editor : SharedPreferences.Editor = sharedPrefs.edit()
                                                 editor.putString("token", token)
                                                 editor.commit()
                                                 dialog.dismiss()
                                                 val intent = Intent(this,PatientSignUp2::class.java)
                                                 startActivity(intent)
                                             }
                                             else if(!status)
                                             {
                                                 dialog.dismiss()
                                                 AwesomeToast.error(this,"Account already exists").show()
                                             }
                                         },
                                         Response.ErrorListener {
                                            dialog.dismiss()
                                             AwesomeToast.error(this,"Account already exists").show()
                                         }){
                                     override fun getParams(): Map<String, String> {
                                         var params1:Map<String, String> = mapOf("email" to email, "password" to password)
                                         return params1
                                     }
                                 }

                                 Volley.newRequestQueue(this).add(s1)

                             }

                         }
                        , Response.ErrorListener {                                               AwesomeToast.error(this,"Account already exists").show()
                }){
                    override fun getParams(): Map<String, String> {
                        var params:Map<String, String> = mapOf("name" to name, "username" to username, "password" to password, "email" to email, "address" to location, "bio" to bioText.text.toString())
                        return params
                    }
                }
                Volley.newRequestQueue(this).add(s)
            }
            else
            {

                AwesomeToast.error(this,"We need more about you!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
