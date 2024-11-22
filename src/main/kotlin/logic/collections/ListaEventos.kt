package logic.collections

import logic.entities.Evento
import logic.structures.ListaEstatica
import util.convertArray

class ListaEventos {
    private var eventos = ListaEstatica<Evento>(10)

    fun inserirEvento(evento: Evento) {
        eventos.anexar(evento)
    }

    fun removerEventoPeloNome(nome: String): Boolean {
        val evento = buscarEventoPeloNome(nome)
        if (evento != null) {
            eventos.apagar(eventos.buscarPosicao(evento))
            return true
        }
        return false
    }

    fun buscarEventoPeloNome(nome: String): Evento? {
        for (i in 0 until eventos.selecionarTodos().size) {
            val evento = eventos.selecionar(i)
            if (evento != null && evento.getNome() == nome) {
                return evento
            }
        }
        return null
    }

    fun buscarEventoPeloId(id: Int): Evento? {
        for (i in 0 until eventos.selecionarTodos().size) {
            val evento = eventos.selecionar(i)
            if (evento != null && evento.getId() == id) {
                return evento
            }
        }
        return null
    }

    fun buscarTodosEventos(): Array<Evento?>? {
        if (eventos.estaVazia()) {
            return null
        }
        val todosEventos = eventos.selecionarTodos()
        val todosEventosConv = convertArray<Evento?>(todosEventos)
        return todosEventosConv
    }

}