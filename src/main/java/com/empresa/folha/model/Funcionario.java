package com.empresa.folha.model;

import java.util.Locale;

/**
 * Representa um funcionário da empresa.
 * Cada subclasse implementa sua própria regra de remuneração.
 */
public abstract class Funcionario implements Identificavel {

    /** Locale usado na formatação de valores (vírgula decimal). */
    public static final Locale LOCALE_BR = Locale.forLanguageTag("pt-BR");

    private final int codigo;
    private final String cpf;
    private String nome;
    private double salarioBase;

    protected Funcionario(int codigo, String nome, String cpf, double salarioBase) {
        this.codigo = Validador.inteiroPositivo(codigo, "código");
        this.cpf = Validador.cpf(cpf);
        setNome(nome);
        this.salarioBase = Validador.naoNegativo(salarioBase, "salário-base");
    }

    @Override
    public int getCodigo() { return codigo; }

    public String getNome() { return nome; }

    public final void setNome(String nome) {
        this.nome = Validador.textoObrigatorio(nome, "nome");
    }

    /** Retorna o CPF somente com dígitos. O CPF é imutável, o que garante sua unicidade. */
    public String getCpf() { return cpf; }

    public String getCpfFormatado() {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public double getSalarioBase() { return salarioBase; }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = Validador.naoNegativo(salarioBase, "salário-base");
    }

    /** Nome da categoria (CLT, PJ, Comissionado). */
    public abstract String getCategoria();

    /** Calcula a remuneração bruta. Sobrescrito em cada subclasse. */
    public abstract double calcularRemuneracao();

    /** Remuneração após descontar tributos, quando o funcionário for Tributavel. */
    public double calcularRemuneracaoLiquida() {
        double bruta = calcularRemuneracao();
        if (this instanceof Tributavel t) {
            return bruta - t.calcularTributos();
        }
        return bruta;
    }

    public String exibirDados() {
        return String.format(LOCALE_BR,
                "Código: %d%nNome: %s%nCPF: %s%nCategoria: %s%n"
                        + "Remuneração bruta: R$ %.2f%nRemuneração líquida: R$ %.2f",
                codigo, nome, getCpfFormatado(), getCategoria(),
                calcularRemuneracao(), calcularRemuneracaoLiquida());
    }

    @Override
    public String toString() {
        return String.format(LOCALE_BR, "[%d] %s (%s) - R$ %.2f",
                codigo, nome, getCategoria(), calcularRemuneracao());
    }
}
