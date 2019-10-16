package com.anksite.jadwalpelajaran

import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("/anksite/upload/master/jadwal.json")
    fun jadwal(): Call<ResponseBody>
}