package com.example.determinemostimprovebasketballplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.determinemostimprovebasketballplayer.player.Player
import com.example.determinemostimprovebasketballplayer.player.PlayersAdapter
import com.example.determinemostimprovebasketballplayer.team.DataTeam
import com.example.determinemostimprovebasketballplayer.team.TeamAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

class TeamActivity : AppCompatActivity() {

    var teams: ArrayList<DataTeam> = ArrayList()
    private var recycler: RecyclerView? = null
    private val adapter = TeamAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val teamId = intent.getStringExtra("TEAM_ID")

        fetchData(teamId)

    }

    private fun fetchData(teamId: String?) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.126.86/ta_160419129/get_team.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("apiresult", response)
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("result") == "OK") {
                        val data = obj.getJSONArray("data")

                        for (i in 0 until data.length()) {
                            val playObj = data.getJSONObject(i)
                            val player = DataTeam(
                                playObj.getString("id"),
                                playObj.getString("nama"),
                                playObj.getString("kota")
                            )
                            teams.add(player)
                        }

                        recycler = findViewById(R.id.recycler_players)

                        recycler?.layoutManager = LinearLayoutManager(this)
                        recycler?.adapter = adapter

                        adapter.updateData(teams)

                        Log.d("cekisiarray", teams.toString())
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                Log.e("Volley Error", it.message.toString())
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                teamId?.let { params["id"] = it }
                return params
            }
        }

        queue.add(stringRequest)
    }
}