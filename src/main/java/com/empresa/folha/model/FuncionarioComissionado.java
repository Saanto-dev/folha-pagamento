package com.empresa.folha.model;

/** Funcionário comissionado: salário-base + percentual sobre as vendas. */
public final class FuncionarioComissionado extends Funcionario implements Tributavel {

    private static final double ALIQUOTA_TRIBUTOS = 0.10;

    private double percentualComissao; // ex.: 5 significa 5%
    private double totalVendas;

    public FuncionarioComissionado(int codigo, String nome, String cpf, double salarioBase,
                                   double percentualComissao, double totalVendas) {
        super(codigo, nome, cpf, salarioBase);
        Validador.positivo(salarioBase, "salário-base");
        setPercentualComissao(percentualComissao);
        setTotalVendas(totalVendas);
    }

    public double getPercentualComissao() { return percentualComissao; }

    public void setPercentualComissao(double percentualComissao) {
        this.percentualComissao = Validador.percentual(percentualComissao, "percentual de comissão");
    }

    public double getTotalVendas() { return totalVendas; }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = Validador.naoNegativo(totalVendas, "total de vendas");
    }

    @Override
    public String getCategoria() { return "Comissionado"; }

    @Override
    public double calcularRemuneracao() {
        return getSalarioBase() + totalVendas * (percentualComissao / 100.0);
    }

    @Override
    public double calcularTributos() {
        return calcularRemuneracao() * ALIQUOTA_TRIBUTOS;
    }
}
