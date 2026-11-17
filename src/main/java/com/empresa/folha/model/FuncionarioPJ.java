package com.empresa.folha.model;

import com.empresa.folha.exception.DadosInvalidosException;

/**
 * Prestador de serviço PJ: recebe por projeto, não possui salário-base
 * (a superclasse recebe 0.0) e recolhe os próprios impostos (não é Tributavel).
 */
public final class FuncionarioPJ extends Funcionario {

    private double valorPorProjeto;
    private int quantidadeProjetos;

    public FuncionarioPJ(int codigo, String nome, String cpf,
                         double valorPorProjeto, int quantidadeProjetos) {
        super(codigo, nome, cpf, 0.0);
        setValorPorProjeto(valorPorProjeto);
        setQuantidadeProjetos(quantidadeProjetos);
    }

    public double getValorPorProjeto() { return valorPorProjeto; }

    public void setValorPorProjeto(double valorPorProjeto) {
        this.valorPorProjeto = Validador.positivo(valorPorProjeto, "valor por projeto");
    }

    public int getQuantidadeProjetos() { return quantidadeProjetos; }

    public void setQuantidadeProjetos(int quantidadeProjetos) {
        this.quantidadeProjetos = Validador.inteiroNaoNegativo(quantidadeProjetos, "quantidade de projetos");
    }

    /** O PJ não possui salário-base: só o valor 0 é aceito. */
    @Override
    public void setSalarioBase(double salarioBase) {
        if (salarioBase != 0) {
            throw new DadosInvalidosException(
                    "O funcionário PJ não possui salário-base; use valor por projeto.");
        }
        super.setSalarioBase(salarioBase);
    }

    @Override
    public String getCategoria() { return "PJ"; }

    @Override
    public double calcularRemuneracao() {
        return valorPorProjeto * quantidadeProjetos;
    }
}
