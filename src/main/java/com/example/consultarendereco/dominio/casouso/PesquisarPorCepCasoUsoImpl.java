package com.example.consultarendereco.dominio.casouso;

import com.example.consultarendereco.dominio.entidade.Endereco;
import com.example.consultarendereco.dominio.excecoes.CepNotFoundException;
import com.example.consultarendereco.dominio.excecoes.ConsultaCepProviderException;
import com.example.consultarendereco.dominio.portas.entrada.PesquisarPorCepCasoUso;
import com.example.consultarendereco.dominio.portas.saida.EnderecoProvedorWeb;
import com.example.consultarendereco.dominio.portas.saida.EnderecoRepositorio;

import java.util.Optional;

public class PesquisarPorCepCasoUsoImpl implements PesquisarPorCepCasoUso {

    private final EnderecoRepositorio repository;
    private final EnderecoProvedorWeb provider;

    public PesquisarPorCepCasoUsoImpl(EnderecoRepositorio repository, EnderecoProvedorWeb provider) {

        this.repository = repository;
        this.provider = provider;
    }

    @Override
    public Endereco pesquisar(String cep) {

        Optional<Endereco> endereco = repository.encontrarPorCep(cep);
        return endereco.orElseGet(() -> buscarEnderecoExterno(cep));
    }

    private Endereco buscarEnderecoExterno(String cep) {

        try {
            Optional<Endereco> enderecoOptional = provider.consultaCep(cep);
            Endereco endereco = validarResposta(enderecoOptional, cep);
            return repository.salvar(endereco);
        } catch (CepNotFoundException e) {
            throw new CepNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ConsultaCepProviderException(e.getMessage());
        }
    }

    private Endereco validarResposta(Optional<Endereco> endereco, String cep) {

        if (endereco.isEmpty()) {
            throw new CepNotFoundException("CEP não encontrado: " + cep);
        }
        return endereco.get();
    }
}
