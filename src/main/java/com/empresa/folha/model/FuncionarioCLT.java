package com.empresa.folha.model;

/** Funcionário CLT: salário-base + adicional de benefícios; sofre INSS e IR. */
public final class FuncionarioCLT extends Funcionario implements Tributavel {

    private static final double ALIQUOTA_INSS = 0.11;
    private static final double ALIQUOTA_IR = 0.075;

    private double adicionalBeneficios;

    public FuncionarioCLT(int codigo, String nome, String cpf,
                          double salarioBase, double adicionalBeneficios) {
        super(codigo, nome, cpf, salarioBase);
        Validador.positivo(salarioBase, "salário-base");
        setAdicionalBeneficios(adicionalBeneficios);
    }

    public double getAdicionalBeneficios() { return adicionalBeneficios; }

    public void setAdicionalBeneficios(double adicionalBeneficios) {
        this.adicionalBeneficios = Validador.naoNegativo(adicionalBeneficios, "adicional de benefícios");
    }

    @Override
    public String getCategoria() { return "CLT"; }

    @Override
    public double calcularRemuneracao() {
        return getSalarioBase() + adicionalBeneficios;
    }

    @Override
    public double calcularTributos() {
        return calcularRemuneracao() * (ALIQUOTA_INSS + ALIQUOTA_IR);
    }
}
