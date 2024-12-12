package util

import javafx.application.Platform
import javafx.scene.control.Alert
import logic.entities.Participante

class NotificationThread(private val usuario: Participante) : Thread() {
    fun exibirNotificacao(message: String) {
        if (usuario == this.usuario) {
            Platform.runLater {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Notificação"
                alert.headerText = null
                alert.contentText = message
                alert.showAndWait()
            }
        }
    }

    override fun run() {
        println("Thread iniciada para o usuário ${usuario.nome}")
    }
}