package com.empresa.folha.view;

import com.empresa.folha.exception.CpfInvalidoException;
import com.empresa.folha.exception.DadosInvalidosException;
import com.empresa.folha.exception.FolhaException;
import com.empresa.folha.exception.FuncionarioJaCadastradoException;
import com.empresa.folha.exception.FuncionarioNaoEncontradoException;
import com.empresa.folha.model.Funcionario;
import com.empresa.folha.model.FuncionarioCLT;
import com.empresa.folha.model.FuncionarioComissionado;
import com.empresa.folha.model.FuncionarioPJ;
import com.empresa.folha.service.FuncionarioService;

import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Interface de console: lê dados do usuário e apresenta os resultados. */
public class FuncionarioView {

    private final FuncionarioService service;
    private final Scanner scanner;
    private final PrintStream saida;

    /** Construtor usado pelo Main (console real). */
    public FuncionarioView(FuncionarioService service) {
        this(service, new Scanner(System.in), System.out);
    }

    /** Construtor usado nos testes (entrada e saída simuladas). */
    public FuncionarioView(FuncionarioService service, Scanner scanner, PrintStream saida) {
        this.service = service;
        this.scanner = scanner;
        this.saida = saida;
    }

    public void iniciar() {
        int opcao = -1;
        do {
            exibirMenu();
            try {
                opcao = lerInt("Escolha: ");
                executar(opcao);
            } catch (CpfInvalidoException e) {
                saida.println("[CPF inválido] " + e.getMessage());
            } catch (DadosInvalidosException e) {
                saida.println("[Dados inválidos] " + e.getMessage());
            } catch (FuncionarioNaoEncontradoException e) {
                saida.println("[Não encontrado] " + e.getMessage());
            } catch (FuncionarioJaCadastradoException e) {
                saida.println("[Duplicidade] " + e.getMessage());
            } catch (FolhaException e) {
                saida.println("[Erro] " + e.getMessage());
            } catch (NoSuchElementException e) {
                saida.println("Entrada encerrada.");
                opcao = 0;
            }
        } while (opcao != 0);
    }

    private void exibirMenu() {
        saida.println("""

                ===== FOLHA DE PAGAMENTO =====
                1 - Cadastrar funcionário
                2 - Listar funcionários
                3 - Buscar funcionário
                4 - Remover funcionário
                5 - Calcular remuneração
                6 - Exibir dados completos
                7 - Gerar relatório
                8 - Total da folha
                0 - Sair
                """);
    }

    private void executar(int opcao) {
        switch (opcao) {
            case 1 -> cadastrar();
            case 2 -> imprimir(service.listar());
            case 3 -> buscar();
            case 4 -> remover();
            case 5 -> saida.printf(Funcionario.LOCALE_BR, "Remuneração: R$ %.2f%n",
                    service.calcularRemuneracao(lerInt("Código: ")));
            case 6 -> saida.println(service.exibirDados(lerInt("Código: ")));
            case 7 -> saida.println(service.gerarRelatorio());
            case 8 -> saida.printf(Funcionario.LOCALE_BR, "Total da folha: R$ %.2f%n",
                    service.calcularTotalFolha());
            case 0 -> saida.println("Encerrando...");
            default -> saida.println("Opção inválida.");
        }
    }

    private void cadastrar() {
        saida.println("Tipo: 1-CLT 2-PJ 3-Comissionado");
        int tipo = lerInt("Tipo: ");
        int codigo = lerInt("Código: ");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");

        Funcionario f = switch (tipo) {
            case 1 -> new FuncionarioCLT(codigo, nome, cpf,
                    lerDouble("Salário-base: "), lerDouble("Adicional de benefícios: "));
            case 2 -> new FuncionarioPJ(codigo, nome, cpf,
                    lerDouble("Valor por projeto: "), lerInt("Quantidade de projetos: "));
            case 3 -> new FuncionarioComissionado(codigo, nome, cpf,
                    lerDouble("Salário-base: "), lerDouble("Comissão (%): "),
                    lerDouble("Total de vendas: "));
            default -> throw new DadosInvalidosException("Tipo de funcionário inválido.");
        };
        service.cadastrar(f);
        saida.println("Cadastrado com sucesso!");
    }

    private void buscar() {
        saida.println("Buscar por: 1-Código 2-CPF 3-Nome (parcial) 4-Nome (exato)");
        switch (lerInt("Tipo: ")) {
            case 1 -> saida.println(service.buscarFuncionario(lerInt("Código: ")));
            case 2 -> saida.println(service.buscarFuncionario(lerTexto("CPF: ")));
            case 3 -> imprimir(service.buscarFuncionario(lerTexto("Nome: "), false));
            case 4 -> imprimir(service.buscarFuncionario(lerTexto("Nome: "), true));
            default -> saida.println("Tipo de busca inválido.");
        }
    }

    private void remover() {
        int codigo = lerInt("Código: ");
        if (service.removerSeExistir(codigo)) {
            saida.println("Funcionário removido.");
        } else {
            saida.println("Nenhum funcionário com o código " + codigo + ".");
        }
    }

    private void imprimir(List<Funcionario> lista) {
        if (lista.isEmpty()) {
            saida.println("Nenhum funcionário encontrado.");
        } else {
            lista.forEach(saida::println);
        }
    }

    private String lerTexto(String msg) {
        saida.print(msg);
        return scanner.nextLine();
    }

    private int lerInt(String msg) {
        saida.print(msg);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException(
                    "Digite um número inteiro válido (recebido: '" + entrada + "').");
        }
    }

    private double lerDouble(String msg) {
        saida.print(msg);
        String entrada = scanner.nextLine().trim();
        try {
            return Double.parseDouble(entrada.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException(
                    "Digite um número válido (recebido: '" + entrada + "').");
        }
    }
}
