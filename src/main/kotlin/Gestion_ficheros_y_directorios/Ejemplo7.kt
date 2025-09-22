package org.example.Gestion_ficheros_y_directorios

import java.nio.file.Path
import java.io.File

// Anotaciones y clases de la librería Jackson para el mapeo a XML.
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

/* Representa la estructura de una única planta.
   La propiedad 'id_planta' será la etiqueta <id_planta>...</id_planta> (así todas) */
data class Planta(
    @JacksonXmlProperty(localName = "id_planta")
    val id_planta: Int,

    @JacksonXmlProperty(localName = "nombre_comun")
    val nombre_comun: String,

    @JacksonXmlProperty(localName = "nombre_cientifico")
    val nombre_cientifico: String,

    @JacksonXmlProperty(localName = "frecuencia_riego")
    val frecuencia_riego: Int,

    @JacksonXmlProperty(localName = "altura_maxima")
    val altura_maxima: Double
)

// Nombre del elemento raíz
@JacksonXmlRootElement(localName = "plantas")
// Data class que representa el elemento raíz del XML.
data class Plantas(
    @JacksonXmlElementWrapper(useWrapping = false) // No necesitamos la etiqueta <plantas> aquí
    @JacksonXmlProperty(localName = "planta")
    val listaPlantas: List<Planta> = emptyList()
)

fun main() {
    val entradaXML = Path.of("datos_ini/mis_plantas.xml")
    val salidaXML = Path.of("datos_ini/mis_plantas2.xml")

    val datos: List<Planta> = leerDatosInicialesXML(entradaXML)

    for (dato in datos) {
        println(
            " - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, " +
                    "Nombre científico: ${dato.nombre_cientifico}, " +
                    "Frecuencia de riego: ${dato.frecuencia_riego} días, " +
                    "Altura: ${dato.altura_maxima} metros"
        )
    }

    escribirDatosXML(salidaXML, datos)
}

fun leerDatosInicialesXML(ruta: Path): List<Planta> {
    val fichero: File = ruta.toFile()

    // Deserializar el XML a objetos Kotlin
    val xmlMapper = XmlMapper().registerKotlinModule()

    // 'readValue' convierte el contenido XML en una instancia de la clase 'Plantas'
    val plantasWrapper: Plantas = xmlMapper.readValue(fichero)

    return plantasWrapper.listaPlantas
}

fun escribirDatosXML(ruta: Path, plantas: List<Planta>) {
    try {
        val fichero: File = ruta.toFile()

        // Creamos instancia de la clase 'Plantas' (raíz del XML).
        val contenedorXml = Plantas(plantas)

        // Configuramos el 'XmlMapper' (motor de Jackson) para la conversión a XML.
        val xmlMapper = XmlMapper().registerKotlinModule()

        // Convertimos 'contenedorXml' en un String con formato XML.
        // .writerWithDefaultPrettyPrinter() formatea con indentación y saltos de línea
        val xmlString =
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedorXml)

        // Escribir un String en un fichero con 'writeText'
        fichero.writeText(xmlString)
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
