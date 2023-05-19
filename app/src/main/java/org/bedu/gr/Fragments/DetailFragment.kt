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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bedu.gr.Adapters.RecyclerAdapterServicio
import org.bedu.gr.Models.Servicio
import org.bedu.gr.R
import org.bedu.gr.databinding.FragmentDetailBinding
import java.io.IOException


class DetailFragment : Fragment(R.layout.fragment_detail) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)

        val jsonFileString = loadJSONFromAsset(view.context)

        val gson = Gson()
        val listServicios = object : TypeToken<List<Servicio>>() {}.type
        var servicios: List<Servicio> = gson.fromJson(jsonFileString, listServicios)
        //val servicio = servicios.filter { it.folio_servicio == 3 }.toList().first()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
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