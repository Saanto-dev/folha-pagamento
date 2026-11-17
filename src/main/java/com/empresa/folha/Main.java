package com.empresa.folha;

import com.empresa.folha.repository.FuncionarioRepository;
import com.empresa.folha.service.FuncionarioService;
import com.empresa.folha.view.FuncionarioView;

/** Ponto de entrada: monta as camadas de dentro para fora. */
public class Main {
    public static void main(String[] args) {
        FuncionarioRepository repository = new FuncionarioRepository();
        FuncionarioService service = new FuncionarioService(repository);
        new FuncionarioView(service).iniciar();
    }
}
