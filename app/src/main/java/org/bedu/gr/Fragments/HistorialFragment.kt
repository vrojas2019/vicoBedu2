package org.bedu.gr.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bedu.gr.Adapters.RecyclerAdapterServicio
import org.bedu.gr.Models.Servicio
import org.bedu.gr.R
import org.bedu.gr.databinding.FragmentHistorialBinding
import java.io.IOException


class HistorialFragment : Fragment(R.layout.fragment_historial) {
    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHistorialBinding.bind(view)

        //recycler = view.findViewById(R.id.recyclerServicio)
        recycler = binding.recyclerHistorial
        val jsonFileString = loadJSONFromAsset(view.context)
        Log.i("data", jsonFileString)

        val gson = Gson()
        val listServicios = object : TypeToken<List<Servicio>>() {}.type
        var servicios: List<Servicio> = gson.fromJson(jsonFileString, listServicios)
        val serviciosfiltered = servicios.filter { it.etapa == 3 }.toList()

        recycler.adapter = RecyclerAdapterServicio(view.context,serviciosfiltered,2)

        recycler.addItemDecoration(DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL))

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false)
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