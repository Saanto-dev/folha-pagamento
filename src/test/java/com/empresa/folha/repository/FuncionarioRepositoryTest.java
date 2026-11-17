package com.empresa.folha.repository;

import com.empresa.folha.exception.DadosInvalidosException;
import com.empresa.folha.exception.FuncionarioJaCadastradoException;
import com.empresa.folha.exception.FuncionarioNaoEncontradoException;
import com.empresa.folha.model.FuncionarioCLT;
import com.empresa.folha.model.FuncionarioPJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioRepositoryTest {

    private FuncionarioRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FuncionarioRepository();
    }

    @Test
    void deveSalvarEBuscarPorCodigo() {
        FuncionarioCLT f = new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800);
        repository.salvar(f);
        assertSame(f, repository.buscarPorCodigo(1));
    }

    @Test
    void deveListarTodosNaOrdemDeInsercao() {
        repository.salvar(new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800));
        repository.salvar(new FuncionarioPJ(2, "Bruno", "111.444.777-35", 2500, 3));
        assertEquals(2, repository.listarTodos().size());
        assertEquals(1, repository.listarTodos().get(0).getCodigo());
    }

    @Test
    void listaRetornadaNaoDeveAlterarOArmazenamento() {
        repository.salvar(new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800));
        repository.listarTodos().clear();
        assertEquals(1, repository.quantidade());
    }

    @Test
    void deveLancarExcecaoParaCodigoDuplicado() {
        repository.salvar(new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800));
        assertThrows(FuncionarioJaCadastradoException.class, () ->
                repository.salvar(new FuncionarioPJ(1, "Bruno", "111.444.777-35", 2500, 3)));
    }

    @Test
    void deveLancarExcecaoAoBuscarInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> repository.buscarPorCodigo(99));
    }

    @Test
    void deveRemoverFuncionario() {
        repository.salvar(new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800));
        repository.remover(1);
        assertEquals(0, repository.quantidade());
    }

    @Test
    void deveLancarExcecaoAoRemoverInexistente() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> repository.remover(99));
    }

    @Test
    void deveLancarExcecaoAoSalvarNulo() {
        assertThrows(DadosInvalidosException.class, () -> repository.salvar(null));
    }

    @Test
    void deveBuscarPorCpf() {
        FuncionarioCLT f = new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800);
        repository.salvar(f);
        assertSame(f, repository.buscarPorCpf("52998224725").orElseThrow());
        assertTrue(repository.existePorCpf("52998224725"));
    }

    @Test
    void deveRetornarVazioParaCpfInexistente() {
        assertTrue(repository.buscarPorCpf("39053344705").isEmpty());
        assertFalse(repository.existePorCpf("39053344705"));
    }

    @Test
    void deveBuscarPorNomeExatoEParcial() {
        repository.salvar(new FuncionarioCLT(1, "Ana Souza", "529.982.247-25", 5200, 0));
        repository.salvar(new FuncionarioPJ(2, "Ana Paula", "111.444.777-35", 2500, 3));
        assertEquals(1, repository.buscarPorNome("ana souza", true).size());
        assertEquals(2, repository.buscarPorNome("ANA", false).size());
        assertTrue(repository.buscarPorNome("Zé", false).isEmpty());
        assertTrue(repository.buscarPorNome("ana", true).isEmpty());
    }

    @Test
    void deveLancarExcecaoParaCpfDuplicadoComCodigoDiferente() {
        repository.salvar(new FuncionarioCLT(1, "Ana", "529.982.247-25", 5200, 800));
        assertThrows(FuncionarioJaCadastradoException.class, () ->
                repository.salvar(new FuncionarioPJ(2, "Outro", "52998224725", 2500, 3)));
        assertEquals(1, repository.quantidade());
    }

    @Test
    void deveBuscarPorNomeIgnorandoAcentos() {
        repository.salvar(new FuncionarioCLT(1, "José Conceição", "529.982.247-25", 5200, 0));
        assertEquals(1, repository.buscarPorNome("jose", false).size());
        assertEquals(1, repository.buscarPorNome("CONCEICAO", false).size());
        assertEquals(1, repository.buscarPorNome("jose conceicao", true).size());
    }

    @Test
    void deveBuscarComAcentoNoTermoMesmoSeONomeNaoTiver() {
        repository.salvar(new FuncionarioCLT(1, "Jose Silva", "529.982.247-25", 5200, 0));
        assertEquals(1, repository.buscarPorNome("José", false).size());
    }
}
