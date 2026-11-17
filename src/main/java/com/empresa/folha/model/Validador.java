package com.empresa.folha.model;

import com.empresa.folha.exception.CpfInvalidoException;
import com.empresa.folha.exception.DadosInvalidosException;

/** Regras de validação reutilizadas pelos construtores e setters. */
final class Validador {

    private Validador() { }

    static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException("O campo '" + campo + "' é obrigatório.");
        }
        return valor.trim();
    }

    static int inteiroPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new DadosInvalidosException("O campo '" + campo + "' deve ser maior que zero.");
        }
        return valor;
    }

    static int inteiroNaoNegativo(int valor, String campo) {
        if (valor < 0) {
            throw new DadosInvalidosException("O campo '" + campo + "' não pode ser negativo.");
        }
        return valor;
    }

    static double positivo(double valor, String campo) {
        if (Double.isNaN(valor) || valor <= 0) {
            throw new DadosInvalidosException("O campo '" + campo + "' deve ser maior que zero.");
        }
        return valor;
    }

    static double naoNegativo(double valor, String campo) {
        if (Double.isNaN(valor) || valor < 0) {
            throw new DadosInvalidosException("O campo '" + campo + "' não pode ser negativo.");
        }
        return valor;
    }

    static double percentual(double valor, String campo) {
        if (Double.isNaN(valor) || valor < 0 || valor > 100) {
            throw new DadosInvalidosException(
                    "O campo '" + campo + "' deve estar entre 0 e 100 (recebido: " + valor + ").");
        }
        return valor;
    }

    /** Valida o CPF (11 dígitos, não repetidos, dígitos verificadores) e retorna só os números. */
    static String cpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new DadosInvalidosException("O campo 'CPF' é obrigatório.");
        }
        String d = cpf.replaceAll("\\D", "");
        if (d.length() != 11 || d.chars().distinct().count() == 1) {
            throw new CpfInvalidoException(cpf);
        }
        if (digito(d, 9) != d.charAt(9) - '0' || digito(d, 10) != d.charAt(10) - '0') {
            throw new CpfInvalidoException(cpf);
        }
        return d;
    }

    /** Calcula o dígito verificador na posição informada (9 ou 10). */
    private static int digito(String d, int posicao) {
        int soma = 0;
        for (int i = 0; i < posicao; i++) {
            soma += (d.charAt(i) - '0') * (posicao + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
