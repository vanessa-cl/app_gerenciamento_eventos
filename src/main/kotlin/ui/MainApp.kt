package ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.control.Button
import javafx.stage.Stage

class MainApp : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "App de Gerenciamento de Eventos"
        val button = Button("Clique aqui")
        val root = StackPane(button)
        primaryStage.scene = Scene(root, 300.0, 200.0)
        primaryStage.show()
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
