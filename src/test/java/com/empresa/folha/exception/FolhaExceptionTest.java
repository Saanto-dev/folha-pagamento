package com.empresa.folha.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FolhaExceptionTest {

    @Test
    void deveGuardarMensagem() {
        assertEquals("erro", new FolhaException("erro").getMessage());
    }

    @Test
    void deveSerRuntimeException() {
        assertInstanceOf(RuntimeException.class, new FolhaException("x"));
    }

    @Test
    void deveSerLancavelECapturavel() {
        FolhaException e = assertThrows(FolhaException.class, () -> {
            throw new FolhaException("falha de negócio");
        });
        assertEquals("falha de negócio", e.getMessage());
    }
}
