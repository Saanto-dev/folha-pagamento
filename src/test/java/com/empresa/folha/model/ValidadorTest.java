package com.empresa.folha.model;

import com.empresa.folha.exception.CpfInvalidoException;
import com.empresa.folha.exception.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorTest {

    @Test
    void deveAceitarCpfValidoComOuSemPontuacao() {
        assertEquals("52998224725", Validador.cpf("529.982.247-25"));
        assertEquals("52998224725", Validador.cpf("52998224725"));
        assertEquals("11144477735", Validador.cpf("111.444.777-35"));
        assertEquals("39053344705", Validador.cpf("390.533.447-05"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "52998224724", "11111111111", "000.000.000-00", "abcdefghijk"})
    void deveRejeitarCpfInvalido(String cpf) {
        assertThrows(CpfInvalidoException.class, () -> Validador.cpf(cpf));
    }

    @Test
    void deveExigirCpfNaoNuloENaoVazio() {
        assertThrows(DadosInvalidosException.class, () -> Validador.cpf(null));
        assertThrows(DadosInvalidosException.class, () -> Validador.cpf("  "));
    }

    @Test
    void deveExigirTextoObrigatorio() {
        assertThrows(DadosInvalidosException.class, () -> Validador.textoObrigatorio(null, "nome"));
        assertThrows(DadosInvalidosException.class, () -> Validador.textoObrigatorio("  ", "nome"));
        assertEquals("Ana", Validador.textoObrigatorio("  Ana ", "nome"));
    }

    @Test
    void deveValidarInteiros() {
        assertEquals(1, Validador.inteiroPositivo(1, "código"));
        assertThrows(DadosInvalidosException.class, () -> Validador.inteiroPositivo(0, "código"));
        assertEquals(0, Validador.inteiroNaoNegativo(0, "projetos"));
        assertThrows(DadosInvalidosException.class, () -> Validador.inteiroNaoNegativo(-1, "projetos"));
    }

    @Test
    void deveRejeitarNegativosEPercentuaisInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> Validador.naoNegativo(-0.01, "salário"));
        assertThrows(DadosInvalidosException.class, () -> Validador.positivo(0, "salário"));
        assertThrows(DadosInvalidosException.class, () -> Validador.percentual(100.1, "comissão"));
        assertThrows(DadosInvalidosException.class, () -> Validador.percentual(-1, "comissão"));
        assertThrows(DadosInvalidosException.class, () -> Validador.naoNegativo(Double.NaN, "salário"));
    }

    @Test
    void deveAceitarLimitesDoPercentual() {
        assertEquals(0.0, Validador.percentual(0, "comissão"));
        assertEquals(100.0, Validador.percentual(100, "comissão"));
    }

    @Test
    void mensagemDeveCitarOCampo() {
        DadosInvalidosException e = assertThrows(DadosInvalidosException.class,
                () -> Validador.naoNegativo(-1, "adicional de benefícios"));
        assertTrue(e.getMessage().contains("adicional de benefícios"));
    }
}
