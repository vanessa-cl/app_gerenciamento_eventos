package ui

import javafx.geometry.Insets
import javafx.geometry.Pos
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
import util.notifyUsers
import util.ui.Alert
import util.ui.Header
import util.ui.PalestrasUI

class TelaDetalhesEvento(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val evento: Evento,
    private val telaMenuEventos: TelaMenuEventos,
    private val usuarioLogado: Participante
) {
    private var titulo = "Detalhes do Evento"
    private val header = Header(primaryStage, mainApp, usuarioLogado, titulo)
    private val alert = Alert()
    private val palestraDAO = PalestraDAO()
    private val palestrasUI = PalestrasUI()
    private val participanteDAO = ParticipanteDAO()

    init {
        loadDataDB()
    }

    private fun loadDataDB() {
        usuarioLogado.inscricoes = palestraDAO.getSubscriptionsParticipante(evento.id, usuarioLogado.id)
        println(
            palestraDAO.getSubscriptionsParticipante(evento.id, usuarioLogado.id).buscarTodasPalestras()
                .contentToString()
        )
        val palestras = palestraDAO.getPalestras(evento.id)
//        println(usuarioLogado.inscricoes.buscarTodasPalestras().contentToString())
        evento.agenda = palestras
        val todasPalestras = palestras.buscarTodasPalestras()

        for (palestra in todasPalestras) {
            if (palestra != null) {
                palestra.participantes =
                    participanteDAO.getParticipantesPalestra(palestra.id, palestra.participantes.limiteParticipantes)
                palestra.filaEspera = participanteDAO.getParticipantesFilaEspera(palestra.id)
            }
        }
    }

    fun detalhesEventoScene(): Scene {
        val detalhesEventoVbox = VBox(10.0)
        detalhesEventoVbox.children.add(header.getHeader())

        val informacoesVbox = VBox(10.0)
        informacoesVbox.children.addAll(
            Label("Nome: ${evento.nome}"),
            Label("Descrição: ${evento.descricao}"),
            Label("Período: ${evento.dataInicio} até ${evento.dataFim}"),
            Label("Valor da inscrição: ${evento.valorInscricao}")
        )

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = telaMenuEventos.menuEventosScene()
        }

        val btnConsultarAgenda = Button("Consultar agenda do evento")
        btnConsultarAgenda.setOnAction {
            primaryStage.scene = this.agendaEventoScene()
        }
        val btnMinhasInscricoes = Button("Minhas inscrições")
        btnMinhasInscricoes.setOnAction {
            primaryStage.scene = this.inscricoesUsuarioScene()
        }

        detalhesEventoVbox.children.addAll(
            VBox(10.0,
                HBox(10.0, Label(evento.nome)).apply { alignment = Pos.TOP_LEFT },
                informacoesVbox.apply { alignment = Pos.TOP_LEFT },
                HBox(10.0, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT },
                HBox(10.0, Label("Opções")).apply { alignment = Pos.TOP_LEFT },
                HBox(10.0, btnConsultarAgenda, btnMinhasInscricoes).apply { alignment = Pos.TOP_LEFT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) },
        )

        detalhesEventoVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(detalhesEventoVbox, 900.0, 400.0)
        primaryStage.title = "Detalhes do Evento"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun agendaEventoScene(): Scene {
        val agendaEventoVbox = VBox(10.0)
        agendaEventoVbox.children.add(header.getHeader())

        val palestras = evento.agenda.buscarTodasPalestras()
        if (palestras == null) {
            alert.showInfo("Agenda do Evento", "Nenhuma palestra encontrada", "Nenhuma palestra encontrada!")
        }

        val actionColumn = palestrasUI.getActionCollumn("Inscreva-se", ::inscreverUsuarioPalestra)
        val tableView = palestrasUI.baseTablePalestras(palestras!!, actionColumn)

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = this.detalhesEventoScene()
        }

        agendaEventoVbox.children.addAll(
            VBox(
                10.0,
                HBox(10.0, Label("Agenda do Evento ${evento.nome}")).apply { alignment = Pos.TOP_LEFT },
                tableView.apply { maxWidth = 950.0 },
                HBox(10.0, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) }
        )

        agendaEventoVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(agendaEventoVbox, 1000.0, 400.0)
        primaryStage.title = "Agenda do Evento"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    fun inscreverUsuarioPalestra(palestra: Palestra) {
        val checarInscricao = palestra.participantes.buscarParticipantePeloId(usuarioLogado.id)

        if (checarInscricao == null && !palestra.participantes.estaCheia()) {
            val inscricao = palestraDAO.subscribeParticipante(palestra.id, usuarioLogado.id)
            if (inscricao) {
                palestra.participantes.inserirParticipante(usuarioLogado)
                usuarioLogado.inscricoes.inserirPalestra(palestra)
                alert.showInfo("Inscrição na palestra", "Sucesso", "Inscrição realizada com sucesso!")
            } else {
                alert.showError("Inscrição na palestra", "Erro", "Erro ao realizar inscrição!")
            }
        } else if (checarInscricao == null && palestra.participantes.estaCheia()) {
            inscricaoPalestraModal(palestra)
        } else {
            alert.showInfo("Inscrição na palestra", "Inscrição já realizada", "Você já está inscrito nesta palestra!")
        }
    }

    private fun inscricoesUsuarioScene(): Scene {
        val inscricoesUsuarioVbox = VBox(10.0)
        inscricoesUsuarioVbox.children.add(header.getHeader())

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = this.detalhesEventoScene()
        }
        val palestras = usuarioLogado.inscricoes.buscarTodasPalestras()
        if (palestras.contentEquals(emptyArray())) {
            alert.showInfo(
                "Minhas inscrições",
                "Nenhuma inscrição encontrada",
                "Você não está inscrito em nenhuma palestra deste evento!"
            )
            return detalhesEventoScene()
        }

        val actionColumn = palestrasUI.getActionCollumn("Cancelar inscrição", ::cancelarInscricaoModal)
        val tableView = palestrasUI.baseTablePalestras(palestras, actionColumn)

        inscricoesUsuarioVbox.children.addAll(
            VBox(
                10.0,
                HBox(10.0, Label("Minhas inscrições no Evento ${evento.nome}")).apply { alignment = Pos.TOP_LEFT },
                tableView.apply { maxWidth = 1300.0 },
                HBox(10.0, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) }
        )

        inscricoesUsuarioVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(inscricoesUsuarioVbox, 1000.0, 400.0)
        primaryStage.title = "Minhas inscrições"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun inscricaoPalestraModal(palestra: Palestra) {
        val modalStage = Stage()
        val inscricaoPalestraVbox = VBox(10.0)
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Inscrição na fila de espera"

        val btnOutraPalestra = Button("Escolher outra palestra")
        btnOutraPalestra.setOnAction {
            modalStage.close()
        }

        val btnFilaEspera = Button("Entrar na lista de espera")
        btnFilaEspera.setOnAction {
            val checarInscricao = palestra.filaEspera.buscarParticipantePeloId(usuarioLogado.id)
            if (checarInscricao != null) {
                alert.showInfo(
                    "Inscrição na lista de espera",
                    "Inscrição já realizada",
                    "Você já está inscrito na lista de espera desta palestra!"
                )
            } else {
                val inscricao = palestraDAO.subscribeParticipanteFilaEspera(palestra.id, usuarioLogado.id)
                if (!inscricao) {
                    alert.showError("Inscrição na lista de espera", "Erro", "Erro ao realizar inscrição!")
                } else {
                    palestra.filaEspera.inserirParticipanteFim(usuarioLogado)
                    alert.showInfo(
                        "Inscrição na lista de espera",
                        "Sucesso",
                        "Inscrição realizada com sucesso! Você será avisado assim que houver vagas disponíveis."
                    )
                }
            }
            modalStage.close()
        }

        inscricaoPalestraVbox.children.addAll(
            VBox(
                10.0,
                Label("A palestra ${palestra.titulo} atingiu a capacidade máxima de participantes.").apply {
                    alignment = Pos.TOP_LEFT; maxWidth = 300.0; isWrapText = true
                },
                Label("Você pode se inscrever em outra palestra ou entrar na lista de espera. Você será avisado assim que houver vagas disponíveis.").apply {
                    alignment = Pos.TOP_LEFT; maxWidth = 300.0; isWrapText = true
                },
                HBox(10.0, btnOutraPalestra, btnFilaEspera).apply { alignment = Pos.BOTTOM_RIGHT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) }
        )

        inscricaoPalestraVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val modalScene = Scene(inscricaoPalestraVbox, 400.0, 200.0)
        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

    fun cancelarInscricaoModal(palestra: Palestra) {
        val cancelarInscricaoVbox = VBox(10.0)
        val modalStage = Stage()
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Cancelar inscrição"

        val btnConfirmar = Button("Confirmar")
        btnConfirmar.setOnAction {
            val inscricao = palestraDAO.unsubscribeParticipante(palestra.id, usuarioLogado.id)
            if (inscricao) {
                palestra.participantes.removerParticipantePeloId(usuarioLogado.id)
                usuarioLogado.inscricoes.removerPalestraPeloId(palestra.id)
                alert.showInfo("Cancelamento de inscrição", "Sucesso", "Inscrição cancelada com sucesso!")
                moverFilaEspera(palestra)
            } else {
                alert.showError("Cancelamento de inscrição", "Erro", "Erro ao cancelar inscrição!")
            }
            modalStage.close()
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            modalStage.close()
        }

        cancelarInscricaoVbox.children.addAll(
            VBox(
                10.0,
                Label("Tem certeza que deseja cancelar a inscrição na palestra ${palestra.titulo}?").apply {
                    alignment = Pos.TOP_LEFT; maxWidth = 300.0; isWrapText = true
                },
                HBox(10.0, btnConfirmar, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) }
        )

        cancelarInscricaoVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val modalScene = Scene(cancelarInscricaoVbox, 400.0, 150.0)
        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

    private fun moverFilaEspera(palestra: Palestra) {
        val participanteFilaEspera = palestra.filaEspera.buscarParticipanteInicio()
        if (participanteFilaEspera == null) {
            return
        }

        val inscricao = palestraDAO.subscribeParticipante(palestra.id, participanteFilaEspera.id)
        if (inscricao) {
            palestraDAO.unsubscribeParticipanteFilaEspera(palestra.id, participanteFilaEspera.id)
            palestra.filaEspera.removerParticipanteInicio()
            palestra.participantes.inserirParticipante(participanteFilaEspera)
            participanteFilaEspera.inscricoes.inserirPalestra(palestra)
            notifyUsers(
                "Sua inscrição na palestra ${palestra.titulo} foi confirmada devido ao surgimento de uma vaga disponível",
                arrayOf(participanteFilaEspera)
            )
        }
    }

}