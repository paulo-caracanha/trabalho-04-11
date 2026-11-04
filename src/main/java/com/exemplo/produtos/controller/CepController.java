package com.exemplo.produtos.controller;

import com.exemplo.produtos.cep.ConsultaCep;
import com.exemplo.produtos.cep.Endereco;
import com.exemplo.produtos.logger.AppLoggerComponent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CepController {

    private final ConsultaCep consultaCep;
    private final AppLoggerComponent logger;

    public CepController(ConsultaCep consultaCep, AppLoggerComponent logger) {
        this.consultaCep = consultaCep;
        this.logger = logger;
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<Endereco> buscar(@PathVariable String cep) {
        logger.debug("Requisicao GET /cep/" + cep + " recebida");
        try {
            Endereco endereco = consultaCep.buscar(cep);
            if (endereco == null) {
                logger.erro("GET /cep/" + cep + " respondeu 404");
                return ResponseEntity.notFound().build();
            }
            logger.info("GET /cep/" + cep + " respondeu 200");
            return ResponseEntity.ok(endereco);
        } catch (IllegalArgumentException e) {
            logger.erro("GET /cep/" + cep + " respondeu 400");
            return ResponseEntity.badRequest().build();
        }
    }
}
