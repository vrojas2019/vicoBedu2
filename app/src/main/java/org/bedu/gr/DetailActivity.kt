package org.bedu.gr

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bedu.gr.Models.Record
import org.bedu.gr.room.Servicio
import org.bedu.gr.databinding.ActivityDetailBinding
import java.io.IOException

class DetailActivity : AppCompatActivity() {

    //Variables globales
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Regresar"


        val extras = intent.extras
        var folioServicio = 0
        if (extras != null) {
            folioServicio = extras.getString("folio")!!.toInt()
        }


        val jsonFileString = loadJSONFromAsset(applicationContext)

        val gson = Gson()
        val listServicios = object : TypeToken<List<Record>>() {}.type
        var servicios: List<Record> = gson.fromJson(jsonFileString, listServicios)
        val servicio = servicios.filter { it.folio_servicio == folioServicio }.toList().first()

        binding.txtFolioServicioDetail.text = servicio.folio_servicio.toString()
        binding.txtAseguradoDetail.text = servicio.Asegurado.toString()
        binding.txtFecha.text = servicio.fecha_solicitud.toString() + " " + servicio.hora_solicitud.toString()
        binding.txtVehiculo.text = "${servicio.vehiculo.toString()} ${servicio.modelo.toString()} ${servicio.anio.toString()}"

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