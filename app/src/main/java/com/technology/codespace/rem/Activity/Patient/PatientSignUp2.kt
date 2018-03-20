package com.technology.codespace.rem.Activity.Patient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.technology.codespace.rem.Activity.ProfileActivity
import com.technology.codespace.rem.Adapter.ContactAdapter
import com.technology.codespace.rem.Data.Contact
import com.technology.codespace.rem.R
import kotlinx.android.synthetic.main.activity_patient_sign_up2.*
import org.json.JSONObject

class PatientSignUp2 : AppCompatActivity() {
    val CONTACT_CODE = 1
    var contactUri: Uri? = null
    var list: ArrayList<Contact>? = null
    var adapter: ContactAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var contact_url = "https://rem-codespace.herokuapp.com/patient/addContact"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_sign_up2)



        list = ArrayList<Contact>()
        layoutManager = LinearLayoutManager(this)

        contactRecyclerView.layoutManager = layoutManager

        signUpNextBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        addContactBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.Contacts.CONTENT_TYPE
            startActivityForResult(intent, CONTACT_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_CODE && resultCode == Activity.RESULT_OK) {
            contactUri = data!!.data
            getContactInfo()
        } else {

        }

    }

    private fun getContactInfo() {
        val cursor = contentResolver.query(contactUri, null, null, null, null)
        if (cursor!!.moveToFirst()) {

            val contactName = cursor.getString(cursor.getColumnIndex((ContactsContract.Contacts.DISPLAY_NAME)))
            //val phoneNo = cursor.getString(cursor.getColumnIndex((ContactsContract.Contacts.HAS_PHONE_NUMBER)))
            val phoneNo = retrieveContactNumber()
            var con = Contact()
            con.cname = contactName
            con.cno = phoneNo
            Log.d("Name", contactName)

            var s: StringRequest = object : StringRequest(Request.Method.POST, contact_url,
                    Response.Listener { response: String ->
                        var obj: JSONObject = JSONObject(response)
                        var status = obj.getBoolean("success")
                        var message = obj.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    },
                    Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    var sharedPrefs: SharedPreferences = getSharedPreferences("loginToken", Context.MODE_PRIVATE)
                    var token = sharedPrefs.getString("token", "")
                    var params:Map<String, String> = mapOf("token" to token, "name" to contactName, "contact" to phoneNo)
                    return params
                }
            }

            Volley.newRequestQueue(this).add(s)


            list!!.add(con)
            adapter = ContactAdapter(this, list!!)
            contactRecyclerView.adapter = adapter
            adapter!!.notifyDataSetChanged()


        }
    }

    fun retrieveContactNumber(): String {

        var contactID: String? = null
        var contactNumber: String? = null
        val cursorID = contentResolver.query(contactUri,
                arrayOf(ContactsContract.Contacts._ID), null, null, null)

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                arrayOf(contactID),
                null)

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        return contactNumber!!

        cursorPhone.close();
    }

}
