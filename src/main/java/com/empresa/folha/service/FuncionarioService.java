package com.empresa.folha.service;

import com.empresa.folha.exception.DadosInvalidosException;
import com.empresa.folha.exception.FuncionarioNaoEncontradoException;
import com.empresa.folha.model.Funcionario;
import com.empresa.folha.model.Tributavel;
import com.empresa.folha.repository.FuncionarioRepository;

import java.util.List;

/** Regras de negócio e coordenação das operações sobre funcionários e folha. */
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    // ===== Cadastro, listagem e remoção =====

    public void cadastrar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new DadosInvalidosException("Os dados do funcionário são obrigatórios.");
        }
        repository.salvar(funcionario);
    }

    public List<Funcionario> listar() {
        return repository.listarTodos();
    }

    public void remover(int codigo) {
        repository.remover(codigo);
    }

    /**
     * Tenta remover e informa o resultado, sem propagar a exceção.
     * @return true se removeu; false se o funcionário não existia
     */
    public boolean removerSeExistir(int codigo) {
        try {
            repository.remover(codigo);
            return true;
        } catch (FuncionarioNaoEncontradoException e) {
            return false;
        }
    }

    // ===== Busca (SOBRECARGA / overload) =====

    /** Busca por código identificador. */
    public Funcionario buscarFuncionario(int codigo) {
        return repository.buscarPorCodigo(codigo);
    }

    /** Busca por CPF (aceita com ou sem pontuação). */
    public Funcionario buscarFuncionario(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new DadosInvalidosException("O CPF para busca não pode ser vazio.");
        }
        String digitos = cpf.replaceAll("\\D", "");
        return repository.buscarPorCpf(digitos)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(cpf));
    }

    /** Busca por nome, de forma exata ou por trecho. */
    public List<Funcionario> buscarFuncionario(String nome, boolean buscaExata) {
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome para busca não pode ser vazio.");
        }
        return repository.buscarPorNome(nome.trim(), buscaExata);
    }

    // ===== Folha de pagamento =====

    public double calcularRemuneracao(int codigo) {
        return buscarFuncionario(codigo).calcularRemuneracao();
    }

    public String exibirDados(int codigo) {
        return buscarFuncionario(codigo).exibirDados();
    }

    public double calcularTotalFolha() {
        return repository.listarTodos().stream()
                .mapToDouble(Funcionario::calcularRemuneracao)
                .sum();
    }

    public double calcularTotalTributos() {
        return repository.listarTodos().stream()
                .filter(f -> f instanceof Tributavel)
                .mapToDouble(f -> ((Tributavel) f).calcularTributos())
                .sum();
    }

    public String gerarRelatorio() {
        List<Funcionario> lista = repository.listarTodos();
        StringBuilder sb = new StringBuilder();
        sb.append("===== RELATÓRIO DE FUNCIONÁRIOS =====\n");
        if (lista.isEmpty()) {
            sb.append("Nenhum funcionário cadastrado.\n");
        } else {
            sb.append(String.format(Funcionario.LOCALE_BR, "%-4s %-20s %-13s %12s %12s%n",
                    "COD", "NOME", "CATEGORIA", "BRUTO", "LÍQUIDO"));
            for (Funcionario f : lista) {
                sb.append(String.format(Funcionario.LOCALE_BR, "%-4d %-20.20s %-13s %12.2f %12.2f%n",
                        f.getCodigo(), f.getNome(), f.getCategoria(),
                        f.calcularRemuneracao(), f.calcularRemuneracaoLiquida()));
            }
        }
        sb.append("-----------------------------------------------------------\n");
        sb.append(String.format(Funcionario.LOCALE_BR, "TOTAL DA FOLHA (BRUTO): R$ %.2f%n", calcularTotalFolha()));
        sb.append(String.format(Funcionario.LOCALE_BR, "TOTAL DE TRIBUTOS: R$ %.2f%n", calcularTotalTributos()));
        return sb.toString();
    }
}
