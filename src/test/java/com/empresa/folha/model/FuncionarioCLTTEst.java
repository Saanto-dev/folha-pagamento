package com.empresa.folha.model;

import com.empresa.folha.exception.CpfInvalidoException;
import com.empresa.folha.exception.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioCLTTest {

    private FuncionarioCLT novo() {
        return new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800);
    }

    @Test
    void deveCalcularRemuneracaoBruta() {
        assertEquals(6000.0, novo().calcularRemuneracao(), 0.001);
    }

    @Test
    void deveCalcularRemuneracaoSemBeneficios() {
        FuncionarioCLT f = new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 0);
        assertEquals(5200.0, f.calcularRemuneracao(), 0.001);
        assertEquals(962.0, f.calcularTributos(), 0.001);
        assertEquals(4238.0, f.calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void deveCalcularTributos() {
        assertEquals(1110.0, novo().calcularTributos(), 0.001);
    }

    @Test
    void deveCalcularRemuneracaoLiquida() {
        assertEquals(4890.0, novo().calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void deveRetornarCategoriaEAdicional() {
        assertEquals("CLT", novo().getCategoria());
        assertEquals(800.0, novo().getAdicionalBeneficios(), 0.001);
    }

    @Test
    void deveImplementarInterfaces() {
        FuncionarioCLT f = novo();
        assertInstanceOf(Tributavel.class, f);
        assertInstanceOf(Identificavel.class, f);
        assertEquals(1, f.getCodigo());
    }

    @Test
    void deveArmazenarCpfSomenteComDigitos() {
        assertEquals("52998224725", novo().getCpf());
        assertEquals("529.982.247-25", novo().getCpfFormatado());
    }

    @Test
    void deveAtualizarSalarioBaseViaSetter() {
        FuncionarioCLT f = novo();
        f.setSalarioBase(5000);
        assertEquals(5800.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void deveAtualizarBeneficiosViaSetter() {
        FuncionarioCLT f = novo();
        f.setAdicionalBeneficios(1000);
        assertEquals(6200.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void deveLancarExcecaoParaSalarioInvalido() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(1, "Ana", "529.982.247-25", 0, 100));
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(1, "Ana", "529.982.247-25", -10, 100));
    }

    @Test
    void deveLancarExcecaoParaBeneficioNegativo() {
        assertThrows(DadosInvalidosException.class, () -> novo().setAdicionalBeneficios(-1));
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, -1));
    }

    @Test
    void deveLancarExcecaoParaCodigoInvalido() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(0, "Ana", "529.982.247-25", 5200, 0));
    }

    @Test
    void deveLancarExcecaoParaNomeVazio() {
        assertThrows(DadosInvalidosException.class, () -> novo().setNome("  "));
    }

    @Test
    void deveLancarExcecaoParaCpfInvalido() {
        assertThrows(CpfInvalidoException.class,
                () -> new FuncionarioCLT(1, "Ana", "123", 5200, 0));
    }
}
