package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import logic.entities.Participante
import util.CargoEnum
import util.FileUtil
import util.StatusEnum
import util.TurnoEnum
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.time.LocalDate

val usuarioTesteColaborador = Participante(1, "João", "joao123@email.com", "123456", CargoEnum.COLABORADOR)

class MainApp : Application() {
    private lateinit var initialScene: Scene
    private val todosEventos = ListaEventos()
    private val FileUtil = FileUtil()

    private fun loadEventos() {
        val eventos = FileUtil.lerArquivoTxt("src/main/resources/db/eventos.txt")
        for (evento in eventos) {
            val dados = evento.split(",")
            val novoEvento = Evento(
                dados[0].toInt(),
                dados[1],
                dados[2],
                dados[3].toDouble(),
                LocalDate.parse(dados[4]),
                LocalDate.parse(dados[5]),
                StatusEnum.valueOf(dados[6]),
                TurnoEnum.valueOf(dados[7])
            )
            todosEventos.inserirEvento(novoEvento)
        }
    }

    override fun start(primaryStage: Stage) {
        loadEventos()
        val telaGerenciarEventos = TelaGerenciarEventos(primaryStage, this, todosEventos, usuarioTesteColaborador)
        val btnEntrarColab = Button("Entrar como colaborador")
        btnEntrarColab.setOnAction {
            primaryStage.scene = telaGerenciarEventos.gerenciarEventosScene()
        }
        val btnEntrarOuvinte = Button("Entrar como ouvinte")
        btnEntrarOuvinte.setOnAction {
//            telaMenuEventos.menuOuvinteScene()
        }
        val btnEncerrar = Button("Encerrar")
        btnEncerrar.setOnAction {
            primaryStage.close()
        }

        val vbox = VBox(10.0, btnEntrarColab, btnEncerrar)
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






