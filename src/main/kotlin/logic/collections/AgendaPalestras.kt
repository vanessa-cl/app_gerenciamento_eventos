package logic.collections

import logic.entities.Palestra
import logic.structures.ListaEstatica
import util.convertArray
import java.time.LocalDate
import java.time.LocalTime

class AgendaPalestras {
    private var palestras = ListaEstatica<Palestra>(10)

    fun inserirPalestra(palestra: Palestra): Boolean {
        val conflito = verificarConflitoHorarios(palestra.getHorarioInicio(), palestra.getHorarioFim())
        if (conflito) {
            // TODO: erro de conflito de horários
            return false
        }
        palestras.anexar(palestra)
        return true
    }

    fun atualizarHorarioPalestra(palestra: Palestra, horarioInicio: LocalTime, horarioFim: LocalTime): Boolean {
        val resultado = buscarPalestraPeloTitulo(palestra.getTitulo())
        val conflito = verificarConflitoHorarios(horarioInicio, horarioFim)
        if (conflito) {
            // TODO: erro de conflito de horários
            return false
        }
        if (resultado != null) {
            resultado.setHorarioInicio(horarioInicio)
            resultado.setHorarioFim(horarioFim)
        }
        return true
    }

    fun verificarConflitoHorarios(horarioInicio: LocalTime, horarioFim: LocalTime): Boolean {
        for (i in 0 until palestras.selecionarTodos().size) {
            val resultado = palestras.selecionar(i)
            if (resultado != null && resultado.getHorarioInicio() == horarioInicio && resultado.getHorarioFim() == horarioFim) {
                return true
            }
        }
        return false
    }

    fun removerPalestraPeloTitulo(titulo: String): Palestra? {
        val resultado = buscarPalestraPeloTitulo(titulo)
        if (resultado != null) {
            palestras.apagar(palestras.buscarPosicao(resultado))
            return resultado
        }
        // TODO: erro de palestra não encontrada
        return null
    }

    fun buscarPalestraPeloTitulo(titulo: String): Palestra? {
        for (i in 0 until palestras.selecionarTodos().size) {
            val resultado = palestras.selecionar(i)
            if (resultado != null && resultado.getTitulo() == titulo) {
                return resultado
            }
        }
        // TODO: erro de palestra não encontrada
        return null
    }

    fun buscarTodasPalestras(): Array<Palestra?> {
        if (estaVazia()) {
            // TODO: erro de lista vazia
            return emptyArray()
        }
        val todasPalestras = palestras.selecionarTodos()
        val palestrasArray = convertArray<Palestra?>(todasPalestras)
        return palestrasArray
    }

    fun buscarTodasPalestrasPorDia(data: LocalDate): Array<Palestra?> {
        if (estaVazia()) {
            // TODO: erro de lista vazia
        }
        val resultado: Array<Palestra?> = arrayOfNulls(palestras.selecionarTodos().size)
        for (i in 0 until palestras.selecionarTodos().size) {
            val palestra = palestras.selecionar(i)
            if (palestra != null && palestra.getData() == data) {
                resultado[i] = palestra
            }
        }
        return resultado
    }

    fun buscarTodasPalestrasPorHorario(horarioInicio: LocalTime, horarioFim: LocalTime): Array<Palestra?> {
        if (estaVazia()) {
            // TODO: erro de lista vazia
        }
        val resultado: Array<Palestra?> = arrayOfNulls(palestras.selecionarTodos().size)
        for (i in 0 until palestras.selecionarTodos().size) {
            val palestra = palestras.selecionar(i)
            if (palestra != null && palestra.getHorarioInicio() == horarioInicio && palestra.getHorarioFim() == horarioFim) {
                resultado[i] = palestra
            }
        }
        return resultado
    }

    fun ordenarPalestrasPorHorario() {
        // TODO: implementar ordenação
    }

    fun estaVazia(): Boolean {
        return palestras.estaVazia()
    }

    fun estaCheia(): Boolean {
        return palestras.estaCheia()
    }
}