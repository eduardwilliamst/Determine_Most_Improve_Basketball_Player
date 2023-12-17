package com.example.determinemostimprovebasketballplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AddTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)

        val addButton: Button = findViewById(R.id.button_save_team)

        addButton.setOnClickListener {
            // Ambil data dari EditText
            val nameEditText: EditText = findViewById(R.id.edit_text_name)
            val cityEditText: EditText = findViewById(R.id.edit_text_city)

            val teamName = nameEditText.text.toString()
            val teamCity = cityEditText.text.toString()

            // Buat request Volley untuk menyimpan data
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.126.86/ta_160419129/insert_team.php"

            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d("InsertTeam", response)
                    // Setelah data disimpan, kembali ke halaman sebelumnya
                    onBackPressed()
                },
                Response.ErrorListener {
                    Log.e("InsertTeam", it.message.toString())
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["nama"] = teamName
                    params["kota"] = teamCity
                    return params
                }
            }

            queue.add(stringRequest)
        }
    }
}