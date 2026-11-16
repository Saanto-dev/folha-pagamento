package com.empresa.folha.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DadosInvalidosExceptionTest {

    @Test
    void deveGuardarMensagem() {
        assertEquals("campo vazio", new DadosInvalidosException("campo vazio").getMessage());
    }

    @Test
    void deveHerdarDeFolhaException() {
        assertInstanceOf(FolhaException.class, new DadosInvalidosException("x"));
    }

    @Test
    void deveSerLancavel() {
        assertThrows(DadosInvalidosException.class, () -> {
            throw new DadosInvalidosException("x");
        });
    }
}
