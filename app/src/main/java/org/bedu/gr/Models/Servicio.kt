package org.bedu.gr.Models

data class Servicio(
    var folio_servicio: Int,
    var Asegurado: String,
    var vehiculo: String,
    var modelo: String,
    var anio: Int,
    var fecha_solicitud: String,
    var hora_solicitud: String,
    var etapa: Int,
    var coordenadas: String,
)
