package com.empresa.folha.model;

/** Contrato para quem sofre tributos ou descontos sobre a remuneração. */
public interface Tributavel {
    double calcularTributos();
}
