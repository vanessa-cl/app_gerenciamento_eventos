package logic.structures

import logic.interfaces.Listavel

class ListaEstatica<T> : Listavel<T> {
    private var ponteiroInicio = 0
    private var ponteiroFim = -1
    private var quantidade = 0
    private var tamanho = 10
    private var dados: Array<Any?> = arrayOfNulls(tamanho)


    override fun anexar(dado: T) {
        if (!estaCheia()) {
            dados[ponteiroFim] = dado
            ponteiroFim = (ponteiroFim + 1) % tamanho
            quantidade++
        }
    }

    override fun inserir(posicao: Int, dado: T) {
        if (posicao >= 0 && posicao < tamanho) {
            val posicaoReal = (ponteiroInicio + posicao) % tamanho
            if (dados[posicaoReal] == null) {
                dados[posicaoReal] = dado
                quantidade++
            }
        }
    }

    override fun selecionar(posicao: Int): T? {
        if (posicao >= 0 && posicao < tamanho) {
            val posicaoReal = (ponteiroInicio + posicao) % tamanho
            return dados[posicaoReal] as T?
        }
        return null
    }

    override fun selecionarTodos(): Array<T?> {
        return dados as Array<T?>
    }

    override fun buscarPosicao(dado: T?): Int {
        for (i in 0 until tamanho) {
            val posicaoReal = (ponteiroInicio + i) % tamanho
            if (dados[posicaoReal] == dado) {
                return i
            }
        }
        return -1
    }

    override fun atualizar(posicao: Int, dado: T?) {
        if (posicao >= 0 && posicao < tamanho) {
            val posicaoReal = (ponteiroInicio + posicao) % tamanho
            dados[posicaoReal] = dado
        }
    }

    override fun apagar(posicao: Int): T? {
        if (posicao >= 0 && posicao < tamanho) {
            val posicaoReal = (ponteiroInicio + posicao) % tamanho
            val dado = dados[posicaoReal]
            dados[posicaoReal] = null
            return dado as T?
        }
        return null
    }

    override fun apagarTodos(): Array<T?> {
        val dadosRemovidos = dados
        dados = arrayOfNulls(tamanho)
        quantidade = 0
        return dadosRemovidos as Array<T?>
    }

    override fun estaVazia(): Boolean {
        return quantidade == 0
    }

    override fun estaCheia(): Boolean {
        return quantidade == tamanho
    }
}



