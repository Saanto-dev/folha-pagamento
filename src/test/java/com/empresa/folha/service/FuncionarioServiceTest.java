package com.empresa.folha.service;

import com.empresa.folha.exception.DadosInvalidosException;
import com.empresa.folha.exception.FuncionarioJaCadastradoException;
import com.empresa.folha.exception.FuncionarioNaoEncontradoException;
import com.empresa.folha.model.*;
import com.empresa.folha.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioServiceTest {

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        service = new FuncionarioService(new FuncionarioRepository());
        service.cadastrar(new FuncionarioCLT(1, "Ana Souza", "529.982.247-25", 5200, 800));              // 6000
        service.cadastrar(new FuncionarioPJ(2, "Bruno Lima", "111.444.777-35", 2500, 3));               // 7500
        service.cadastrar(new FuncionarioComissionado(3, "Ana Paula", "390.533.447-05", 2500, 5, 40000)); // 4500
    }

    // ----- sobrecarga de buscarFuncionario -----

    @Test
    void deveBuscarPorCodigo() {
        assertEquals("Ana Souza", service.buscarFuncionario(1).getNome());
    }

    @Test
    void deveBuscarPorCpfComOuSemPontuacao() {
        assertEquals(1, service.buscarFuncionario("529.982.247-25").getCodigo());
        assertEquals(1, service.buscarFuncionario("52998224725").getCodigo());
    }

    @Test
    void deveBuscarPorNomeExato() {
        assertEquals(1, service.buscarFuncionario("ana souza", true).size());
    }

    @Test
    void deveBuscarPorNomeParcial() {
        assertEquals(2, service.buscarFuncionario("ana", false).size());
    }

    @Test
    void deveRetornarListaVaziaParaNomeInexistente() {
        assertTrue(service.buscarFuncionario("Zé", false).isEmpty());
    }

    @Test
    void deveLancarExcecaoParaCodigoInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(99));
    }

    @Test
    void deveLancarExcecaoParaCpfInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.buscarFuncionario("000.000.000-00"));
    }

    @Test
    void deveLancarExcecaoParaCpfOuNomeVazios() {
        assertThrows(DadosInvalidosException.class, () -> service.buscarFuncionario(" "));
        assertThrows(DadosInvalidosException.class, () -> service.buscarFuncionario(" ", true));
        assertThrows(DadosInvalidosException.class, () -> service.buscarFuncionario((String) null));
    }

    // ----- cadastro, remoção, duplicidade -----

    @Test
    void deveImpedirCpfDuplicado() {
        assertThrows(FuncionarioJaCadastradoException.class, () ->
                service.cadastrar(new FuncionarioPJ(10, "Outro", "529.982.247-25", 1000, 1)));
    }

    @Test
    void deveImpedirCodigoDuplicado() {
        assertThrows(FuncionarioJaCadastradoException.class, () ->
                service.cadastrar(new FuncionarioPJ(1, "Outro", "168.995.350-09", 1000, 1)));
    }

    @Test
    void deveLancarExcecaoAoCadastrarNulo() {
        assertThrows(DadosInvalidosException.class, () -> service.cadastrar(null));
    }

    @Test
    void deveListarTodos() {
        assertEquals(3, service.listar().size());
    }

    @Test
    void deveRemoverFuncionario() {
        service.remover(2);
        assertEquals(2, service.listar().size());
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(2));
    }

    @Test
    void deveLancarExcecaoAoRemoverInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.remover(99));
    }

    @Test
    void removerSeExistirNaoDevePropagarExcecao() {
        assertFalse(service.removerSeExistir(99));
        assertTrue(service.removerSeExistir(1));
        assertEquals(2, service.listar().size());
    }

    // ----- folha, relatório, dados -----

    @Test
    void deveCalcularRemuneracaoPorCodigo() {
        assertEquals(7500.0, service.calcularRemuneracao(2), 0.001);
    }

    @Test
    void deveCalcularTotalDaFolha() {
        assertEquals(18000.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void deveCalcularTotalDeTributos() {
        // CLT 1110 + Comissionado 450 (PJ não tributa)
        assertEquals(1560.0, service.calcularTotalTributos(), 0.001);
    }

    @Test
    void deveAtualizarTotalAposRemocao() {
        service.remover(2);
        assertEquals(10500.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void deveGerarRelatorioCompleto() {
        String r = service.gerarRelatorio();
        assertTrue(r.contains("Ana Souza"));
        assertTrue(r.contains("Bruno Lima"));
        assertTrue(r.contains("Ana Paula"));
        assertTrue(r.contains("TOTAL DA FOLHA"));
        assertTrue(r.contains("18000,00"));
        assertTrue(r.contains("1560,00"));
    }

    @Test
    void deveGerarRelatorioParaFolhaVazia() {
        FuncionarioService vazio = new FuncionarioService(new FuncionarioRepository());
        assertTrue(vazio.gerarRelatorio().contains("Nenhum funcionário"));
        assertEquals(0.0, vazio.calcularTotalFolha(), 0.001);
        assertEquals(0.0, vazio.calcularTotalTributos(), 0.001);
    }

    @Test
    void deveExibirDadosCompletos() {
        String dados = service.exibirDados(1);
        assertTrue(dados.contains("CLT"));
        assertTrue(dados.contains("529.982.247-25"));
        assertTrue(dados.contains("6000,00"));
        assertTrue(dados.contains("4890,00"));
    }

    @Test
    void listaRetornadaPeloServiceNaoDeveAlterarOsDados() {
        List<Funcionario> lista = service.listar();
        lista.clear();
        assertEquals(3, service.listar().size());
    }

    @Test
    void deveBuscarPorNomeSemAcentoNoService() {
        service.cadastrar(new FuncionarioPJ(4, "José Ávila", "168.995.350-09", 1000, 1));
        assertEquals(1, service.buscarFuncionario("jose avila", true).size());
    }

    @Test
    void relatorioDeveManterColunasAlinhadasComNomeLongo() {
        service.cadastrar(new FuncionarioPJ(4, "Maria Aparecida dos Santos Oliveira",
                "168.995.350-09", 1000, 1));
        String[] linhas = service.gerarRelatorio().split("\\R");
        int tamanhoCabecalho = linhas[1].length();
        for (int i = 2; i < 2 + service.listar().size(); i++) {
            assertEquals(tamanhoCabecalho, linhas[i].length(), "linha desalinhada: " + linhas[i]);
        }
        assertFalse(service.gerarRelatorio().contains("Oliveira"));
    }
}
