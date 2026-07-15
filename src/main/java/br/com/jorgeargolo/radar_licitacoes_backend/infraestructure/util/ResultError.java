package br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;

/**
 * @author Jorge Roberto
 */
public class ResultError {

    /**
     * Recupera os erros de validação de uma requisição e os armazena em um map.
     *
     * @param result - O objeto que contém os erros.
     * @return um map com os erros de validação da solicitação.
     */
    public static HashMap<String, String> getResultErrors(BindingResult result){

        HashMap<String, String> erros = new HashMap<>();

        for(FieldError erro : result.getFieldErrors())
            erros.put(erro.getField(), erro.getDefaultMessage());

        return erros;

    }
}
