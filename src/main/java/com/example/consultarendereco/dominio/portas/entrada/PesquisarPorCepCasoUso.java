package com.example.consultarendereco.dominio.portas.entrada;

import com.example.consultarendereco.dominio.entidade.Endereco;

public interface PesquisarPorCepCasoUso {

    Endereco pesquisar(String cep);
}
