package org.example.Gestion_ficheros_y_directorios

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

data class PlantaBinaria(val id_planta: Int, val nombre_comun: String, val altura_maxima: Double)

// Tamaño fijo para cada registro en el fichero
const val TAMANO_ID = Int.SIZE_BYTES // 4 bytes
const val TAMANO_NOMBRE = 20 // String de tamaño fijo 20 bytes
const val TAMANO_ALTURA = Double.SIZE_BYTES // 8 bytes
const val TAMANO_REGISTRO = TAMANO_ID + TAMANO_NOMBRE + TAMANO_ALTURA

// Función que crea un fichero (si no existe) o lo vacía (si existe)
fun vaciarCrearFichero(path: Path) {
    try {
        // WRITE: permite escritura
        // CREATE: crea el fichero si no existe
        // TRUNCATE_EXISTING: vacía el fichero si ya existía
        FileChannel.open(
            path,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).close()
        println("El fichero '${path.fileName}' existe y está vacío.")
    } catch (e: Exception) {
        println("Error al vaciar o crear el fichero: ${e.message}")
    }
}

// Añadir una planta al final del fichero
fun anadirPlanta(path: Path, idPlanta: Int, nombre: String, altura: Double) {
    val nuevaPlanta = PlantaBinaria(idPlanta, nombre, altura)
    try {
        FileChannel.open(
            path,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)

            // Llenamos el buffer con los datos de la planta
            buffer.putInt(nuevaPlanta.id_planta)
            val nombreBytes = nuevaPlanta.nombre_comun
                .padEnd(20, ' ')
                .toByteArray(Charset.defaultCharset())
            buffer.put(nombreBytes, 0, 20)
            buffer.putDouble(nuevaPlanta.altura_maxima)

            buffer.flip()
            while (buffer.hasRemaining()) {
                canal.write(buffer)
            }
            println("Planta '${nuevaPlanta.nombre_comun}' añadida con éxito.")
        }
    } catch (e: Exception) {
        println("Error al añadir la planta: ${e.message}")
    }
}

// Leer todas las plantas del fichero
fun leerPlantas(path: Path): List<PlantaBinaria> {
    val plantas = mutableListOf<PlantaBinaria>()
    try {
        FileChannel.open(path, StandardOpenOption.READ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
            while (canal.read(buffer) > 0) {
                buffer.flip()

                val id = buffer.getInt()
                val nombreBytes = ByteArray(TAMANO_NOMBRE)
                buffer.get(nombreBytes)
                val nombre = String(nombreBytes, Charset.defaultCharset()).trim()
                val altura = buffer.getDouble()

                plantas.add(PlantaBinaria(id, nombre, altura))
                buffer.clear()
            }
        }
    } catch (e: Exception) {
        println("Error al leer el fichero: ${e.message}")
    }
    return plantas
}

fun main() {
    val archivoPath: Path = Paths.get("plantas.bin")

    val lista = listOf(
        PlantaBinaria(1, "Rosa", 1.5),
        PlantaBinaria(2, "Girasol", 3.0),
        PlantaBinaria(3, "Margarita", 0.6)
    )

    // Vaciar o crear el fichero
    vaciarCrearFichero(archivoPath)

    // Añadir las plantas al fichero
    lista.forEach { planta ->
        anadirPlanta(archivoPath, planta.id_planta, planta.nombre_comun, planta.altura_maxima)
    }

    // Leer el fichero binario
    val leidas = leerPlantas(archivoPath)
    println("Plantas leídas del fichero:")
    for (dato in leidas) {
        println(" - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, Altura: ${dato.altura_maxima} metros")
    }
}
