package ui

import logic.collections.ListaEventos
import util.enums.CargoEnum
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import javafx.scene.layout.HBox
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
    private val root = VBox()
    private val loginVbox = VBox()

    override fun start(primaryStage: Stage) {
        DatabaseInit.init()
        todosEventos = eventoDAO.getEventos()

        val inputUsuario = TextField()
        inputUsuario.maxWidth = 300.0
        val inputCpf = TextField()
        inputCpf.maxWidth = 300.0
        val btnLogin = Button("Entrar")
        btnLogin.setOnAction {
            val usuario = participanteDAO.getParticipanteNomeCpf(inputUsuario.text, inputCpf.text)
            if (usuario == null) {
                vbox.children.add(Label("Usuário/Cpf inválido"))
            } else {
                inputUsuario.clear()
                inputCpf.clear()
                if (usuario.cargo == CargoEnum.COLABORADOR) {
                    val telaGerenciarEventos = TelaGerenciarEventos(primaryStage, this, root, todosEventos, usuario)
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

        val usuarioBox = VBox(5.0, Label("Usuário:"), inputUsuario).apply {
            alignment = Pos.TOP_LEFT
        }
        val cpfBox = VBox(5.0, Label("CPF:"), inputCpf).apply {
            alignment = Pos.TOP_LEFT
        }
        val formBox = VBox(10.0, usuarioBox, cpfBox).apply {
            alignment = Pos.CENTER_LEFT
        }

        loginVbox.children.addAll(
            Label("Bem-vindo ao App de Gerenciamento de Eventos!"),
            Label("Por favor, faça login para continuar:"),
            formBox,
            HBox(10.0, btnLogin, btnEncerrar).apply { alignment = Pos.CENTER }
        )
        loginVbox.apply { alignment = Pos.CENTER; spacing = 10.0; maxWidth = 300.0 }

        root.children.addAll(
            loginVbox
        )
        root.apply { alignment = Pos.TOP_CENTER; spacing = 10.0; padding = Insets(20.0, 0.0, 0.0, 0.0) }
        // TODO: corrigir importação de arquivo de estilo
//        val resource = javaClass.getResource("/MainApp/main.css")
//        if (resource != null) {
//            println("Arquivo de estilo encontrado: ${resource.path}")
//            initialScene.stylesheets.add(resource.toExternalForm())
//        } else {
//            println("Erro ao carregar o arquivo de estilo")
//        }

        initialScene = Scene(root, 1000.0, 600.0)
        primaryStage.title = "Login"
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






