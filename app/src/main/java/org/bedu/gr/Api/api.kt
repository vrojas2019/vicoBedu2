package org.bedu.gr.Api

import org.bedu.gr.Interfaces.IApi
import org.bedu.gr.constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object api {

    private val retrofit = Retrofit.Builder()
        .baseUrl(constantes.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val endpoint = retrofit.create(IApi::class.java)

}