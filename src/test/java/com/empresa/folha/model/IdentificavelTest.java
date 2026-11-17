package com.empresa.folha.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdentificavelTest {

    @Test
    void implementacaoAnonimaDeveRetornarCodigo() {
        Identificavel i = () -> 42;
        assertEquals(42, i.getCodigo());
    }

    @Test
    void todasAsClassesConcretasDevemSerIdentificaveis() {
        Identificavel clt = new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800);
        Identificavel pj = new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, 3);
        Identificavel com = new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2500, 5, 40000);
        assertEquals(1, clt.getCodigo());
        assertEquals(2, pj.getCodigo());
        assertEquals(3, com.getCodigo());
    }
}
