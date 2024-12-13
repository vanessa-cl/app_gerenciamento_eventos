package util.ui

import javafx.scene.control.Alert

class Alert {
    fun showInfo(titulo: String, cabecalho: String, conteudo: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = titulo
        alert.headerText = cabecalho
        alert.contentText = conteudo
        alert.showAndWait()
    }

    fun showError(titulo: String, cabecalho: String, conteudo: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = titulo
        alert.headerText = cabecalho
        alert.contentText = conteudo
        alert.showAndWait()
    }
}