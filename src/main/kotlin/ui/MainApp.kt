package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import logic.entities.Participante
import util.CargoEnum
import java.time.LocalDate

//import javafx.application.Application
//import javafx.scene.Scene
//import javafx.scene.layout.StackPane
//import javafx.scene.control.Button
//import javafx.stage.Stage

//class MainApp : Application() {
//    override fun start(primaryStage: Stage) {
//        primaryStage.title = "App de Gerenciamento de Eventos"
//        val button = Button("Clique aqui")
//        val root = StackPane(button)
//        primaryStage.scene = Scene(root, 300.0, 200.0)
//        primaryStage.show()
//    }
//}

val usuarioTesteColaborador = Participante(1, "João", "joao123@email.com", "123456", CargoEnum.COLABORADOR)
val usuarioTesteOuvinte = Participante(2, "Alana", "alana321@email.com", "654321", CargoEnum.OUVINTE)

fun main() {
    val menuEventos = MenuEventos()
    var opcaoLogin: Int
    var encerrar = false
    while (!encerrar) {
        println("Digite 1 para colaborador, 2 para ouvinte ou 3 para encerrar:")
        opcaoLogin = readln().toInt()
        val usuario = if (opcaoLogin == 1) usuarioTesteColaborador else usuarioTesteOuvinte
        when (opcaoLogin) {
            1 -> menuEventos.mostrarMenuColaborador()
            2 -> menuEventos.mostrarMenuOuvinte(usuario)
            3 -> encerrar = true
            else -> println("Opção inválida! Tente novamente")
        }
    }
}

