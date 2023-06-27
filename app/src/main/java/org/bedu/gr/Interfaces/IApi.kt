package org.bedu.gr.Interfaces

import org.bedu.gr.Models.DatosAPI
import retrofit2.Call
import retrofit2.http.*

interface IApi {
    @GET("/v3/b/{bin}")
    fun getServicios(
        @Path("bin") bin:String,
        @Header("X-ACCESS-KEY") key:String,
    ): Call<DatosAPI>


}