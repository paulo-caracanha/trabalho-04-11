package com.exemplo.produtos.cep;

import com.exemplo.produtos.logger.AppLoggerComponent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ConsultaCepProxy implements ConsultaCep {

    private final ConsultaCep provedor;
    private final AppLoggerComponent logger;
    private final Map<String, Endereco> cache = new ConcurrentHashMap<>();

    public ConsultaCepProxy(@Qualifier("provedor") ConsultaCep provedor, AppLoggerComponent logger) {
        this.provedor = provedor;
        this.logger = logger;
    }

    @Override
    public Endereco buscar(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            logger.erro("Proxy bloqueou cep com formato invalido: " + cep);
            throw new IllegalArgumentException("CEP deve ter 8 digitos");
        }
        Endereco guardado = cache.get(cep);
        if (guardado != null) {
            logger.info("Proxy respondeu o cep " + cep + " pelo cache");
            return guardado;
        }
        logger.info("Proxy nao achou o cep " + cep + " no cache, consultando o provedor");
        Endereco endereco = provedor.buscar(cep);
        if (endereco != null) {
            cache.put(cep, endereco);
            logger.debug("Proxy guardou o cep " + cep + " no cache");
        }
        return endereco;
    }
}
