package ui

import logic.collections.ListaEventos
import util.enums.CargoEnum
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.database.DatabaseInit
import logic.database.EventoDAO
import logic.database.ParticipanteDAO

class MainApp : Application() {
    private lateinit var initialScene: Scene
    private val eventoDAO = EventoDAO()
    private val participanteDAO = ParticipanteDAO()
    private lateinit var todosEventos: ListaEventos
    private var vbox = VBox(10.0)

    override fun start(primaryStage: Stage) {
        DatabaseInit.init()
        todosEventos = eventoDAO.getEventos()

        val pagelabel = Label("Login")
        val labelUsuario = Label("Usuário: ")
        val inputUsuario = TextField()
        val labelCpf = Label("CPF: ")
        val inputCpf = TextField()
        val btnLogin = Button("Entrar")

        btnLogin.setOnAction {
            val usuario = participanteDAO.getParticipanteNomeCpf(inputUsuario.text, inputCpf.text)
            if (usuario == null) {
                vbox.children.add(Label("Usuário/Cpf inválido"))
            } else {
                inputUsuario.clear()
                inputCpf.clear()
                if (usuario.cargo == CargoEnum.COLABORADOR) {
                    val telaGerenciarEventos = TelaGerenciarEventos(primaryStage, this, todosEventos, usuario)
                    primaryStage.scene = telaGerenciarEventos.gerenciarEventosScene()
                } else {
                    val telaMenuEventos = TelaMenuEventos(primaryStage, this, todosEventos, usuario)
                    primaryStage.scene = telaMenuEventos.menuEventosScene()
                }
            }
        }

        val btnEncerrar = Button("Sair")
        btnEncerrar.setOnAction {
            primaryStage.close()
        }

        vbox = VBox(10.0, pagelabel, labelUsuario, inputUsuario, labelCpf, inputCpf, btnLogin, btnEncerrar)
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






