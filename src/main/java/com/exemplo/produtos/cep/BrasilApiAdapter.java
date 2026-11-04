package com.exemplo.produtos.cep;

import com.exemplo.produtos.logger.AppLoggerComponent;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("provedor")
@ConditionalOnProperty(name = "cep.provedor", havingValue = "brasilapi")
public class BrasilApiAdapter implements ConsultaCep {

    private final AppLoggerComponent logger;
    private final RestTemplate restTemplate = new RestTemplate();

    public BrasilApiAdapter(AppLoggerComponent logger) {
        this.logger = logger;
    }

    @Override
    public Endereco buscar(String cep) {
        logger.debug("BrasilApiAdapter chamando https://brasilapi.com.br/api/cep/v1/" + cep);
        Map<String, Object> json;
        try {
            json = restTemplate.getForObject("https://brasilapi.com.br/api/cep/v1/" + cep, Map.class);
        } catch (HttpClientErrorException e) {
            logger.erro("BrasilAPI respondeu com erro " + e.getStatusCode().value() + " para o cep " + cep);
            return null;
        }
        if (json == null) {
            logger.erro("BrasilAPI nao encontrou o cep " + cep);
            return null;
        }
        Endereco endereco = new Endereco(
                (String) json.get("cep"),
                (String) json.get("street"),
                (String) json.get("neighborhood"),
                (String) json.get("city"),
                (String) json.get("state"));
        logger.info("BrasilApiAdapter converteu a resposta da BrasilAPI para Endereco");
        return endereco;
    }
}
