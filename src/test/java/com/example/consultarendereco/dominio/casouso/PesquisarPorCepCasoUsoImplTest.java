package com.example.consultarendereco.dominio.casouso;

import com.example.consultarendereco.dominio.entidade.Endereco;
import com.example.consultarendereco.dominio.excecoes.CepNotFoundException;
import com.example.consultarendereco.dominio.excecoes.ConsultaCepProviderException;
import com.example.consultarendereco.dominio.portas.saida.EnderecoProvedorWeb;
import com.example.consultarendereco.dominio.portas.saida.EnderecoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PesquisarPorCepCasoUsoImplTest {

    @Mock
    EnderecoRepositorio repositorio;

    @Mock
    EnderecoProvedorWeb provider;

    @InjectMocks
    PesquisarPorCepCasoUsoImpl casoUso;

    private final String CEP = "01001000";

    private Endereco resposta;

    @BeforeEach
    void setup() {

        resposta = new Endereco();
        resposta.setId(1L);
        resposta.setCep("01001000");
        resposta.setLogradouro("Praça da Sé");
        resposta.setComplemento("lado ímpar");
        resposta.setBairro("Sé");
        resposta.setLocalidade("São Paulo");
        resposta.setUf("SP");
    }

    @Test
    void deveAcharEnderecoNoBancoDeDados() {

        when(repositorio.encontrarPorCep(anyString())).thenReturn(Optional.of(resposta));

        Endereco enderecoEncotrado = casoUso.pesquisar(CEP);

        assertEquals(resposta.getId(), enderecoEncotrado.getId());
        assertEquals(resposta.getCep(), enderecoEncotrado.getCep());
        assertEquals(resposta.getLogradouro(), enderecoEncotrado.getLogradouro());
        assertEquals(resposta.getComplemento(), enderecoEncotrado.getComplemento());
        assertEquals(resposta.getBairro(), enderecoEncotrado.getBairro());
        assertEquals(resposta.getLocalidade(), enderecoEncotrado.getLocalidade());
        assertEquals(resposta.getUf(), enderecoEncotrado.getUf());
    }

    @Test
    void deveAcharEnderecoNaAPIExterna() throws Exception {

        when(repositorio.encontrarPorCep(anyString())).thenReturn(Optional.empty());
        when(provider.consultaCep(anyString())).thenReturn(Optional.of(resposta));
        when(repositorio.salvar(any(Endereco.class))).thenReturn(resposta);

        Endereco enderecoEncotrado = casoUso.pesquisar(CEP);

        assertEquals(resposta.getId(), enderecoEncotrado.getId());
        assertEquals(resposta.getCep(), enderecoEncotrado.getCep());
        assertEquals(resposta.getLogradouro(), enderecoEncotrado.getLogradouro());
        assertEquals(resposta.getComplemento(), enderecoEncotrado.getComplemento());
        assertEquals(resposta.getBairro(), enderecoEncotrado.getBairro());
        assertEquals(resposta.getLocalidade(), enderecoEncotrado.getLocalidade());
        assertEquals(resposta.getUf(), enderecoEncotrado.getUf());
    }

    @Test
    void deveLancarExcecaoCepNaoEncontrado() throws Exception {

        when(repositorio.encontrarPorCep(anyString())).thenReturn(Optional.empty());
        when(provider.consultaCep(anyString())).thenReturn(Optional.empty());

        assertThrows(CepNotFoundException.class, () -> casoUso.pesquisar(CEP));
    }

    @Test
    void deveLancarExcecaoConsultaCepProvider() throws Exception {

        when(repositorio.encontrarPorCep(anyString())).thenReturn(Optional.empty());
        when(provider.consultaCep(anyString())).thenThrow(new Exception("Erro"));

        assertThrows(ConsultaCepProviderException.class, () -> casoUso.pesquisar(CEP));
    }
}