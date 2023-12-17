package com.example.determinemostimprovebasketballplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.example.determinemostimprovebasketballplayer.event.Event
import com.example.determinemostimprovebasketballplayer.event.EventAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class HomeFragment : Fragment() {
    var events:ArrayList<Event> = ArrayList()

    private var recycler: RecyclerView? = null

    private val adapter = EventAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cari FAB berdasarkan ID
        val fabButton: FloatingActionButton = view.findViewById(R.id.fab_add_event)

        // Set listener untuk FAB
        fabButton.setOnClickListener {
            // Ketika FAB ditekan, buka AddTeamActivity
            val intent = Intent(activity, AddEventActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val q = Volley.newRequestQueue(activity)
        val url = "http://192.168.126.86/ta_160419129/get_event.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")

                    for(i in 0 until data.length()) {
                        val playObj = data.getJSONObject(i)
                        val event = Event(
                            playObj.getString("id"),
                            playObj.getString("nama"),
                            playObj.getString("tahun")
                        )
                        events.add(event)
                    }

                    recycler = view.findViewById(R.id.recycler_home)

                    recycler?.layoutManager = LinearLayoutManager(requireContext())
                    recycler?.adapter = this.adapter

                    this.adapter.updateData(events)

                    Log.d("cekisiarray", events.toString())
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)

        return view
    }


}