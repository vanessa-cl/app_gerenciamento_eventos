package ui

import logic.collections.ListaEventos
import logic.entities.Participante
import util.enums.CargoEnum
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.database.DatabaseInit
import logic.database.EventoDAO

val usuarioTesteColaborador = Participante(1, "João", "joao123@email.com", "12345678900", CargoEnum.COLABORADOR)
val usuarioTesteOuvinte = Participante(2, "Ana", "ana321@email.com", "00987654321", CargoEnum.OUVINTE)

class MainApp : Application() {
    private lateinit var initialScene: Scene
    private val eventoDAO = EventoDAO()
    private lateinit var todosEventos: ListaEventos


    override fun start(primaryStage: Stage) {
        DatabaseInit.init()

        todosEventos = eventoDAO.getEventos()

        val btnEntrarColab = Button("Entrar como colaborador")
        btnEntrarColab.setOnAction {
            val telaGerenciarEventos = TelaGerenciarEventos(primaryStage, this, todosEventos, usuarioTesteColaborador)
            primaryStage.scene = telaGerenciarEventos.gerenciarEventosScene()
        }

        val btnEntrarOuvinte = Button("Entrar como ouvinte")
        btnEntrarOuvinte.setOnAction {
            val telaMenuEventos = TelaMenuEventos(primaryStage, this, todosEventos, usuarioTesteOuvinte)
            primaryStage.scene = telaMenuEventos.menuEventosScene()
        }

        val btnEncerrar = Button("Encerrar")
        btnEncerrar.setOnAction {
            primaryStage.close()
        }

        val vbox = VBox(10.0, btnEntrarColab, btnEntrarOuvinte, btnEncerrar)
        val titledPane = TitledPane("App de Gerenciamento de Eventos", vbox)

        initialScene = Scene(titledPane, 1000.0, 600.0)

        // TODO: corrigir importação de arquivo de estilo
        try {
            val resource = javaClass.getResource("./styles/Style.css")
            initialScene.stylesheets.add(resource!!.toExternalForm())
        } catch (e: Exception) {
            println("Erro ao carregar o arquivo de estilo")
        }
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






