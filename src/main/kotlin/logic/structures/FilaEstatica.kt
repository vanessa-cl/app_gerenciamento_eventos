package logic.structures

import logic.interfaces.Enfileiravel

class FilaEstatica<T> : Enfileiravel<T> {
    private var ponteiroInicio = 0
    private var ponteiroFim = 0
    private var tamanho = 10
    private var dados: Array<Any?> = arrayOfNulls(tamanho)

    override fun enfileirar(dado: T?) {
        if (!estaCheia()) {
            dados[ponteiroFim] = dado
            ponteiroFim = (ponteiroFim + 1) % tamanho
        }
    }

    override fun desenfileirar(): T? {
        if (!estaVazia()) {
            val temp = dados[ponteiroInicio]
            dados[ponteiroInicio] = null
            ponteiroInicio = (ponteiroInicio + 1) % tamanho
            return temp as T?
        }
        return null
    }

    override fun atualizar(dado: T?) {
        if (!estaVazia()) {
            dados[ponteiroInicio] = dado
        }
    }

    override fun frente(): T? {
        if (!estaVazia()) {
            return dados[ponteiroInicio] as T?
        }
        return null
    }

    override fun estaVazia(): Boolean {
        return ponteiroInicio == ponteiroFim
    }

    override fun estaCheia(): Boolean {
        return (ponteiroFim + 1) % tamanho == ponteiroInicio
    }

    override fun imprimir(): String {
        var result = ""
        for (i in 0 until tamanho) {
            val posicaoReal = (ponteiroInicio + i) % tamanho
            if (dados[posicaoReal] != null) {
                result += "${dados[posicaoReal]} "
            }
        }
        return result
    }
}

