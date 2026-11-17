package com.empresa.folha.exception;

import com.empresa.folha.model.*;
import com.empresa.folha.repository.FuncionarioRepository;
import com.empresa.folha.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Cobre cada situação de erro listada no requisito 4 do enunciado. */
class ExcecoesTest {

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        service = new FuncionarioService(new FuncionarioRepository());
        service.cadastrar(new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800));
    }

    @Test
    void cadastroComCpfInvalido() {
        assertThrows(CpfInvalidoException.class,
                () -> new FuncionarioCLT(2, "Bruno", "123.456.789-00", 5200, 0));
    }

    @Test
    void cadastroComCodigoJaExistente() {
        assertThrows(FuncionarioJaCadastradoException.class, () ->
                service.cadastrar(new FuncionarioPJ(1, "Bruno", "111.444.777-35", 2500, 3)));
    }

    @Test
    void cadastroComCpfJaExistente() {
        assertThrows(FuncionarioJaCadastradoException.class, () ->
                service.cadastrar(new FuncionarioPJ(2, "Bruno", "52998224725", 2500, 3)));
    }

    @Test
    void buscaDeFuncionarioInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(99));
        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.buscarFuncionario("390.533.447-05"));
    }

    @Test
    void remocaoDeFuncionarioInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.remover(99));
    }

    @Test
    void salarioNegativo() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(2, "Bruno", "111.444.777-35", -1, 0));
    }

    @Test
    void beneficioNegativo() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(2, "Bruno", "111.444.777-35", 5200, -10));
    }

    @Test
    void comissaoNegativaEPercentualAcimaDeCem() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioComissionado(2, "Bruno", "111.444.777-35", 2500, -5, 0));
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioComissionado(2, "Bruno", "111.444.777-35", 2500, 150, 0));
    }

    @Test
    void dadosObrigatoriosNaoInformados() {
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(2, null, "111.444.777-35", 5200, 0));
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(2, "Bruno", null, 5200, 0));
        assertThrows(DadosInvalidosException.class,
                () -> new FuncionarioCLT(0, "Bruno", "111.444.777-35", 5200, 0));
        assertThrows(DadosInvalidosException.class, () -> service.cadastrar(null));
    }

    @Test
    void todasAsExcecoesDevemHerdarDeFolhaException() {
        assertInstanceOf(FolhaException.class, new CpfInvalidoException("x"));
        assertInstanceOf(FolhaException.class, new DadosInvalidosException("x"));
        assertInstanceOf(FolhaException.class, new FuncionarioNaoEncontradoException(1));
        assertInstanceOf(FolhaException.class, new FuncionarioJaCadastradoException("x"));
    }
}
