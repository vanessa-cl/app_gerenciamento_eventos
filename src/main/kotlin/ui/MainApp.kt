package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import java.time.LocalDate

//import javafx.application.Application
//import javafx.scene.Scene
//import javafx.scene.layout.StackPane
//import javafx.scene.control.Button
//import javafx.stage.Stage

//class MainApp : Application() {
//    override fun start(primaryStage: Stage) {
//        primaryStage.title = "App de Gerenciamento de Eventos"
//        val button = Button("Clique aqui")
//        val root = StackPane(button)
//        primaryStage.scene = Scene(root, 300.0, 200.0)
//        primaryStage.show()
//    }
//}

fun main() {
    // menu de colaboradores
    menuGerenciarEventos()
}

fun menuGerenciarEventos() {
    val telaGerenciarEventos = GerenciarEventos()
    var encerrar = false
    while (!encerrar) {
        telaGerenciarEventos.mostrarMenuColaborador()
        val opcao = readln().toInt()
        when (opcao) {
            1 -> telaGerenciarEventos.cadastrarEvento()
            2 -> telaGerenciarEventos.exibirEventos()
            3 -> menuGerenciarPalestras(telaGerenciarEventos.todosEventos)
            else -> encerrar = true
        }
    }
}

fun menuGerenciarPalestras(eventos: ListaEventos) {
    println("Digite o nome do evento que deseja gerenciar as palestras:")
    val nomeEvento = readln()
    val evento = eventos.buscarEventoPeloNome(nomeEvento)
    if (evento != null) {
        val telaGerenciarPalestras = GerenciarPalestras(evento)
        var voltar = false
        while (!voltar) {
            telaGerenciarPalestras.mostrarMenuPalestras()
            val opcao = readln().toInt()
            when (opcao) {
                1 -> telaGerenciarPalestras.cadastrarPalestra()
                2 -> telaGerenciarPalestras.exibirPalestras()
                3 -> telaGerenciarPalestras.cancelarPalestra()
                4 -> telaGerenciarPalestras.atualizarHorarioPalestra()
                5 -> telaGerenciarPalestras.consultarParticipantes()
                6 -> telaGerenciarPalestras.consultarListaEspera()
                7 -> voltar = true
            }
        }
    } else {
        println("Evento não encontrado")
    }
}
