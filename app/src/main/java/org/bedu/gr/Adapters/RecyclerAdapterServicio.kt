package org.bedu.gr.Adapters


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.bedu.gr.DetailActivity
import org.bedu.gr.Models.Servicio
import org.bedu.gr.R


class RecyclerAdapterServicio(val c: Context,val servicios: List<Servicio>, val tipo:Int) : RecyclerView.Adapter<RecyclerAdapterServicio.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)

        if (tipo ==2)
           view = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio_historial, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servicio = servicios[position]
        holder.bind(servicio)

        holder.itemView.setOnClickListener(View.OnClickListener {

            //Toast.makeText(c, servicio.folio_servicio.toString(), Toast.LENGTH_SHORT).show()

            /*val activity = c as AppCompatActivity
            val fragment = DetailFragment()
            c.supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment,fragment)
                commit()
            }*/

            val intent = Intent(c, DetailActivity::class.java)
            intent.putExtra("folio",servicio.folio_servicio.toString())
            c.startActivity(intent)

        })


    }

    override fun getItemCount(): Int {
        return servicios.size
    }



    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val imgvetapa1 = view.findViewById<ImageView>(R.id.imgvetapa1)
        private val imgvetapa2 = view.findViewById<ImageView>(R.id.imgvetapa2)
        private val imgvetapa3 = view.findViewById<ImageView>(R.id.imgvetapa3)
        private val folio_servicio = view.findViewById<TextView>(R.id.lblFolio)
        private val asegurado = view.findViewById<TextView>(R.id.lblAsegurado)
        private val vehiculo = view.findViewById<TextView>(R.id.lblVehiculo)
        private val fecha = view.findViewById<TextView>(R.id.lblFecha)
        private var etapa: Int = 0
        private var coordenadas: String = ""
        lateinit var optionsMenu : ImageButton

        init {
            if (tipo ==1){
                optionsMenu = view.findViewById(R.id.imgbMenuPopup)
                optionsMenu.setOnClickListener { popupMenus(it) }
            }

        }

        private fun popupMenus(v : View) {
            val position = servicios[adapterPosition]

                val popupMenus = PopupMenu(c,v)
                popupMenus.inflate(R.menu.popup_menu_servicio)
                popupMenus.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.action_popup_fotos ->{

                            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            gallery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            val origin = c as Activity
                            startActivityForResult(origin,gallery,1000,null)

                            //Toast.makeText(c,"Cargar fotos " + position.folio_servicio,Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.action_popup_ubicacion ->{
                            coordenadas = position.coordenadas
                            val intentUri = Uri.parse("google.navigation:q=$coordenadas")
                            val intent = Intent(Intent.ACTION_VIEW, intentUri)
                            intent.setPackage("com.google.android.apps.maps")
                            startActivity(c,intent,null)
                            true
                        }
                        else-> true
                    }

                }

                popupMenus.show()

                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenus)
                menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                    .invoke(menu,true)
        }
        fun bind(servicio: Servicio){
            folio_servicio.text = servicio.folio_servicio.toString()
            asegurado.text = servicio.Asegurado.toString()
            vehiculo.text = servicio.vehiculo.toString() + " " + servicio.modelo.toString() + " " + servicio.anio.toString()
            fecha.text = servicio.fecha_solicitud.toString() + " " + servicio.hora_solicitud.toString()
            etapa = servicio.etapa
            coordenadas = servicio.coordenadas

            when (etapa) {
                1 -> {
                    imgvetapa1.visibility = View.VISIBLE
                    imgvetapa2.visibility = View.INVISIBLE
                    imgvetapa3.visibility = View.INVISIBLE
                }
                2 -> {
                    imgvetapa1.visibility = View.INVISIBLE
                    imgvetapa2.visibility = View.VISIBLE
                    imgvetapa3.visibility = View.INVISIBLE
                }
                3 -> {
                    imgvetapa1.visibility = View.INVISIBLE
                    imgvetapa2.visibility = View.INVISIBLE
                    imgvetapa3.visibility = View.VISIBLE
                }
                else -> {
                    Log.i("INFO", "Opción no disponible")
                    imgvetapa1.visibility = View.VISIBLE
                    imgvetapa2.visibility = View.INVISIBLE
                    imgvetapa3.visibility = View.INVISIBLE
                }
            }
        }
    }

}