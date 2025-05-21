package com.example.consultarendereco.dominio.portas.saida;

import com.example.consultarendereco.dominio.entidade.Endereco;

import java.util.Optional;

public interface EnderecoProvedorWeb {

    Optional<Endereco> consultaCep(String cep) throws Exception;
}
