package util.ui

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.stage.Stage
import logic.entities.Participante
import ui.MainApp

class Header(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val usuarioLogado: Participante
) : HBox() {
    private val tituloLabel = Label("App de Eventos")
    private val usuarioLogadoLabel = Label(usuarioLogado.nome)
    private val btnLogout: Button = Button("Logout")

    init {
        spacing = 10.0
        children.addAll(tituloLabel, usuarioLogadoLabel, btnLogout)
    }

    fun setLogout(action: () -> Unit) {
        btnLogout.setOnAction {
            primaryStage.scene = mainApp.getInitialScene()
        }
    }
}