package com.anksite.jadwalpelajaran

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class ReceiverNotif : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            Intent.ACTION_BOOT_COMPLETED -> {
                setAlarm(context)
            }

            "notif" -> {
                showNotif(context)
            }
        }
    }

    fun setAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, ReceiverNotif::class.java)
        i.action = "notif"
        val pi = PendingIntent.getBroadcast(context, 1000, i, 0)

        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        if (Calendar.getInstance().after(cal)) {
            cal.add(Calendar.HOUR_OF_DAY, 24)
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC, cal.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
    }

    fun showNotif(contex: Context){
        val i = Intent(contex, ActivityMain::class.java)
        val pi = PendingIntent.getActivity(contex, 0, i, PendingIntent.FLAG_CANCEL_CURRENT)

        val listJadwal = getJadwalNow(contex)
        if(listJadwal.size>0){
            val stackNotif = NotificationCompat.InboxStyle()
            for(jdw in listJadwal){
                stackNotif.addLine("${jdw.start} - ${jdw.end}   ${jdw.room}   ${jdw.mapel}")
            }

            var displayDay = ""
            when(listJadwal[0].day){
                "1" -> displayDay = "MINGGU"
                "2" -> displayDay = "SENIN"
                "3" -> displayDay = "SELASA"
                "4" -> displayDay = "RABU"
                "5" -> displayDay = "KAMIS"
                "6" -> displayDay = "JUMAT"
                "7" -> displayDay = "SABTU"
            }
            stackNotif.setSummaryText(displayDay)

            val notifManager = contex.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notifBuilder = NotificationCompat.Builder(contex, "jdw")
                    .setSmallIcon(R.drawable.schedule)
                    .setSound(notifSound)
                    .setVibrate(longArrayOf(0))
                    .setGroup("jdw")
                    .setGroupSummary(true)
                    .setStyle(stackNotif)
                    .setContentIntent(pi)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel("jdw", "jadwal", NotificationManager.IMPORTANCE_DEFAULT)
                channel.vibrationPattern = longArrayOf(0)
                channel.enableVibration(true)

                notifBuilder.setChannelId("jdw")
                notifManager.createNotificationChannel(channel)
            }

            val notif = notifBuilder.build()
            notifManager.notify(1000, notif)
        }
    }

    fun getJadwalNow(context: Context): ArrayList<DataJadwal> {
        val sp = context.getSharedPreferences("data", 0)
        val strJadwal = sp.getString("jadwal", "NULL")
        val jArrayJadwal = JSONArray(strJadwal)

        val cal = Calendar.getInstance()
        val curDay = cal.get(Calendar.DAY_OF_WEEK).toString()

        val listJadwal = ArrayList<DataJadwal>()
        repeat(jArrayJadwal.length()){j ->
            val jObjJadwal= jArrayJadwal.getJSONObject(j)

            val day= jObjJadwal.getString("day")

            if(day==curDay){
                val start   = jObjJadwal.getString("start")
                val end     = jObjJadwal.getString("end")
                val room    = jObjJadwal.getString("room")
                val mapel   = jObjJadwal.getString("mapel")
                val guru    = jObjJadwal.getString("guru")

                val data = DataJadwal(day, start, end, room, mapel, guru)
                listJadwal.add(data)
            }
        }
        return listJadwal
    }
}
