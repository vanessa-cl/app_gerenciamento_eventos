package ui

import logic.entities.Participante
import util.CargoEnum

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

class MainApp : Application() {
    private lateinit var initialScene: Scene

    override fun start(primaryStage: Stage) {
        val telaMenuEventos = TelaMenuEventos(primaryStage, this)
        val btnEntrar = Button("Entrar")
        btnEntrar.setOnAction {
            telaMenuEventos.menuColaboradorScene()
        }
        val btnEncerrar = Button("Encerrar")
        btnEncerrar.setOnAction {
            primaryStage.close()
        }

        val vbox = VBox(10.0, btnEntrar, btnEncerrar)
        val titledPane = TitledPane("App de Gerenciamento de Eventos", vbox)

        initialScene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "HomePage"
        primaryStage.scene = initialScene
        primaryStage.show()
    }


    fun getInitialScene(): Scene {
        return initialScene
    }
}

fun main(args: Array<String>) {
    launch(MainApp::class.java, *args)
}


//val usuarioTesteColaborador = Participante(1, "João", "joao123@email.com", "123456", CargoEnum.COLABORADOR)
//val usuarioTesteOuvinte = Participante(2, "Alana", "alana321@email.com", "654321", CargoEnum.OUVINTE)
//
//fun main() {
//    val menuEventos = MenuEventos()
//    var opcaoLogin: Int
//    var encerrar = false
//    while (!encerrar) {
//        println("Digite 1 para colaborador, 2 para ouvinte ou 3 para encerrar:")
//        opcaoLogin = readln().toInt()
//        val usuario = if (opcaoLogin == 1) usuarioTesteColaborador else usuarioTesteOuvinte
//        when (opcaoLogin) {
//            1 -> menuEventos.mostrarMenuColaborador()
//            2 -> menuEventos.mostrarMenuOuvinte(usuario)
//            3 -> encerrar = true
//            else -> println("Opção inválida! Tente novamente")
//        }
//    }
//}






