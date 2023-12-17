package com.example.determinemostimprovebasketballplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AddPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)

        val addButton: Button = findViewById(R.id.button_save_player)


        val teamId = intent.getStringExtra("TEAM_ID")

        addButton.setOnClickListener {
            // Ambil data dari EditText
            val nameEditText: EditText = findViewById(R.id.edit_text_name_pemain)
            val umurEditText: EditText = findViewById(R.id.edit_text_age_pemain)
            val tinggiEditText: EditText = findViewById(R.id.edit_text_height_pemain)
            val beratEditText: EditText = findViewById(R.id.edit_text_weight_pemain)

            val playerName = nameEditText.text.toString()
            val playerAge = umurEditText.text.toString()
            val playerHeight = tinggiEditText.text.toString()
            val playerWeight = beratEditText.text.toString()

            // Buat request Volley untuk menyimpan data
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.126.86/ta_160419129/insert_pemain.php"

            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d("InsertPlayer", response)
                    // Setelah data disimpan, kembali ke halaman sebelumnya
                    onBackPressed()
                },
                Response.ErrorListener {
                    Log.e("InsertPlayer", it.message.toString())
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["nama"] = playerName
                    params["umur"] = playerAge
                    params["tinggi"] = playerHeight
                    params["berat"] = playerWeight
                    params["team_id"] = teamId.toString()
                    return params
                }
            }

            queue.add(stringRequest)
        }
    }
}