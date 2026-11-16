package com.empresa.folha.exception;

/** CPF com tamanho errado, sequência repetida ou dígitos verificadores incorretos. */
public class CpfInvalidoException extends DadosInvalidosException {
    public CpfInvalidoException(String cpf) {
        super("CPF inválido: " + cpf);
    }
}
