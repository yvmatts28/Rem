package com.technology.codespace.rem.Activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.awesome.dialog.AwesomeCustomDialog
import com.awesome.shorty.AwesomeToast
import com.technology.codespace.rem.Adapter.ContactAdapter
import com.technology.codespace.rem.Data.Contact
import com.technology.codespace.rem.R
import kotlinx.android.synthetic.main.activity_patient_sign_up2.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.contact_popup.view.*
import org.json.JSONArray
import org.json.JSONObject


class ProfileActivity : AppCompatActivity() {

    var list: ArrayList<Contact>? = null
    var adapter: ContactAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var p = Paint()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        list = ArrayList<Contact>()
        layoutManager = LinearLayoutManager(this)

        //var rview = findViewById<RecyclerView>(R.id.con_recyclerView)
        con_recyclerView.layoutManager = layoutManager


        var url = "https://rem-codespace.herokuapp.com/patient/getPatientDetails"

        var sharedPrefs: SharedPreferences = getSharedPreferences("loginToken", Context.MODE_PRIVATE)
        var token = sharedPrefs.getString("token", "")

        var s: StringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener {

            response: String ->
            var obj = JSONObject(response)
            var success = obj.getBoolean("success")
            var message = obj.getString("message")
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            if (success) {
                list!!.clear()
                var bio = obj.getJSONObject("patient").getString("bio")
                bioText.setText(bio)
                var conJArray: JSONArray = obj.getJSONObject("patient").getJSONArray("contacts")
                if (conJArray.length() == 0)
                    AwesomeToast.error(this, "No contacts as of now", Toast.LENGTH_LONG).show()
                else {

                    for (i in 0 until conJArray.length() - 1) {
                        var c = Contact()
                        c.cname = conJArray.getJSONObject(i).getString("name")
                        c.cno = conJArray.getJSONObject(i).getString("contact")
                        //list.add(Contact(conJArray.getJSONObject(i).getString("name"), conJArray.getJSONObject(i).getString("contact")))
                        list!!.add(c)
                        adapter = ContactAdapter(this, list!!)
                        con_recyclerView.adapter = adapter
                        adapter!!.notifyDataSetChanged()


                    }
                }
            }


        }, Response.ErrorListener { }) {
            override fun getParams(): Map<String, String> {

                var params: Map<String, String> = mapOf("token" to token)
                return params
            }
        }

        Volley.newRequestQueue(this).add(s)

        fab_menu.cab.setOnClickListener {

            val intent = packageManager.getLaunchIntentForPackage("com.olacabs.customer")
            if (intent != null) {
                startActivity(intent)
            } else {
                var uri = Uri.parse("market://details?id=com.ola.olacabs.customer")
                val i = Intent(Intent.ACTION_VIEW, uri)
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

                try {
                    startActivity(i)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW))
                    Uri.parse("http://play.google.com/store/apps/details?id=com.ola.olacabs.customer")
                }
            }
        }
        fab_menu.navigate.setOnClickListener {
            var map = "http://maps.google.co.in/maps?q=" + "VIT"
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(map))
            startActivity(i)
        }

    }

//    private fun initSwipe() {
//        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//
//            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//
//                if (direction == ItemTouchHelper.LEFT) {
//                    adapter!!.removeItem(position)
//                } else {
//
//                }
//            }
//
//            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//
//                val icon: Bitmap
//                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//
//                    val itemView = viewHolder.itemView
//                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
//                    val width = height / 3
//
//                    if (dX > 0) {
//                        p.color = Color.parseColor("#388E3C")
//                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
//                        c.drawRect(background, p)
//                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_local_phone_black_24dp)
//                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
//                        c.drawBitmap(icon, null, icon_dest, p)
//                    } else {
//                        p.color = Color.parseColor("#D32F2F")
//                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
//                        c.drawRect(background, p)
//                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete_black_24dp)
//                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
//                        c.drawBitmap(icon, null, icon_dest, p)
//                    }
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(con_recyclerView)
//    }
}

