package util

import logic.structures.ListaEstatica
import java.io.File

class FileUtil() {

    fun lerArquivoTxt(path: String): Array<String> {
        val file = File(path)
        val list = file.readLines()
        val array: Array<String> = list.toTypedArray()
        return array
    }

    fun escreverArquivoTxt(path: String, content: String) {
        val file = File(path)
        file.writeText(content)
    }

    fun anexarArquivoTxt(path: String, content: String) {
        val file = File(path)
        file.appendText(content + System.lineSeparator())
    }
}