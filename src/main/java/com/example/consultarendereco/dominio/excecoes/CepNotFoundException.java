package com.example.consultarendereco.dominio.excecoes;

public class CepNotFoundException extends RuntimeException {

    public CepNotFoundException(String message) {
        super(message);
    }
}
