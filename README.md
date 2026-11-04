Atividade de 04/11: adapter e proxy na consulta de CEP.

esses arquivos são adicionados ao projeto do CRUD e do AppLogger (repositório trabalho-27-10).

ConsultaCep, Endereco, ViaCepAdapter, BrasilApiAdapter e ConsultaCepProxy: pasta cep
 
CepController: rota GET /cep/{cep}

application.properties: propriedade cep.provedor (viacep ou brasilapi)
