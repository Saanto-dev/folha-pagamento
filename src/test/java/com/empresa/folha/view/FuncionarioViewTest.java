package com.empresa.folha.view;

import com.empresa.folha.model.FuncionarioCLT;
import com.empresa.folha.model.FuncionarioPJ;
import com.empresa.folha.repository.FuncionarioRepository;
import com.empresa.folha.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioViewTest {

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        service = new FuncionarioService(new FuncionarioRepository());
    }

    /** Executa a view com a entrada simulada e devolve tudo o que foi impresso. */
    private String executar(String entrada) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream saida = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        new FuncionarioView(service, new Scanner(entrada), saida).iniciar();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private void cadastrarAna() {
        service.cadastrar(new FuncionarioCLT(1, "Ana Souza", "529.982.247-25", 5200, 800));
    }

    // ----- sucesso -----

    @Test
    void deveEncerrarAoEscolherZero() {
        assertTrue(executar("0\n").contains("Encerrando"));
    }

    @Test
    void deveCadastrarFuncionarioCLT() {
        String saida = executar("1\n1\n1\nAna Souza\n529.982.247-25\n5200\n800\n0\n");
        assertTrue(saida.contains("Cadastrado com sucesso"));
        assertEquals(1, service.listar().size());
    }

    @Test
    void deveCadastrarPJEComissionado() {
        executar("1\n2\n2\nBruno Lima\n111.444.777-35\n2500\n3\n"
                + "1\n3\n3\nCarla\n390.533.447-05\n2500\n5\n40000\n0\n");
        assertEquals(2, service.listar().size());
        assertEquals(18000.0 - 6000.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void deveAceitarVirgulaComoSeparadorDecimal() {
        executar("1\n1\n1\nAna\n529.982.247-25\n5200,50\n800\n0\n");
        assertEquals(6000.5, service.calcularRemuneracao(1), 0.001);
    }

    @Test
    void deveListarFuncionariosCadastrados() {
        cadastrarAna();
        assertTrue(executar("2\n0\n").contains("Ana Souza"));
    }

    @Test
    void deveInformarListaVazia() {
        assertTrue(executar("2\n0\n").contains("Nenhum funcionário encontrado"));
    }

    @Test
    void deveBuscarPorCodigo() {
        cadastrarAna();
        assertTrue(executar("3\n1\n1\n0\n").contains("Ana Souza"));
    }

    @Test
    void deveBuscarPorCpf() {
        cadastrarAna();
        assertTrue(executar("3\n2\n529.982.247-25\n0\n").contains("Ana Souza"));
    }

    @Test
    void deveBuscarPorNomeParcialEExato() {
        cadastrarAna();
        assertTrue(executar("3\n3\nana\n0\n").contains("Ana Souza"));
        assertTrue(executar("3\n4\nana souza\n0\n").contains("Ana Souza"));
    }

    @Test
    void deveInformarTipoDeBuscaInvalido() {
        assertTrue(executar("3\n9\n0\n").contains("Tipo de busca inválido"));
    }

    @Test
    void deveRemoverFuncionarioExistente() {
        cadastrarAna();
        assertTrue(executar("4\n1\n0\n").contains("Funcionário removido"));
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void deveExibirRemuneracaoDadosRelatorioETotal() {
        cadastrarAna();
        service.cadastrar(new FuncionarioPJ(2, "Bruno Lima", "111.444.777-35", 2500, 3));
        String saida = executar("5\n1\n6\n1\n7\n8\n0\n");
        assertTrue(saida.contains("Remuneração: R$ 6000,00"));
        assertTrue(saida.contains("CLT"));
        assertTrue(saida.contains("TOTAL DA FOLHA"));
        assertTrue(saida.contains("Total da folha: R$ 13500,00"));
    }

    // ----- falha: cada exceção personalizada aparece com sua mensagem -----

    @Test
    void deveMostrarErroDeCpfInvalido() {
        String saida = executar("1\n1\n1\nAna\n123\n5200\n800\n0\n");
        assertTrue(saida.contains("[CPF inválido]"));
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void deveMostrarErroDeDadosInvalidos() {
        String saida = executar("1\n1\n1\nAna\n529.982.247-25\n-5200\n800\n0\n");
        assertTrue(saida.contains("[Dados inválidos]"));
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void deveMostrarErroDeEntradaNaoNumerica() {
        assertTrue(executar("abc\n0\n").contains("[Dados inválidos]"));
    }

    @Test
    void deveMostrarErroDeTipoInvalido() {
        String saida = executar("1\n7\n1\nAna\n529.982.247-25\n0\n");
        assertTrue(saida.contains("Tipo de funcionário inválido"));
    }

    @Test
    void deveMostrarErroDeDuplicidade() {
        cadastrarAna();
        String saida = executar("1\n2\n1\nBruno\n111.444.777-35\n2500\n3\n0\n");
        assertTrue(saida.contains("[Duplicidade]"));
        assertEquals(1, service.listar().size());
    }

    @Test
    void deveMostrarErroAoBuscarInexistente() {
        assertTrue(executar("3\n1\n99\n0\n").contains("[Não encontrado]"));
    }

    @Test
    void deveInformarRemocaoDeInexistenteSemDerrubarOPrograma() {
        assertTrue(executar("4\n99\n0\n").contains("Nenhum funcionário com o código 99"));
    }

    @Test
    void deveInformarOpcaoInvalida() {
        assertTrue(executar("9\n0\n").contains("Opção inválida"));
    }

    @Test
    void deveContinuarFuncionandoDepoisDeUmErro() {
        String saida = executar("abc\n0\n");
        assertTrue(saida.contains("[Dados inválidos]"));
        assertTrue(saida.contains("Encerrando"));
    }

    @Test
    void deveEncerrarSemTravarQuandoAEntradaAcaba() {
        assertTrue(executar("").contains("Entrada encerrada"));
    }
}
