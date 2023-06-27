package org.bedu.gr.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.bedu.gr.R
import org.bedu.gr.Adapters.RecyclerAdapterServicio
import org.bedu.gr.Api.api
import org.bedu.gr.Models.DatosAPI
import org.bedu.gr.Models.Record
import org.bedu.gr.constantes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class ServiciosFragment : Fragment(R.layout.fragment_servicios) {

    private lateinit var recycler: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val binding = FragmentServiciosBinding.bind(view)

        recycler = view.findViewById(R.id.recyclerServicio)
        //recycler = binding.recyclerServicio

       //Integramos retrofit
        val call = api.endpoint.getServicios( constantes.BIN, constantes.API_KEY)

        call.enqueue(object : Callback<DatosAPI?> {
             override fun onResponse(call: Call<DatosAPI?>, response: Response<DatosAPI?>) {

                 if (response.code() == 200){

                     val body = response.body()
                     Log.i("responseBody",body?.record?.count().toString())
                     body?.let {
                         if (it.record.count()> 0){
                             var servicios: List<Record> = it.record
                             val serviciosfiltered = servicios.filter { it.etapa < 3 }.toList()
                             recycler.adapter = RecyclerAdapterServicio(view.context,serviciosfiltered,1)
                             recycler.addItemDecoration(DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL))

                         }
                         else{
                             Toast.makeText(context, "Ups, no hemos encontrado servicios. " + response.code().toString(), Toast.LENGTH_LONG).show()
                             Log.i("Info","Ups, no hemos encontrado servicios.")
                         }

                     }
                 }
                 else{
                     Toast.makeText(context, "Ups, no encontramos el servicio: " + response.code().toString(), Toast.LENGTH_LONG).show()
                 }

             }

             override fun onFailure(call: Call<DatosAPI?>, t: Throwable) {
                 Toast.makeText(context, "Ups, encontramos un error. ", Toast.LENGTH_LONG).show()
                 Log.i("ERROR",t.toString())
             }


         })


        /*val jsonFileString = loadJSONFromAsset(view.context)
        Log.i("data", jsonFileString)
        val gson = Gson()
        val listServicios = object : TypeToken<List<Record>>() {}.type
        var servicios: List<Record> = gson.fromJson(jsonFileString, listServicios)
        val serviciosfiltered = servicios.filter { it.etapa < 3 }.toList()
        recycler.adapter = RecyclerAdapterServicio(view.context,serviciosfiltered,1)
        recycler.addItemDecoration(DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL))
*/
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_servicios, container, false)
    }



   private fun loadJSONFromAsset(c: Context):String{

        val jsonString: String
        try {
            jsonString = c.assets.open("DATA.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
        return jsonString
    }
}