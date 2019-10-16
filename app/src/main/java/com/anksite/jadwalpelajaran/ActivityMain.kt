package com.anksite.jadwalpelajaran

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ActivityMain : AppCompatActivity() {
    val TAG = "ActivityMain"
    val listJadwal = ArrayList<DataJadwal>()
    lateinit var mAdapter: RecAdaptJadwal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = RecAdaptJadwal(listJadwal)
        rv_jadwal.layoutManager = LinearLayoutManager(this)
        rv_jadwal.adapter = mAdapter
        showJadwal()
        ReceiverNotif().setAlarm(this)
        if(readJadwal()!="NULL"){
            ReceiverNotif().showNotif(this)
        }
    }

    fun showJadwal(){
        val strJadwal = readJadwal()
        if(strJadwal!="NULL"){
            val jArrayJadwal = JSONArray(strJadwal)

            var saveDay = ""

            val cal = Calendar.getInstance()
            val curDay   = cal.get(Calendar.DAY_OF_WEEK)
            var frontIndex = 0

            repeat(jArrayJadwal.length()){i ->
                val jObjJadwal= jArrayJadwal.getJSONObject(i)

                var day= jObjJadwal.getString("day")
                val start   = jObjJadwal.getString("start")
                val end     = jObjJadwal.getString("end")
                val room    = jObjJadwal.getString("room")
                val mapel   = jObjJadwal.getString("mapel")
                val guru    = jObjJadwal.getString("guru")

                val data : DataJadwal

                if(saveDay.contains(",$day")){
                    data = DataJadwal(day+"#", start, end, room, mapel, guru)
                }else{
                    data = DataJadwal(day, start, end, room, mapel, guru)
                    saveDay = "$saveDay,$day"
                }

                if(day.toInt()<curDay){
                    listJadwal.add(data)
                }else{
                    listJadwal.add(frontIndex, data)
                    ++frontIndex
                }
            }
            mAdapter.notifyDataSetChanged()
        }else{
            loadJadwal()
        }
    }

    //https://raw.githubusercontent.com/anksite/upload/master/jadwal.json
    fun loadJadwal(){
        pb_load.visibility = View.VISIBLE
        ApiUtils().getApiService().jadwal().enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                pb_load.visibility = View.GONE
                val jadwal = response.body()?.string()
                if(jadwal!=null) saveJadwal(jadwal)
                listJadwal.clear()
                showJadwal()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                pb_load.visibility = View.GONE
                Toast.makeText(applicationContext, "Load Error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    fun saveJadwal(jadwal: String){
        val sp = getSharedPreferences("data", 0)
        sp.edit().putString("jadwal", jadwal).apply()
    }

    fun readJadwal(): String{
        val sp = getSharedPreferences("data", 0)
        val jadwal = sp.getString("jadwal", "NULL")
        return jadwal?:"NULL"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_awal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_load -> {
                loadJadwal()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
