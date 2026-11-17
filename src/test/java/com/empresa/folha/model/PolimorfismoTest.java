package com.empresa.folha.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Demonstra o polimorfismo por sobreposição (override). */
class PolimorfismoTest {

    private Funcionario clt() { return new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800); }
    private Funcionario pj() { return new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, 3); }
    private Funcionario com() {
        return new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2500, 5, 40000);
    }

    @Test
    void cadaReferenciaDeveExecutarSuaPropriaRegra() {
        Funcionario f1 = clt();
        Funcionario f2 = pj();
        Funcionario f3 = com();
        assertEquals(6000.0, f1.calcularRemuneracao(), 0.001);
        assertEquals(7500.0, f2.calcularRemuneracao(), 0.001);
        assertEquals(4500.0, f3.calcularRemuneracao(), 0.001);
    }

    @Test
    void listaPolimorficaDeveSomarCadaRegraDeCalculo() {
        List<Funcionario> lista = List.of(clt(), pj(), com());
        double total = 0;
        for (Funcionario f : lista) {
            total += f.calcularRemuneracao();
        }
        assertEquals(18000.0, total, 0.001);
    }

    @Test
    void liquidaDeveSerPolimorficaPorCategoria() {
        assertEquals(4890.0, clt().calcularRemuneracaoLiquida(), 0.001);
        assertEquals(7500.0, pj().calcularRemuneracaoLiquida(), 0.001);
        assertEquals(4050.0, com().calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void categoriasDevemSerDiferentes() {
        assertNotEquals(clt().getCategoria(), pj().getCategoria());
        assertNotEquals(pj().getCategoria(), com().getCategoria());
    }

    @Test
    void todosDevemSerIdentificaveis() {
        List<Identificavel> lista = List.of(clt(), pj(), com());
        assertEquals(List.of(1, 2, 3), lista.stream().map(Identificavel::getCodigo).toList());
    }
}
