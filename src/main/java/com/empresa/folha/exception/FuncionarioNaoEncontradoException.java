package com.empresa.folha.exception;

/** Funcionário inexistente na busca ou na remoção. */
public class FuncionarioNaoEncontradoException extends FolhaException {
    public FuncionarioNaoEncontradoException(int codigo) {
        super("Funcionário não encontrado com o código: " + codigo);
    }

    public FuncionarioNaoEncontradoException(String cpf) {
        super("Funcionário não encontrado com o CPF: " + cpf);
    }
}
