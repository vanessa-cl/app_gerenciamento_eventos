package ui

import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.entities.Evento
import logic.entities.Palestra
import util.SequentialId
import java.time.LocalTime

class TelaGerenciarPalestras(
    private val primaryStage: Stage,
    private val evento: Evento,
    private val telaGerenciarEventos: TelaGerenciarEventos
) {
    private var id = SequentialId()
    private var vbox = VBox(10.0)
    private val resultadoText = SimpleStringProperty()
    private var resultadoLabel = Label()


    fun gerenciarPalestrasScene(): Scene {
        val pageLabel = Label("Gerenciamento de Palestras do Evento ${evento.getNome()}")
        val vboxPalestras = exibirPalestrasBox()
        val btnCadastro = Button("Cadastrar nova palestra")
        btnCadastro.setOnAction {
            vbox.children.clear()
            primaryStage.scene = cadastrarPalestraScene()
        }
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = telaGerenciarEventos.gerenciarEventosScene()
        }
        vbox = VBox(
            10.0,
            pageLabel,
            vboxPalestras,
            btnCadastro,
            btnVoltar
        )

        val titledPane = TitledPane("Gerenciamento de Palestras", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Gerenciar Palestras"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    fun cadastrarPalestraScene(): Scene {
        resultadoLabel.textProperty().bind(resultadoText)
        resultadoText.set("")
        val labelTitulo = Label("Título da palestra:")
        val inputTitulo = TextField()
        val labelNomePalestrante = Label("Nome do palestrante:")
        val inputNomePalestrante = TextField()
        val labelLocal = Label("Local:")
        val inputLocal = TextField()
        val labelLimitePart = Label("Limite de participantes:")
        val inputLimitePart = TextField()
        val labelData = Label("Data da palestra:")
        val inputData = DatePicker()
        val labelHoraInicio = Label("Horário de início:")
        val inputHoraInicio = TextField()
        val labelHoraTermino = Label("Horário de término:")
        val inputHoraTermino = TextField()

        val btnConfirmar = Button("Cadastrar")
        btnConfirmar.setOnAction {
            val inputTituloContent = inputTitulo.text
            val inputNomePalestranteContent = inputNomePalestrante.text
            val inputLocalContent = inputLocal.text
            val inputLimitePartContent = inputLimitePart.text
            val inputDataContent = inputData.value
            val inputHoraInicioContent = inputHoraInicio.text
            val inputHoraTerminoContent = inputHoraTermino.text

            val novaPalestra =
                Palestra(
                    id.gerarId(),
                    inputTituloContent,
                    inputNomePalestranteContent,
                    inputLimitePartContent.toInt(),
                    inputLocalContent,
                    inputDataContent,
                    LocalTime.parse(inputHoraInicioContent),
                    LocalTime.parse(inputHoraTerminoContent)
                )
            val sucesso = evento.getAgenda().inserirPalestra(novaPalestra)
            if (!sucesso) {
                resultadoText.set("Erro ao cadastrar palestra! Conflito de horários")
            } else {
                resultadoText.set("Palestra cadastrada com sucesso!")
            }
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = gerenciarPalestrasScene()
        }
        vbox.children.addAll(
            labelTitulo,
            inputTitulo,
            labelNomePalestrante,
            inputNomePalestrante,
            labelLocal,
            inputLocal,
            labelLimitePart,
            inputLimitePart,
            labelData,
            inputData,
            labelHoraInicio,
            inputHoraInicio,
            labelHoraTermino,
            inputHoraTermino,
            resultadoLabel,
            btnConfirmar,
            btnVoltar
        )
        val titledPane = TitledPane("Cadastrar nova palestra", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)

        return scene
    }

    fun exibirPalestrasBox(): VBox {
        val vbox = VBox(10.0)
        resultadoLabel.textProperty().bind(resultadoText)
        val palestras = evento.getAgenda().buscarTodasPalestras()
        if (palestras == null) {
            resultadoText.set("Não há palestras cadastradas para este evento")
            vbox.children.addAll(
                resultadoLabel
            )
            return vbox
        }

        val table = gerenciarPalestrasTable(palestras)
        vbox.children.addAll(
            table
        )

        return vbox
    }

    fun gerenciarPalestrasTable(palestras: Array<Palestra?>): TableView<Palestra> {
        val tableView = TableView<Palestra>()

        val colId = TableColumn<Palestra, Int>("ID")
        colId.cellValueFactory = PropertyValueFactory("id")

        val colTitulo = TableColumn<Palestra, String>("Título")
        colTitulo.cellValueFactory = PropertyValueFactory("titulo")

        val colPalestrante = TableColumn<Palestra, String>("Palestrante")
        colPalestrante.cellValueFactory = PropertyValueFactory("palestrante")

        val colLocal = TableColumn<Palestra, String>("Local")
        colLocal.cellValueFactory = PropertyValueFactory("local")

        val colData = TableColumn<Palestra, String>("Data")
        colData.cellValueFactory = PropertyValueFactory("data")

        val colHorarioInicio = TableColumn<Palestra, String>("Horário de Início")
        colHorarioInicio.cellValueFactory = PropertyValueFactory("horarioInicio")

        val colHorarioTermino = TableColumn<Palestra, String>("Horário de Término")
        colHorarioTermino.cellValueFactory = PropertyValueFactory("horarioFim")

        val colLimiteParticipantes = TableColumn<Palestra, Int>("Limite de Participantes")
        colLimiteParticipantes.cellValueFactory = PropertyValueFactory("limiteParticipantes")

        val actionColumn = TableColumn<Palestra, Void>("Ações")
        actionColumn.setCellFactory {
            object : TableCell<Palestra, Void>() {
                private val btn = Button("Cancelar")

                init {
                    btn.setOnAction {
                        val palestra = tableView.items[index]
                        // TODO: implementar modal de cancelamento de palestra
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
            colId,
            colTitulo,
            colPalestrante,
            colLocal,
            colData,
            colHorarioInicio,
            colHorarioTermino,
            colLimiteParticipantes,
            actionColumn
        )

        val data = FXCollections.observableArrayList(palestras.filterNotNull())
        tableView.items = data
        return tableView

    }
}