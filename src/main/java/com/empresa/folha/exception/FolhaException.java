package com.empresa.folha.exception;

/** Exceção base de todos os erros de negócio do sistema. */
public class FolhaException extends RuntimeException {
    public FolhaException(String mensagem) {
        super(mensagem);
    }
}
