package com.empresa.folha.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioNaoEncontradoExceptionTest {

    @Test
    void construtorPorCodigoDeveCitarOCodigo() {
        assertTrue(new FuncionarioNaoEncontradoException(7).getMessage().contains("7"));
    }

    @Test
    void construtorPorCpfDeveCitarOCpf() {
        String msg = new FuncionarioNaoEncontradoException("529.982.247-25").getMessage();
        assertTrue(msg.contains("CPF"));
        assertTrue(msg.contains("529.982.247-25"));
    }

    @Test
    void deveHerdarDeFolhaException() {
        assertInstanceOf(FolhaException.class, new FuncionarioNaoEncontradoException(1));
    }

    @Test
    void naoDeveSerDadosInvalidos() {
        Object e = new FuncionarioNaoEncontradoException(1);
        assertFalse(e instanceof DadosInvalidosException);
    }
}
