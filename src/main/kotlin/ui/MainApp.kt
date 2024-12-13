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
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.database.DatabaseInit
import logic.database.EventoDAO
import logic.database.ParticipanteDAO
import util.ui.Alert

class MainApp : Application() {
    private lateinit var initialScene: Scene
    private val eventoDAO = EventoDAO()
    private val participanteDAO = ParticipanteDAO()
    private lateinit var todosEventos: ListaEventos
    private val root = VBox()
    private val alert = Alert()

    override fun start(primaryStage: Stage) {
        val loginVbox = VBox()
        DatabaseInit.init()
        todosEventos = eventoDAO.getEventos()

        val inputEmail = TextField()
        val inputSenha = TextField()
        val emailBox = VBox(5.0, Label("Email:"), inputEmail).apply {
            alignment = Pos.TOP_LEFT; maxWidth = 300.0
        }
        val senhaBox = VBox(5.0, Label("Senha:"), inputSenha).apply {
            alignment = Pos.TOP_LEFT; maxWidth = 300.0
        }
        val formLoginVbox = VBox(10.0, emailBox, senhaBox).apply {
            alignment = Pos.CENTER_LEFT; maxWidth = 300.0
        }

        val btnLogin = Button("Entrar")
        btnLogin.isDisable = true
        val validarCampos = {
            btnLogin.isDisable = inputEmail.text.isEmpty() ||
                    inputSenha.text.isEmpty()
        }
        inputEmail.textProperty().addListener { _, _, _ -> validarCampos() }
        inputSenha.textProperty().addListener { _, _, _ -> validarCampos() }


        btnLogin.setOnAction {
            val usuario = participanteDAO.getParticipanteEmail(inputEmail.text, inputSenha.text)
            if (usuario == null) {
                alert.showError(
                    "Falha na autenticação",
                    "Erro!",
                    "Verifique seu email/senha e tente novamente."
                )
            } else {
                inputEmail.clear()
                inputSenha.clear()
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

        loginVbox.children.addAll(
            Label("Bem-vindo ao App de Gerenciamento de Eventos!"),
            Label("Por favor, faça login para continuar:"),
            formLoginVbox,
            HBox(10.0, btnLogin, btnEncerrar).apply { alignment = Pos.CENTER }
        )
        loginVbox.apply { alignment = Pos.CENTER; spacing = 10.0; maxWidth = 300.0 }

        root.children.addAll(
            loginVbox
        )
        root.apply { alignment = Pos.TOP_CENTER; spacing = 10.0; padding = Insets(20.0, 0.0, 0.0, 0.0) }

        initialScene = Scene(root, 900.0, 400.0)
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






