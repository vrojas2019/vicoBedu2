package org.bedu.gr.room

import androidx.room.*

@Dao
interface IServicioDao {

    @Insert
    fun insertServicio(servicio: Servicio)

    @Update
    fun updateVehicle(servicio: Servicio)

    @Delete
    fun removeVehicle(servicio: Servicio)

    @Query("DELETE FROM Servicio WHERE id=:id")
    fun removeServicioById(id: Int)

    @Query("SELECT * FROM Servicio")
    fun getAll(): List<Servicio>

    @Query("SELECT * FROM Servicio WHERE id = :id")
    fun getServicioById(id: Int): Servicio

    @Query("SELECT * FROM Servicio WHERE folio_servicio = :folio_servicio")
    fun getServicioByFolio(folio_servicio: Int): List<Servicio>
}