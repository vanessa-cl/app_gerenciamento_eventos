package util.ui

import javafx.geometry.Pos
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.Stage
import logic.entities.Participante
import ui.MainApp

class Header(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val usuarioLogado: Participante,
    private val titulo: String
) : HBox() {
    private val btnLogout: Button = Button("Logout")

    init {
        padding = Insets(10.0)
        maxWidth = 1300.0

        val tituloBox = HBox(10.0, Label(titulo)).apply { alignment = Pos.TOP_LEFT }

        val usuarioBox = HBox(
            10.0,
            Label("Usuário: ${usuarioLogado.nome} - ${usuarioLogado.cargo}"),
            btnLogout
        ).apply {
            alignment = Pos.CENTER_RIGHT
        }

        children.addAll(tituloBox, usuarioBox)
        HBox.setHgrow(tituloBox, Priority.ALWAYS)

        btnLogout.setOnAction {
            primaryStage.scene = mainApp.getInitialScene()
        }
    }

    fun getHeader(): HBox {
        return this
    }
}