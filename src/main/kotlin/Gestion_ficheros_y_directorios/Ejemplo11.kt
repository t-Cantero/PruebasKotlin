package org.example.Gestion_ficheros_y_directorios

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val ancho = 200
    val alto = 100
    val imagen = BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB)
// Rellenar la imagen con colores
    for (x in 0 until ancho) {
        for (y in 0 until alto) {
            val rojo = (x * 90) / ancho
            val verde = (y * 90) / alto
            val azul = 205
            val color = Color(rojo, verde, azul)
            imagen.setRGB(x, y, color.rgb)
        }
    }
// Guardar la imagen
    val archivo = File("multimedia/imagen_generada.png")
    ImageIO.write(imagen, "png", archivo)
    println("Imagen generada correctamente: ${archivo.absolutePath}")
}