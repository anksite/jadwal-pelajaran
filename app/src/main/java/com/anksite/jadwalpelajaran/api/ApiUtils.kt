package com.anksite.jadwalpelajaran

class ApiUtils {
    fun getApiService(): APIService{
        return RetrofitClient().getClient("https://raw.githubusercontent.com").create(APIService::class.java)
    }
}