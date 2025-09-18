package org.example.Gestion_ficheros_y_directorios

import java.nio.file.Path
import java.nio.file.Files

fun main() {
    val datosIni = Path.of("datos_ini")
    val datosFin = Path.of("datos_fin")
    val ficheroDatosIni = datosIni.resolve("mis_datos.json")

    try {
        // Comprobar y crear carpeta datos_ini
        if (Files.notExists(datosIni)) {
            Files.createDirectory(datosIni)
            println("Carpeta datos_ini creada")
        } else {
            println("Carpeta datos_ini encontrada")
        }

        // Comprobar y crear carpeta datos_fin
        if (Files.notExists(datosFin)) {
            Files.createDirectory(datosFin)
            println("Carpeta datos_fin creada")
        } else {
            println("Carpeta datos_fin encontrada")
        }

        // Comprobar existencia del fichero en datos_ini
        if (Files.exists(ficheroDatosIni)) {
            println("El fichero ${ficheroDatosIni.fileName} existe en datos_ini.")
        } else {
            println("El fichero ${ficheroDatosIni.fileName} NO existe en datos_ini.")
        }

    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
