package logic.structures

class ListaEstatica : Listavel {
    private var Int ponteiroInicio
    private var Int ponteiroFim
    private var Int quantidade
    private var Int tamanho
    private var Array<T?> dados


    override fun anexar(dado: T) {
        if (!estaCheia()) {
            dados[ponteiroFim] = dado
            ponteiroFim = (ponteiroFim + 1) % tamanho
            quantidade++
        }
    }

    override fun Inserir(posicao: Int, dado: T) {
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
            return dados[posicaoReal]
        }
        return null
    }

    override fun selecionarTodos(): Array<T?> {
        return dados
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
            return dado
        }
        return null
    }

    override fun apagarTodos(): Array<T?> {
        val dadosRemovidos = dados
        dados = arrayOfNulls(tamanho)
        quantidade = 0
        return dadosRemovidos
    }

    override fun estaVazia(): Boolean {
        return quantidade == 0
    }

    override fun estaCheia(): Boolean {
        return quantidade == tamanho
    }
}



