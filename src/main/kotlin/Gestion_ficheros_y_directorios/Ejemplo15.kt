package org.example.Gestion_ficheros_y_directorios

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

fun eliminarPlanta(path: Path, idPlanta: Int) {
    // Creamos un fichero temporal en el mismo directorio
    val pathTemporal = Paths.get(path.toString() + ".tmp")
    var plantaEncontrada = false

    // Abrimos el canal de lectura para el fichero original
    FileChannel.open(path, StandardOpenOption.READ).use { canalLectura ->
        // Abrimos el canal de escritura para el fichero temporal
        FileChannel.open(pathTemporal, StandardOpenOption.WRITE, StandardOpenOption.CREATE).use { canalEscritura ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)

            // Leemos el fichero original registro a registro
            while (canalLectura.read(buffer) > 0) {
                buffer.flip()
                val id = buffer.getInt() // Solo necesitamos el ID

                if (id == idPlanta) {
                    plantaEncontrada = true
                    // Si es la planta a eliminar, no escribimos este registro
                } else {
                    // Si NO es la planta a eliminar, escribimos el registro completo en el fichero temporal
                    buffer.rewind() // Volvemos al inicio del buffer
                    canalEscritura.write(buffer)
                }

                buffer.clear() // Preparamos el buffer para la siguiente lectura
            }
        }
    }

    if (plantaEncontrada) {
        // Si se encontró y eliminó, reemplazar fichero original con el temporal
        Files.move(pathTemporal, path, StandardCopyOption.REPLACE_EXISTING)
        println("Planta con ID $idPlanta eliminada con éxito.")
    } else {
        // Si no se encontró, eliminamos el temporal
        Files.delete(pathTemporal)
        println("No se encontró ninguna planta con ID $idPlanta.")
    }
}

fun main() {
    val archivoPath: Path = Paths.get("plantas.bin")

    // Eliminar una planta (por ejemplo, la de ID 3)
    eliminarPlanta(archivoPath, 3)

    // Volver a leer para verificar los cambios
    val leidas = leerPlantas(archivoPath)
    println("Plantas después de eliminar:")
    for (dato in leidas) {
        println(" - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, Altura: ${dato.altura_maxima} metros")
    }
}
