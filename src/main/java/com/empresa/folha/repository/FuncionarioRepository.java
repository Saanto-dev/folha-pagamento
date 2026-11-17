package com.empresa.folha.repository;

import com.empresa.folha.exception.DadosInvalidosException;
import com.empresa.folha.exception.FuncionarioJaCadastradoException;
import com.empresa.folha.exception.FuncionarioNaoEncontradoException;
import com.empresa.folha.model.Funcionario;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Armazena e recupera funcionários em memória, indexados pelo código. */
public class FuncionarioRepository {

    private final Map<Integer, Funcionario> funcionarios = new LinkedHashMap<>();

    public void salvar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new DadosInvalidosException("Os dados do funcionário são obrigatórios.");
        }
        if (funcionarios.containsKey(funcionario.getCodigo())) {
            throw new FuncionarioJaCadastradoException(
                    "Já existe um funcionário com o código: " + funcionario.getCodigo());
        }
        if (existePorCpf(funcionario.getCpf())) {
            throw new FuncionarioJaCadastradoException(
                    "Já existe um funcionário com o CPF: " + funcionario.getCpfFormatado());
        }
        funcionarios.put(funcionario.getCodigo(), funcionario);
    }

    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios.values());
    }

    public Funcionario buscarPorCodigo(int codigo) {
        Funcionario f = funcionarios.get(codigo);
        if (f == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }
        return f;
    }

    /** @param cpf CPF somente com dígitos */
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        return funcionarios.values().stream()
                .filter(f -> f.getCpf().equals(cpf))
                .findFirst();
    }

    public boolean existePorCpf(String cpf) {
        return buscarPorCpf(cpf).isPresent();
    }

    /** Busca por nome, ignorando maiúsculas e acentos: igualdade (exata) ou trecho (parcial). */
    public List<Funcionario> buscarPorNome(String nome, boolean exata) {
        String termo = normalizar(nome);
        return funcionarios.values().stream()
                .filter(f -> exata
                        ? normalizar(f.getNome()).equals(termo)
                        : normalizar(f.getNome()).contains(termo))
                .toList();
    }

    /** Remove acentos e padroniza em minúsculas, independente do idioma da máquina. */
    private static String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    public void remover(int codigo) {
        if (funcionarios.remove(codigo) == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }
    }

    public int quantidade() {
        return funcionarios.size();
    }
}
