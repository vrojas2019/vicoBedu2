package org.bedu.gr.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Servicio::class], version = 1)
abstract class DataBaseVICO : RoomDatabase(){

    abstract fun servicioDao(): IServicioDao

    companion object {
        @Volatile
        private var dbInstance: DataBaseVICO? = null

        private const val DB_NAME = "servicios_db"

        fun getInstance(context: Context) : DataBaseVICO {

            return dbInstance?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseVICO::class.java,
                    DB_NAME
                ).build()
                dbInstance = instance

                instance
            }
        }
    }

}