package Gestión_de_ficheros_y_directorios

import java.nio.file.Path
fun main() {
// Path relativo al directorio del proyecto
    val rutaRelativa: Path = Path.of("documentos", "ejemplo.txt")
// Path absoluto en Windows
    val rutaAbsolutaWin: Path = Path.of("C:", "Users", "Pol", "Documentos")
// Path absoluto en Linux/macOS
    val rutaAbsolutaNix: Path = Path.of("/home/pol/documentos")
    println("Ruta relativa: " + rutaRelativa) // Muestra la ruta relativa
    println("Ruta absoluta: " + rutaRelativa.toAbsolutePath()) // ruta completa
    println("Ruta absoluta: " + rutaAbsolutaWin) // ruta absoluta Windows
    println("Ruta absoluta: " + rutaAbsolutaNix) // ruta absoluta Linux/macOS
}