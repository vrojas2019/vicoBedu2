package org.bedu.gr.Models


data class Record(
    val Asegurado: String,
    val anio: Int,
    val coordenadas: String,
    val etapa: Int,
    val fecha_solicitud: String,
    val folio_servicio: Int,
    val hora_solicitud: String,
    val modelo: String,
    val vehiculo: String
)