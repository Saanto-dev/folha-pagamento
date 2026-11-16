package com.empresa.folha.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioJaCadastradoExceptionTest {

    @Test
    void deveGuardarMensagem() {
        String msg = "Já existe um funcionário com o código: 1";
        assertEquals(msg, new FuncionarioJaCadastradoException(msg).getMessage());
    }

    @Test
    void deveHerdarDeFolhaException() {
        assertInstanceOf(FolhaException.class, new FuncionarioJaCadastradoException("x"));
    }

    @Test
    void deveSerLancavel() {
        assertThrows(FuncionarioJaCadastradoException.class, () -> {
            throw new FuncionarioJaCadastradoException("x");
        });
    }
}
