package com.example.determinemostimprovebasketballplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.determinemostimprovebasketballplayer.statistic.Statistic
import com.example.determinemostimprovebasketballplayer.statistic.StatisticAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class StatisticFragment : Fragment() {
    var statistics:ArrayList<Statistic> = ArrayList()

    private var recycler: RecyclerView? = null

    private val adapter = StatisticAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        val q = Volley.newRequestQueue(activity)
        val url = "http://192.168.126.86/ta_160419129/list_statistik.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresultstatistik", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")

                    for(i in 0 until data.length()) {
                        val playObj = data.getJSONObject(i)
                        val statistic = Statistic(
                            id = playObj.getString("id"),
                            ppg = playObj.getString("ppg").toFloat(),
                            apg = playObj.getString("apg").toFloat(),
                            rpg = playObj.getString("rpg").toFloat(),
                            spg = playObj.getString("spg").toFloat(),
                            bpg = playObj.getString("bpg").toFloat(),
                            name_player = playObj.getString("name_player")
                        )
                        statistics.add(statistic)
                        Log.d("masuk", it)
                    }

                    recycler = view.findViewById(R.id.table_recycler_view)

                    recycler?.layoutManager = LinearLayoutManager(requireContext())
                    recycler?.adapter = this.adapter

                    this.adapter.updateData(statistics)

                    Log.d("cekisiarray", statistics.toString())
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)

        return view
    }
}