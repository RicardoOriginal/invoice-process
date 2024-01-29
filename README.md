Procedimentos para implantação do sistema de processamento de boletos e envio de e-mails:

1 - Instalar banco de dados postgresql:
    Acesse a pasta database dentro deste projeto, instale via docker compose o banco de dados e execute o script para criação das tabelas.
    Este serviço processa as linhas usando spring batch.
    O tempo não ficou o esperado, acredito que em python seria bem mais rapido, no meu caso demorou 16 minutos para processar as 1100000 linhas.
    Para testes do backend somente eu coloquei um arquivo com a collection do postman.

2 - Subir serviço backend;
    Pode subir com os comandos tradicionais, caso queira usar docker para subir a imagen, pode ser feito também, o projeto foi configuração já com o arquivo dockerfile.

3 - Subir frontend;
    Fiz poucas alterações, logo é só subir o projeto como está no readme do mesmo.
    npm i
    npm run dev:node