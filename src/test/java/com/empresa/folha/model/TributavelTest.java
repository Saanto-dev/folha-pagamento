package com.empresa.folha.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TributavelTest {

    @Test
    void implementacaoAnonimaDeveRetornarTributos() {
        Tributavel t = () -> 100.0;
        assertEquals(100.0, t.calcularTributos(), 0.001);
    }

    @Test
    void cltEComissionadoDevemSerTributaveis() {
        assertInstanceOf(Tributavel.class, new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 0));
        assertInstanceOf(Tributavel.class,
                new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2500, 5, 0));
    }

    @Test
    void pjNaoDeveSerTributavel() {
        Object pj = new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, 3);
        assertFalse(pj instanceof Tributavel);
    }

    @Test
    void tributosDevemDiferirEntreCategorias() {
        Tributavel clt = new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800);
        Tributavel com = new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2500, 5, 40000);
        assertEquals(1110.0, clt.calcularTributos(), 0.001);
        assertEquals(450.0, com.calcularTributos(), 0.001);
    }
}
