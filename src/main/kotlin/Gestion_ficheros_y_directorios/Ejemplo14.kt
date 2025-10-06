package org.example.Gestion_ficheros_y_directorios

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun modificarAlturaPlanta(path: Path, idPlanta: Int, nuevaAltura: Double) {
    // Abrimos un canal con permisos de lectura y escritura
    FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE).use { canal ->
        val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
        var encontrado = false

        while (canal.read(buffer) > 0 && !encontrado) {
            /*
             canal.read(buffer) lee un bloque completo de 32 bytes y los guarda en el buffer.
             Después de esta operación, el "puntero" o "cursor" interno del canal (canal.position())
             ha avanzado 32 bytes y ahora se encuentra al final del registro que acabamos de leer.
            */
            val posicionActual = canal.position()
            buffer.flip()

            val id = buffer.getInt()
            if (id == idPlanta) {
                // Hemos encontrado el registro. Calculamos la posición del campo 'altura'.
                // Posición de inicio del registro = (actual - tamaño registro)
                // Luego sumamos 4 bytes (id) + 20 bytes (nombre)
                val posicionAltura = posicionActual - TAMANO_REGISTRO + TAMANO_ID + TAMANO_NOMBRE

                // Creamos un buffer solo para el double
                val bufferAltura = ByteBuffer.allocate(TAMANO_ALTURA)
                bufferAltura.putDouble(nuevaAltura)
                bufferAltura.flip()

                // Escribimos el nuevo valor en la posición exacta del fichero
                canal.write(bufferAltura, posicionAltura)

                encontrado = true
            }

            buffer.clear()
        }

        if (encontrado) {
            println("Altura de la planta con ID $idPlanta modificada a $nuevaAltura")
        } else {
            println("No se encontró la planta con ID $idPlanta")
        }
    }
}
fun main() {
    val archivoPath: Path = Paths.get("plantas.bin")

    // --- Modificar una planta ---
    modificarAlturaPlanta(archivoPath, 2, 5.5)

    // --- Volver a leer para verificar el cambio ---
    val leidasDespuesDeModificar = leerPlantas(archivoPath)

    println("Plantas leídas después de la modificación:")
    for (dato in leidasDespuesDeModificar) {
        println(" - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, Altura: ${dato.altura_maxima} metros")
    }
}
