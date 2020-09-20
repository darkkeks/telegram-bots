package ru.darkkeks.telegram.core.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TelegramFiles {

    @GET("{file_path}")
    fun downloadFile(
            @Path("file_path", encoded = true) filePath: String
    ) : Call<ResponseBody>

}
