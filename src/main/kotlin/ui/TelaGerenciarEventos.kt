package ui

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import logic.entities.Participante
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.collections.ListaEventos
import logic.entities.Evento
import util.NotificationThread
import util.SequentialId
import java.time.LocalDate

class TelaGerenciarEventos(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val eventos: ListaEventos,
    val usuarioLogado: Participante
) {
    private var id = SequentialId()
    private var vbox = VBox(10.0)
    private val resultadoText = SimpleStringProperty()
    private var resultadoLabel = Label()
    private lateinit var telaGerenciarPalestras: TelaGerenciarPalestras

    fun gerenciarEventosScene(): Scene {
        val pageLabel = Label("Gerenciamento de Eventos")
        val vboxEventos = exibirEventosBox()
        val btnCadastro = Button("Cadastrar novo evento")
        btnCadastro.setOnAction {
            vbox.children.clear()
            primaryStage.scene = cadastrarEventoScene()
        }
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = mainApp.getInitialScene()
        }
        vbox = VBox(
            10.0,
            pageLabel,
            vboxEventos,
            btnCadastro,
            btnVoltar
        )

        val titledPane = TitledPane("Gerenciamento de Eventos", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Gerenciar Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun cadastrarEventoScene(): Scene {
        resultadoLabel.textProperty().bind(resultadoText)
        resultadoText.set("")
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
            eventos.inserirEvento(novoEvento)
            resultadoText.set("Evento cadastrado com sucesso!")
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = gerenciarEventosScene()
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
            resultadoLabel,
            btnConfirmar,
            btnVoltar
        )
        val titledPane = TitledPane("Cadastrar novo evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)

        return scene
    }

    private fun exibirEventosBox(): VBox {
        val vbox = VBox(10.0)
        resultadoLabel.textProperty().bind(resultadoText)
        val eventos = eventos.buscarTodosEventos()
        if (eventos == null) {
            resultadoText.set("Não há eventos cadastrados!")
            vbox.children.addAll(
                resultadoLabel
            )
            return vbox
        }

        val table = gerenciarEventosTable(eventos)
        vbox.children.addAll(
            table
        )

        return vbox
    }

    private fun gerenciarEventosTable(eventos: Array<Evento?>): TableView<Evento> {
        val tableView = TableView<Evento>()

        val idColumn = TableColumn<Evento, Int>("ID")
        idColumn.cellValueFactory = PropertyValueFactory("id")

        val nomeColumn = TableColumn<Evento, String>("Nome")
        nomeColumn.cellValueFactory = PropertyValueFactory("nome")

        val dataInicioColumn = TableColumn<Evento, LocalDate>("Data de Início")
        dataInicioColumn.cellValueFactory = PropertyValueFactory("dataInicio")

        val dataTerminoColumn = TableColumn<Evento, LocalDate>("Data de Término")
        dataTerminoColumn.cellValueFactory = PropertyValueFactory("dataFim")

        val descricaoColumn = TableColumn<Evento, String>("Descrição")
        descricaoColumn.cellValueFactory = PropertyValueFactory("descricao")

        val valorInscricaoColumn = TableColumn<Evento, Double>("Valor da Inscrição")
        valorInscricaoColumn.cellValueFactory = PropertyValueFactory("valorInscricao")

        val actionColumn = TableColumn<Evento, Void>("Ações")
        actionColumn.setCellFactory {
            object : TableCell<Evento, Void>() {
                private val btn = Button("Gerenciar")

                init {
                    btn.setOnAction {
                        val evento = tableView.items[index]
                        telaGerenciarPalestras = TelaGerenciarPalestras(primaryStage, evento, this@TelaGerenciarEventos)
                        primaryStage.scene = telaGerenciarPalestras.gerenciarPalestrasScene()
                    }
                }

                override fun updateItem(item: Void?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        graphic = null
                    } else {
                        graphic = btn
                    }
                }
            }
        }

        tableView.columns.addAll(
            idColumn,
            nomeColumn,
            dataInicioColumn,
            dataTerminoColumn,
            descricaoColumn,
            valorInscricaoColumn,
            actionColumn
        )

        val data = FXCollections.observableArrayList(eventos.filterNotNull())
        tableView.items = data
        return tableView
    }

}