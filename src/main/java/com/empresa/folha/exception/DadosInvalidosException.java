package com.empresa.folha.exception;

/** Dado ausente, negativo, fora de faixa ou em formato inválido. */
public class DadosInvalidosException extends FolhaException {
    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
