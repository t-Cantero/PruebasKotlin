package org.example.Gestion_ficheros_y_directorios

import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
fun main() {
    val originalPath = Path.of("multimedia/png/faro.png")
    val copiaPath = Path.of("multimedia/png/faro_copia.png")
    val grisPath = Path.of("multimedia/png/faro_escala_de_grises.png")
// 1. Comprobar si la imagen existe
    if (!Files.exists(originalPath)) {
        println("No se encuentra la imagen original: $originalPath")
    } else {
// 2. Copiar la imagen con java.nio (para no modificar el original)
        Files.copy(originalPath, copiaPath, StandardCopyOption.REPLACE_EXISTING)
        println("Imagen copiada a: $copiaPath")
// 3. Leer la imagen en un objeto BufferedImage
        val imagen: BufferedImage = ImageIO.read(copiaPath.toFile())
        // 4. Convertir a escala de grises, píxel por píxel
        for (x in 0 until imagen.width) {
            for (y in 0 until imagen.height) {
// Obtenemos el color del píxel actual.
                val color = Color(imagen.getRGB(x, y))
                /* Calcular el valor de gris usando la fórmula de luminosidad.
                Esta fórmula pondera los colores rojo, verde y azul según la
                sensibilidad del ojo humano.
                El resultado es un único valor de brillo que convertimos a entero.
                */
                val gris = (color.red * 0.299 + color.green * 0.587 + color.blue *
                        0.114).toInt()
// Creamos un nuevo color donde los componentes rojo, verde y azul
// son todos iguales al valor de 'gris' que hemos calculado.
                val colorGris = Color(gris, gris, gris)
// Establecemos el nuevo color gris en el píxel de la imagen.
                imagen.setRGB(x, y, colorGris.rgb)
            }
        }
// 5. Guardar la imagen modificada
// Usamos "png" porque es un formato sin pérdida, ideal para imágenes
        ImageIO.write(imagen, "png", grisPath.toFile())
        println("Imagen convertida a escala de grises y guardada como: $grisPath")
    }
}