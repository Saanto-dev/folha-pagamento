package com.empresa.folha.model;

import com.empresa.folha.exception.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioPJTest {

    private FuncionarioPJ novo() {
        return new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, 3);
    }

    @Test
    void deveCalcularRemuneracaoPorProjetos() {
        assertEquals(7500.0, novo().calcularRemuneracao(), 0.001);
    }

    @Test
    void deveRetornarZeroSemProjetos() {
        FuncionarioPJ f = new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, 0);
        assertEquals(0.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void naoDeveTerSalarioBaseNemSerTributavel() {
        FuncionarioPJ f = novo();
        assertEquals(0.0, f.getSalarioBase(), 0.001);
        Object referencia = f;
        assertFalse(referencia instanceof Tributavel);
        assertEquals(f.calcularRemuneracao(), f.calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void deveRetornarCategoriaEGetters() {
        FuncionarioPJ f = novo();
        assertEquals("PJ", f.getCategoria());
        assertEquals(2500.0, f.getValorPorProjeto(), 0.001);
        assertEquals(3, f.getQuantidadeProjetos());
    }

    @Test
    void deveAtualizarPorSetters() {
        FuncionarioPJ f = novo();
        f.setQuantidadeProjetos(5);
        f.setValorPorProjeto(3000);
        assertEquals(15000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void deveLancarExcecaoParaValorPorProjetoInvalido() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioPJ(2, "Bruno", "111.444.777-35", 0, 3));
        assertThrows(DadosInvalidosException.class, () -> novo().setValorPorProjeto(-5));
    }

    @Test
    void deveLancarExcecaoParaQuantidadeNegativa() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, -1));
        assertThrows(DadosInvalidosException.class, () -> novo().setQuantidadeProjetos(-1));
    }

    @Test
    void deveRejeitarSalarioBaseDiferenteDeZero() {
        FuncionarioPJ f = novo();
        assertThrows(DadosInvalidosException.class, () -> f.setSalarioBase(5000));
        assertEquals(0.0, f.getSalarioBase(), 0.001);
        assertEquals(7500.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void deveAceitarSalarioBaseZero() {
        FuncionarioPJ f = novo();
        f.setSalarioBase(0);
        assertEquals(0.0, f.getSalarioBase(), 0.001);
    }
}
