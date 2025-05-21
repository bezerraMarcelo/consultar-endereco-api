package com.example.consultarendereco.dominio.portas.saida;

import com.example.consultarendereco.dominio.entidade.Endereco;

import java.util.Optional;

public interface EnderecoRepositorio {

    Optional<Endereco> encontrarPorCep(String cep);
    Endereco salvar(Endereco endereco);
}
