package ui

import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import logic.database.PalestraDAO
import logic.database.HorarioDAO
import logic.entities.Evento
import logic.entities.Palestra
import logic.structures.ListaEstatica
import util.enums.StatusEnum
import util.enums.TurnoEnum
import java.time.LocalDate
import java.time.LocalTime

class TelaGerenciarPalestras(
    private val primaryStage: Stage,
    private val evento: Evento,
    private val telaGerenciarEventos: TelaGerenciarEventos
) {
    private var vbox = VBox(10.0)
    private val resultadoText = SimpleStringProperty()
    private var resultadoLabel = Label()
    private val palestraDAO = PalestraDAO()
    private val horarioDAO = HorarioDAO()

    fun gerenciarPalestrasScene(): Scene {
        val pageLabel = Label("Gerenciamento de Palestras do Evento ${evento.nome}")
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

    private fun cadastrarPalestraScene(): Scene {
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
        val duracaoLabel = Label("Duração da palestra em horas:")
        val inputDuracao = TextField()
        val labelHoraInicio = Label("Horário de início:")
        val comboBoxHoraInicio = ComboBox<String>()
        val labelHoraTermino = Label("Horário de término:")
        val inputHoraTermino = TextField()
        val horarios = buscarHorarios(evento.turno)
        var palestrasExistentes: Array<Palestra?>? = null

        inputData.valueProperty().addListener { _, _, valor ->
            palestrasExistentes = null
            comboBoxHoraInicio.items.clear()
            inputHoraTermino.clear()
            if (valor != null && inputDuracao.text != null) {
                palestrasExistentes = palestraDAO.getPalestras(evento.id).buscarTodasPalestrasPorDia(valor)
                for (horario in horarios.selecionarTodos()) {
                    if (horario == null) {
                        continue
                    } else {
                        if (isHorarioDisponivel(
                                LocalTime.parse(horario.toString()),
                                inputDuracao.text.toLong(),
                                palestrasExistentes
                            )
                        ) {
                            comboBoxHoraInicio.items.add(horario.toString())
                        }
                    }
                }
            }
        }

        inputDuracao.textProperty().addListener { _, _, valor ->
            inputData.value = null
            comboBoxHoraInicio.items.clear()
            inputHoraTermino.clear()
        }

        comboBoxHoraInicio.valueProperty().addListener { _, _, valor ->
            inputHoraTermino.clear()
            if (valor != null && inputDuracao.text != null && palestrasExistentes != null) {
                val horaInicio = LocalTime.parse(valor)
                if (isHorarioDisponivel(horaInicio, inputDuracao.text.toLong(), palestrasExistentes)) {
                    inputHoraTermino.text = horaInicio.plusHours(inputDuracao.text.toLong()).toString()
                }
            }
        }

        val btnConfirmar = Button("Cadastrar")
        btnConfirmar.setOnAction {
            val novaPalestra =
                Palestra(
                    0,
                    inputTitulo.text,
                    inputNomePalestrante.text,
                    inputLimitePart.text.toInt(),
                    inputLocal.text,
                    inputData.value,
                    inputDuracao.text.toLong(),
                    LocalTime.parse(comboBoxHoraInicio.value),
                    LocalTime.parse(inputHoraTermino.text)
                )
            val idGerado = palestraDAO.insertPalestra(evento.id, novaPalestra)
            if (idGerado != null) {
                novaPalestra.id = idGerado
                evento.agenda.inserirPalestra(novaPalestra)
                resultadoText.set("Palestra cadastrada com sucesso!")
            } else {
                resultadoText.set("Erro ao cadastrar palestra!")
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
            duracaoLabel,
            inputDuracao,
            labelData,
            inputData,
            labelHoraInicio,
            comboBoxHoraInicio,
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

    private fun isHorarioDisponivel(
        novaHoraInicio: LocalTime,
        novaDuracao: Long,
        palestras: Array<Palestra?>?
    ): Boolean {
        val novaHoraTermino = novaHoraInicio.plusHours(novaDuracao)

        if (palestras == null) {
            return true
        }
        for (palestra in palestras) {
            if (palestra != null) {
                val horaInicioExistente = palestra.horarioInicio
                val horaTerminoExistente = palestra.horarioFim

                if (novaHoraInicio.isBefore(horaTerminoExistente) && novaHoraInicio.isAfter(
                        horaInicioExistente.minusMinutes(
                            1
                        )
                    )
                ) {
                    return false
                }

                if (novaHoraTermino.isBefore(horaTerminoExistente) && novaHoraTermino.isAfter(horaInicioExistente)) {
                    return false
                }
            }
        }

        return true
    }

    private fun exibirPalestrasBox(): VBox {
        // TODO: adicionar filtro por data/horário
        // TODO: adicionar botão para atualizar horário da palestra
        val vbox = VBox(10.0)
        resultadoLabel.textProperty().bind(resultadoText)

        val palestras = palestraDAO.getPalestras(evento.id).buscarTodasPalestras()
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

    private fun gerenciarPalestrasTable(palestras: Array<Palestra?>): TableView<Palestra> {
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

        val colDuracao = TableColumn<Palestra, String>("Duração em Horas")
        colDuracao.cellValueFactory = PropertyValueFactory("duracao")

        val colHorarioInicio = TableColumn<Palestra, String>("Horário de Início")
        colHorarioInicio.cellValueFactory = PropertyValueFactory("horarioInicio")

        val colHorarioTermino = TableColumn<Palestra, String>("Horário de Término")
        colHorarioTermino.cellValueFactory = PropertyValueFactory("horarioFim")

        val colLimiteParticipantes = TableColumn<Palestra, Int>("Limite de Participantes")
        colLimiteParticipantes.cellValueFactory = PropertyValueFactory("limiteParticipantes")

        val colStatus = TableColumn<Palestra, StatusEnum>("Status")
        colStatus.cellValueFactory = PropertyValueFactory("status")

        val actionColumn = TableColumn<Palestra, Void>("Ações")
        actionColumn.setCellFactory {
            object : TableCell<Palestra, Void>() {
                private val btnCancelar = Button("Cancelar")
                private val btnAtualizarHorario = Button("Atualizar horário")

                init {
                    btnCancelar.setOnAction {
                        val palestra = tableView.items[index]
                        cancelarPalestraModal(palestra)
                    }
                    btnAtualizarHorario.setOnAction {
                        val palestra = tableView.items[index]
                        atualizarHorarioPalestraModal(palestra)
                    }
                }

                override fun updateItem(item: Void?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        graphic = null
                    } else {
                        graphic = HBox(10.0, btnCancelar, btnAtualizarHorario)
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
            colStatus,
            actionColumn
        )

        val data = FXCollections.observableArrayList(palestras.filterNotNull())
        tableView.items = data
        return tableView
    }

    fun cancelarPalestraModal(palestra: Palestra) {
        val modalStage = Stage()
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Tem certeza de que deseja cancelar a palestra?"

        val alerta = Label("Esta ação não poderá ser revertida.")
        val btnConfirmar = Button("Confirmar")
        val btnVoltar = Button("Voltar")

        btnConfirmar.setOnAction {
            val sucesso = palestraDAO.deletePalestra(palestra.id)
            if (sucesso) {
                evento.agenda.removerPalestraPeloId(palestra.id)
                vbox.children.clear()
                primaryStage.scene = gerenciarPalestrasScene()
            } else {
                println("Erro ao cancelar palestra!")
            }
            modalStage.close()
        }

        btnVoltar.setOnAction {
            modalStage.close()
        }

        val hbox = HBox(10.0, btnConfirmar, btnVoltar)
        val vbox = VBox(10.0, alerta, hbox)
        val modalScene = Scene(vbox, 400.0, 150.0)

        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

    private fun buscarHorarios(turno: TurnoEnum): ListaEstatica<String> {
        return horarioDAO.getHorariosPorTurno(turno)
    }

    fun atualizarHorarioPalestraModal(palestra: Palestra) {
        val modalStage = Stage()
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Atualizar horário da palestra ${palestra.titulo}"
        val horariosDisponiveis = buscarHorarios(evento.turno)

        val tituloLabel = Label("Título: ${palestra.titulo}")
        val dataLabel = Label("Data: ${palestra.data}")
        val localLabel = Label("Local: ${palestra.local}")
        val limLabel = Label("Limite de participantes: ${palestra.limiteParticipantes}")
        val horarioInicioLabel = Label("Horário de início")
        val horarioInicioComboBox = ComboBox<String>()

        for (horario in horariosDisponiveis.selecionarTodos()) {
            horarioInicioComboBox.items.add(horario.toString())

        }
        horariosDisponiveis.buscarPosicao(palestra.horarioInicio.toString())


//        horarioInicioComboBox.selectionModel.selectFirst()
        val horarioFimLabel = Label("Horário de término")
        val horarioFimComboBox = ComboBox<String>()
        val btnConfirmar = Button("Confirmar")
        val btnVoltar = Button("Voltar")

        btnConfirmar.setOnAction {
//            val atualizar = palestraDAO.atualizarHorarioPalestra(palestra.id, horarioInicioComboBox.value)
//            if (atualizar) {
//                vbox.children.clear()
//                primaryStage.scene = gerenciarPalestrasScene()
//            } else {
//                println("Erro ao atualizar horário da palestra!")
//            }
            modalStage.close()
        }

        btnVoltar.setOnAction {
            modalStage.close()
        }


        val vboxPalestra = VBox(10.0, tituloLabel, dataLabel, localLabel, limLabel)
        val vboxHorarioInicio = VBox(10.0, horarioInicioLabel, horarioInicioComboBox)
        val vboxHorarioFim = VBox(10.0, horarioFimLabel, horarioFimComboBox)
        val hboxBtn = HBox(10.0, btnConfirmar, btnVoltar)
        val vbox = VBox(10.0, vboxPalestra, vboxHorarioInicio, vboxHorarioFim, hboxBtn)
        val modalScene = Scene(vbox, 400.0, 150.0)

        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

}