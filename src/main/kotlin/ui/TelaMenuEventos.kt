package ui

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.DatePicker
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.collections.ListaEventos
import logic.entities.Evento
import util.SequentialId
import util.Tables

class TelaMenuEventos(private val primaryStage: Stage, private val mainApp: MainApp) : StackPane() {
    private var todosEventos = ListaEventos()
    private var id = SequentialId()
    private var tables = Tables()

    fun menuColaboradorScene(): Scene {
        val btnCadastro = Button("Cadastrar novo evento")
        btnCadastro.setOnAction {
            primaryStage.scene = cadastrarEvento()
        }
        val btnLista = Button("Listar eventos")
        btnLista.setOnAction {
            primaryStage.scene = exibirEventos()
        }
        val btnGerenciaPalestras = Button("Gerenciar palestras")
        btnGerenciaPalestras.setOnAction {
            println("Gerenciar palestras")
        }
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = mainApp.getInitialScene()
        }
        val vbox = VBox(
            10.0, btnCadastro,
            btnLista,
            btnGerenciaPalestras,
            btnVoltar
        )
        val titledPane = TitledPane("Gerenciar Eventos", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)

        primaryStage.title = "Gerenciar Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    fun cadastrarEvento(): Scene {
        val vbox = VBox(10.0)
        val labelNome = Label("Nome do evento:")
        val inputNome = TextField()
        val labelDataInicio = Label("Data de início:")
        val inputDataInicio = DatePicker()
        val labelDataTermino = Label("Data de término:")
        val inputDataTermino = DatePicker()
        val labelDescricao = Label("Descrição:")
        val inputDescricao = TextField()
        val labelValorInscricao = Label("Valor da inscrição (Ex: R$0.00):")
        val inputValorInscricao = TextField()

        val btnConfirmar = Button("Cadastrar")
        btnConfirmar.setOnAction {
            val inputNomeContent = inputNome.text
            val inputDataInicioContent = inputDataInicio.value
            val inputDataTerminoContent = inputDataTermino.value
            val inputDescricaoContent = inputDescricao.text
            val inputValorInscricaoContent = inputValorInscricao.text

            val novoEvento =
                Evento(
                    id.gerarId(),
                    inputNomeContent,
                    inputDescricaoContent,
                    inputValorInscricaoContent.toDouble(),
                    inputDataInicioContent,
                    inputDataTerminoContent
                )
            println(novoEvento.getNome())
            todosEventos.inserirEvento(novoEvento)
            println("Evento cadastrado com sucesso!")
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = menuColaboradorScene()
        }
        vbox.children.addAll(
            labelNome,
            inputNome,
            labelDataInicio,
            inputDataInicio,
            labelDataTermino,
            inputDataTermino,
            labelDescricao,
            inputDescricao,
            labelValorInscricao,
            inputValorInscricao,
            btnConfirmar,
            btnVoltar
        )
        val titledPane = TitledPane("Cadastrar novo evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)

        return scene
    }

    fun exibirEventos(): Scene {
        var resultado: Label
        val vbox = VBox(10.0)
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = menuColaboradorScene()
        }
        val eventos = todosEventos.buscarTodosEventos()
        if (eventos == null) {
            resultado = Label("Não há eventos cadastrados!")
            vbox.children.addAll(
                resultado,
                btnVoltar
            )
            val titledPane = TitledPane("Consultar eventos", vbox)
            val scene = Scene(titledPane, 1000.0, 600.0)
            return scene
        }

        val table = tables.eventosTable(eventos)
        vbox.children.addAll(
            table,
            btnVoltar
        )
        val titledPane = TitledPane("Consultar eventos", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)

        return scene
    }
}