package ui

import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import logic.database.PalestraDAO
import logic.database.ParticipanteDAO
import logic.entities.Evento
import logic.entities.Palestra
import logic.entities.Participante
import util.ui.PalestrasUI

class TelaDetalhesEvento(
    private val primaryStage: Stage,
    private val evento: Evento,
    private val telaMenuEventos: TelaMenuEventos,
    private val usuarioLogado: Participante
) {
    private val vbox = VBox(10.0)
    private val palestraDAO = PalestraDAO()
    private val palestrasUI = PalestrasUI()
    private val participanteDAO = ParticipanteDAO()

    fun detalhesEventoScene(): Scene {
        val pageLabel = Label(evento.nome)
        val vboxInfo = VBox(10.0)
        val infoLabel = Label("Detalhes do Evento")
        val descricaoLabel = Label("Descrição: ${evento.descricao}")
        val periodoLabel = Label("Período: ${evento.dataInicio} até ${evento.dataFim}")
        val valorInscricaoLabel = Label("Valor da inscrição: ${evento.valorInscricao}")
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            vbox.children.clear()
            primaryStage.scene = telaMenuEventos.menuEventosScene()
        }
        vboxInfo.children.addAll(
            pageLabel,
            infoLabel,
            descricaoLabel,
            periodoLabel,
            valorInscricaoLabel,
            btnVoltar
        )

        val hboxActions = HBox(10.0)
        val btnConsultarAgenda = Button("Consultar agenda do evento")
        btnConsultarAgenda.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.agendaEventoScene()
        }
        val btnMinhasInscricoes = Button("Minhas inscrições")
        btnMinhasInscricoes.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.inscricoesUsuarioScene()
        }
        hboxActions.children.addAll(btnConsultarAgenda, btnMinhasInscricoes)

        vbox.children.addAll(
            vboxInfo,
            hboxActions
        )
        carregarDetalhesDB()
        val titledPane = TitledPane("Detalhes do Evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Detalhes do Evento"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun carregarDetalhesDB() {
        val palestras = palestraDAO.getPalestras(evento.id)
        evento.agenda = palestras
        val todasPalestras = palestras.buscarTodasPalestras()

        for (palestra in todasPalestras!!) {
            if (palestra != null) {
                val participantes =
                    participanteDAO.getParticipantesPalestra(palestra.id, usuarioLogado.id)
                palestra.participantes = participantes
                val filaEspera = participanteDAO.getParticipantesFilaEspera(palestra.id)
                palestra.filaEspera = filaEspera
            }
        }
    }

    private fun agendaEventoScene(): Scene {
        val pageLabel = Label("Agenda do Evento ${evento.nome}")
        val palestras = evento.agenda.buscarTodasPalestras()
        if (palestras == null) {
            vbox.children.add(Label("Não há palestras cadastradas para este evento"))
        }

        val table = agendaEventoTable(palestras!!)
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.detalhesEventoScene()
        }
        vbox.children.addAll(
            pageLabel,
            table,
            btnVoltar
        )

        val titledPane = TitledPane("Agenda do Evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Agenda do Evento"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun agendaEventoTable(palestras: Array<Palestra?>): TableView<Palestra> {
        val actionColumn = palestrasUI.getActionCollumn("Inscreva-se", ::inscreverUsuarioPalestra)
        val tableView = palestrasUI.baseTablePalestras(palestras, actionColumn)
        return tableView
    }

    fun inscreverUsuarioPalestra(palestra: Palestra) {
        val checarInscricao = palestra.participantes.buscarParticipantePeloId(usuarioLogado.id)
        val label: Label

        if (checarInscricao != null) {
            label = Label("Você já está inscrito nesta palestra!")
            vbox.children.add(label)
            return
        }
        if (palestra.participantes.estaCheia()) {
            // TODO: confirmar inscrição na fila de espera
            inscricaoPalestraModal(palestra)
        }
        val inscricao = palestraDAO.subscribeParticipante(palestra.id, usuarioLogado.id)
    }

    private fun inscricaoPalestraModal(palestra: Palestra) {
        val modalStage = Stage()
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Inscrição na fila de espera"
        val label = Label("A palestra ${palestra.titulo} atingiu a capacidade máxima de participantes.")
        val subLabel =
            Label("Você pode se inscrever em outra palestra ou entrar na lista de espera. Você será avisado assim que houver vagas disponíveis.")
        val btnOutraPalestra = Button("Escolher outra palestra")
        btnOutraPalestra.setOnAction {
            modalStage.close()
        }
        val btnFilaEspera = Button("Entrar na lista de espera")
        btnFilaEspera.setOnAction {
            val checarInscricao = palestra.filaEspera.buscarParticipantePeloId(usuarioLogado.id)

            if (checarInscricao != null) {
                vbox.children.add(Label("Você já está inscrito na lista de espera desta palestra!"))
            } else {
                palestra.filaEspera.inserirParticipanteFim(usuarioLogado)
                // palestraDAO.subscribeParticipanteFilaEspera(usuarioLogado)
                vbox.children.add(Label("Inscrição na lista de espera realizada com sucesso!"))
            }
            modalStage.close()
        }
        val hbox = HBox(10.0, btnOutraPalestra, btnFilaEspera)
        val vboxModal = VBox(10.0, label, subLabel, hbox)
        val modalScene = Scene(vboxModal, 400.0, 150.0)

        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

    // TODO: definir onde a função será chamada
    fun moverFilaEspera(palestra: Palestra) {
        val participanteFilaEspera = palestra.filaEspera.removerParticipanteInicio()
        if (participanteFilaEspera == null) {
            return
        }
        palestra.participantes.inserirParticipante(participanteFilaEspera)
        // TODO: notificar usuário da inscrição na palestra
        // palestraDAO.subscribeParticipante(participanteFilaEspera)
        // palestraDAO.unsubscribeParticipanteFilaEspera(participanteFilaEspera)
    }

    private fun inscricoesUsuarioScene(): Scene {
        val pageLabel = Label("Minhas inscrições no Evento ${evento.nome}")
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.detalhesEventoScene()
        }
        val palestras = palestraDAO.getSubscriptionsParticipante(evento.id, usuarioLogado.id).buscarTodasPalestras()
        println(palestras)
        if (palestras == null) {
            vbox.children.addAll(
                Label("Você não está inscrito em nenhuma palestra deste evento"),
                btnVoltar
            )
        }

        val table = inscricoesUsuarioTable(palestras!!)

        vbox.children.addAll(
            pageLabel,
            table,
            btnVoltar
        )

        val titledPane = TitledPane("Minhas inscrições", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Minhas inscrições"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun inscricoesUsuarioTable(palestras: Array<Palestra?>): TableView<Palestra> {
        val actionColumn = palestrasUI.getActionCollumn("Cancelar inscrição", ::cancelarInscricao)
        val tableView = palestrasUI.baseTablePalestras(palestras, actionColumn)
        return tableView
    }

    fun cancelarInscricao(palestra: Palestra) {
        val inscricao = palestraDAO.unsubscribeParticipante(palestra.id, usuarioLogado.id)
        val label = Label(if (inscricao) "Inscrição cancelada com sucesso" else "Erro ao cancelar inscrição")
        vbox.children.add(label)
    }

}