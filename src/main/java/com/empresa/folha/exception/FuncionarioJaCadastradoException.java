package com.empresa.folha.exception;

/** Já existe funcionário com o mesmo código ou CPF. */
public class FuncionarioJaCadastradoException extends FolhaException {
    public FuncionarioJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
