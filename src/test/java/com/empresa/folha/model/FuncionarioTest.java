package com.empresa.folha.model;

import com.empresa.folha.exception.CpfInvalidoException;
import com.empresa.folha.exception.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioTest {

    /** Subclasse mínima, sem Tributavel. */
    private static class Simples extends Funcionario {
        Simples(int codigo, String nome, String cpf, double salarioBase) {
            super(codigo, nome, cpf, salarioBase);
        }
        @Override public String getCategoria() { return "Simples"; }
        @Override public double calcularRemuneracao() { return getSalarioBase(); }
    }

    /** Subclasse mínima, com Tributavel (10% fixo). */
    private static class ComTributo extends Simples implements Tributavel {
        ComTributo(int codigo, String nome, String cpf, double salarioBase) {
            super(codigo, nome, cpf, salarioBase);
        }
        @Override public double calcularTributos() { return calcularRemuneracao() * 0.10; }
    }

    private Simples novo() {
        return new Simples(1, "  Ana  ", "529.982.247-25", 1000);
    }

    // ----- sucesso -----

    @Test
    void deveCriarComDadosValidos() {
        Simples f = novo();
        assertEquals(1, f.getCodigo());
        assertEquals("Ana", f.getNome());
        assertEquals("52998224725", f.getCpf());
        assertEquals("529.982.247-25", f.getCpfFormatado());
        assertEquals(1000.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void deveImplementarIdentificavel() {
        assertInstanceOf(Identificavel.class, novo());
    }

    @Test
    void deveAtualizarDadosPorSetters() {
        Simples f = novo();
        f.setNome("Maria");
        f.setSalarioBase(2000);
        assertEquals("Maria", f.getNome());
        assertEquals(2000.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void liquidaDeveSerIgualABrutaQuandoNaoTributavel() {
        Simples f = novo();
        assertEquals(f.calcularRemuneracao(), f.calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void liquidaDeveDescontarTributosQuandoTributavel() {
        ComTributo f = new ComTributo(1, "Ana", "529.982.247-25", 1000);
        assertEquals(900.0, f.calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void exibirDadosDeveConterTodasAsInformacoes() {
        String dados = novo().exibirDados();
        assertTrue(dados.contains("Ana"));
        assertTrue(dados.contains("529.982.247-25"));
        assertTrue(dados.contains("Simples"));
        assertTrue(dados.contains("1000,00"));
    }

    @Test
    void toStringDeveResumirOFuncionario() {
        String s = novo().toString();
        assertTrue(s.contains("[1]"));
        assertTrue(s.contains("Ana"));
        assertTrue(s.contains("1000,00"));
    }

    @Test
    void salarioBaseZeroDeveSerAceitoNaClasseAbstrata() {
        // o PJ passa 0.0 ao construtor da superclasse
        assertEquals(0.0, new Simples(1, "Ana", "529.982.247-25", 0).getSalarioBase(), 0.001);
    }

    // ----- falha -----

    @Test
    void deveRejeitarCodigoInvalido() {
        assertThrows(DadosInvalidosException.class, () -> new Simples(0, "Ana", "529.982.247-25", 10));
        assertThrows(DadosInvalidosException.class, () -> new Simples(-5, "Ana", "529.982.247-25", 10));
    }

    @Test
    void deveRejeitarNomeNuloOuVazio() {
        assertThrows(DadosInvalidosException.class, () -> new Simples(1, null, "529.982.247-25", 10));
        assertThrows(DadosInvalidosException.class, () -> novo().setNome("   "));
    }

    @Test
    void deveRejeitarCpfInvalido() {
        assertThrows(CpfInvalidoException.class, () -> new Simples(1, "Ana", "123", 10));
        assertThrows(CpfInvalidoException.class, () -> new Simples(1, "Ana", "529.982.247-24", 10));
        assertThrows(DadosInvalidosException.class, () -> new Simples(1, "Ana", null, 10));
    }

    @Test
    void deveRejeitarSalarioBaseNegativo() {
        assertThrows(DadosInvalidosException.class, () -> new Simples(1, "Ana", "529.982.247-25", -1));
        assertThrows(DadosInvalidosException.class, () -> novo().setSalarioBase(-0.01));
    }

    @Test
    void naoDeveAlterarEstadoQuandoSetterFalha() {
        Simples f = novo();
        assertThrows(DadosInvalidosException.class, () -> f.setSalarioBase(-1));
        assertEquals(1000.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void cpfNaoDevePoderSerAlteradoDepoisDeCriado() {
        // garante a unicidade do CPF: nenhum setter público burla o service/repository
        assertThrows(NoSuchMethodException.class,
                () -> Funcionario.class.getMethod("setCpf", String.class));
    }
}
