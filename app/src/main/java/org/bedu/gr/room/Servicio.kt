package org.bedu.gr.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class Servicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val folio_servicio: Int,
    @ColumnInfo val photoType: Int, // 1= vin, 2=placa, 3=vehiculo
    @ColumnInfo val photo: String,
    @ColumnInfo val flag: Boolean = false,
)
