package logic.collections

import logic.entities.Palestra
import logic.structures.ListaEstatica
import util.convertArray
import java.time.LocalDate
import java.time.LocalTime

// métodos de inserção e cancelamento retornam booleanos para indicar sucesso ou falha
// métodos de busca retornam null se não encontrarem o objeto

class AgendaPalestras {
    private var palestras = ListaEstatica<Palestra>(10)

    fun inserirPalestra(palestra: Palestra): Boolean {
        val conflito = verificarConflito(
            palestra.id,
            palestra.horarioInicio,
            palestra.horarioFim,
            palestra.local,
            palestra.data
        )
        if (conflito) {
            // TODO: erro de conflito de horários
            return false
        }
        palestras.anexar(palestra)
        return true
    }

    fun atualizarHorarioPalestra(palestra: Palestra, horarioInicio: LocalTime, horarioFim: LocalTime): Boolean {
        val resultado = buscarPalestraPeloTitulo(palestra.titulo)
        if (resultado != null) {
            val conflito = verificarConflito(palestra.id, horarioInicio, horarioFim, resultado.local, resultado.data)
            if (conflito) {
                // TODO: erro de conflito de horários
                // TODO: reorganizar palestras por horário
                return false
            }
            resultado.horarioInicio = horarioInicio
            resultado.horarioFim = horarioFim
            return true
        }
        // TODO: erro de palestra não encontrada
        return false
    }

    private fun verificarConflito(id: Int, horarioInicio: LocalTime, horarioFim: LocalTime, local: String, data: LocalDate): Boolean {
        for (i in 0 until palestras.selecionarTodos().size) {
            val resultado = palestras.selecionar(i)
            if (resultado != null && resultado.id != id &&
                resultado.horarioInicio == horarioInicio &&
                resultado.horarioFim == horarioFim &&
                resultado.local == local &&
                resultado.data == data
            ) {
                return true
            }
        }
        return false
    }

    fun removerPalestraPeloTitulo(titulo: String): Boolean {
        val resultado = buscarPalestraPeloTitulo(titulo)
        if (resultado != null) {
            palestras.apagar(palestras.buscarPosicao(resultado))
            return true
        }
        // TODO: erro de palestra não encontrada
        return false
    }

    fun removerPalestraPeloId(id: Int): Boolean {
        val resultado = buscarPalestraPeloId(id)
        if (resultado != null) {
            palestras.apagar(palestras.buscarPosicao(resultado))
            return true
        }
        // TODO: erro de palestra não encontrada
        return false
    }

    fun buscarPalestraPeloTitulo(titulo: String): Palestra? {
        if (estaVazia()) {
            // TODO: erro de lista vazia
            return null
        }
        for (i in 0 until palestras.selecionarTodos().size) {
            val resultado = palestras.selecionar(i)
            if (resultado != null && resultado.titulo == titulo) {
                return resultado
            }
        }
        // TODO: erro de palestra não encontrada
        return null
    }

    fun buscarPalestraPeloId(id: Int): Palestra? {
        if (estaVazia()) {
            // TODO: erro de lista vazia
            return null
        }
        for (i in 0 until palestras.selecionarTodos().size) {
            val resultado = palestras.selecionar(i)
            if (resultado != null && resultado.id == id) {
                return resultado
            }
        }
        // TODO: erro de palestra não encontrada
        return null
    }

    fun buscarTodasPalestras(): Array<Palestra?>? {
        if (estaVazia()) {
            // TODO: erro de lista vazia
            return null
        }
        val todasPalestras = palestras.selecionarTodos()
        val palestrasArray = convertArray<Palestra?>(todasPalestras)
        return palestrasArray
    }

    fun buscarTodasPalestrasPorDia(data: LocalDate): Array<Palestra?>? {
        if (estaVazia()) {
            // TODO: erro de lista vazia
            return null
        }
        val resultado: Array<Palestra?> = arrayOfNulls(palestras.selecionarTodos().size)
        for (i in 0 until palestras.selecionarTodos().size) {
            val palestra = palestras.selecionar(i)
            if (palestra != null && palestra.data == data) {
                resultado[i] = palestra
            }
        }
        return resultado
    }

    fun buscarTodasPalestrasPorHorario(horarioInicio: LocalTime, horarioFim: LocalTime): Array<Palestra?>? {
        if (estaVazia()) {
            // TODO: erro de lista vazia
            return null
        }
        val resultado: Array<Palestra?> = arrayOfNulls(palestras.selecionarTodos().size)
        for (i in 0 until palestras.selecionarTodos().size) {
            val palestra = palestras.selecionar(i)
            if (palestra != null && palestra.horarioInicio == horarioInicio && palestra.horarioFim == horarioFim) {
                resultado[i] = palestra
            }
        }
        return resultado
    }

    fun ordenarPalestrasPorHorario(): Array<Palestra>? {
        val todasPalestras = buscarTodasPalestras()
        if (todasPalestras == null) {
            // TODO: erro de lista vazia
            return emptyArray()
        }
        val palestrasOrdenadas =
            todasPalestras.filterNotNull().sortedWith(compareBy({ it!!.data }, { it!!.horarioInicio }))

        for (palestra in palestrasOrdenadas) {
            println(palestra)
        }
        return palestrasOrdenadas as Array<Palestra>
        }

    fun estaVazia(): Boolean {
        return palestras.estaVazia()
    }

    fun estaCheia(): Boolean {
        return palestras.estaCheia()
    }
}