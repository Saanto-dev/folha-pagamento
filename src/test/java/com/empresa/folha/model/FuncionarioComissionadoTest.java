package com.empresa.folha.model;

import com.empresa.folha.exception.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioComissionadoTest {

    private FuncionarioComissionado novo() {
        return new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2500, 5, 40000);
    }

    @Test
    void deveCalcularRemuneracaoComComissao() {
        assertEquals(4500.0, novo().calcularRemuneracao(), 0.001);
    }

    @Test
    void deveManterSalarioBaseSemVendas() {
        FuncionarioComissionado f =
                new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2500, 5, 0);
        assertEquals(2500.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void deveCalcularTributosELiquido() {
        assertEquals(450.0, novo().calcularTributos(), 0.001);
        assertEquals(4050.0, novo().calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void deveRetornarCategoriaEGetters() {
        FuncionarioComissionado f = novo();
        assertEquals("Comissionado", f.getCategoria());
        assertEquals(5.0, f.getPercentualComissao(), 0.001);
        assertEquals(40000.0, f.getTotalVendas(), 0.001);
        assertInstanceOf(Tributavel.class, f);
    }

    @Test
    void deveAceitarLimitesDoPercentual() {
        FuncionarioComissionado f = novo();
        f.setPercentualComissao(0);
        assertEquals(2500.0, f.calcularRemuneracao(), 0.001);
        f.setPercentualComissao(100);
        assertEquals(42500.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void deveLancarExcecaoParaPercentualInvalido() {
        assertThrows(DadosInvalidosException.class, () -> novo().setPercentualComissao(101));
        assertThrows(DadosInvalidosException.class, () -> novo().setPercentualComissao(-1));
    }

    @Test
    void deveLancarExcecaoParaVendasNegativas() {
        assertThrows(DadosInvalidosException.class, () -> novo().setTotalVendas(-10));
    }

    @Test
    void deveLancarExcecaoParaSalarioBaseInvalido() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioComissionado(3, "Carla", "390.533.447-05", 0, 5, 0));
    }
}
