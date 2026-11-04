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
@ConditionalOnProperty(name = "cep.provedor", havingValue = "viacep")
public class ViaCepAdapter implements ConsultaCep {

    private final AppLoggerComponent logger;
    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCepAdapter(AppLoggerComponent logger) {
        this.logger = logger;
    }

    @Override
    public Endereco buscar(String cep) {
        logger.debug("ViaCepAdapter chamando https://viacep.com.br/ws/" + cep + "/json/");
        Map<String, Object> json;
        try {
            json = restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json/", Map.class);
        } catch (HttpClientErrorException e) {
            logger.erro("ViaCEP respondeu com erro " + e.getStatusCode().value() + " para o cep " + cep);
            return null;
        }
        if (json == null || json.containsKey("erro")) {
            logger.erro("ViaCEP nao encontrou o cep " + cep);
            return null;
        }
        Endereco endereco = new Endereco(
                (String) json.get("cep"),
                (String) json.get("logradouro"),
                (String) json.get("bairro"),
                (String) json.get("localidade"),
                (String) json.get("uf"));
        logger.info("ViaCepAdapter converteu a resposta do ViaCEP para Endereco");
        return endereco;
    }
}
