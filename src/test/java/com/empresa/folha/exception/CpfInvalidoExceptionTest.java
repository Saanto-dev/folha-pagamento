package com.empresa.folha.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CpfInvalidoExceptionTest {

    @Test
    void mensagemDeveConterOCpfInformado() {
        String msg = new CpfInvalidoException("123.456.789-00").getMessage();
        assertTrue(msg.contains("123.456.789-00"));
        assertTrue(msg.contains("CPF"));
    }

    @Test
    void deveSerEspecializacaoDeDadosInvalidos() {
        assertInstanceOf(DadosInvalidosException.class, new CpfInvalidoException("x"));
        assertInstanceOf(FolhaException.class, new CpfInvalidoException("x"));
    }

    @Test
    void deveSerCapturadaComoDadosInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> {
            throw new CpfInvalidoException("x");
        });
    }
}
