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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

class DetailTeamActivity : AppCompatActivity() {

    var players: ArrayList<Player> = ArrayList()
    private var recycler: RecyclerView? = null
    private val adapter = PlayersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_team)

        val teamId = intent.getStringExtra("TEAM_ID")

        fetchData(teamId)

        // Cari FAB berdasarkan ID
//        val fabButton: FloatingActionButton = this.findViewById(R.id.fab_add_player)

        // Set listener untuk FAB
//        fabButton.setOnClickListener {
//            // Ketika FAB ditekan, buka AddTeamActivity
//            val intent = Intent(this, AddPlayerActivity::class.java)
//            intent.putExtra("TEAM_ID", teamId)
//            startActivity(intent)
//        }
    }

    private fun fetchData(teamId: String?) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.126.86/ta_160419129/get_pemain.php"

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
                            val player = Player(
                                id = playObj.getString("id"),
                                nama = playObj.getString("nama"),
                                umur = playObj.getString("umur"),
                                tinggi = playObj.getString("tinggi"),
                                berat = playObj.getString("berat")
                            )
                            players.add(player)
                        }

                        recycler = findViewById(R.id.recycler_players)

                        recycler?.layoutManager = LinearLayoutManager(this)
                        recycler?.adapter = adapter

                        adapter.updateData(players)

                        Log.d("cekisiarray", players.toString())
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
