Gerenciador de Trabalhos

dwededwdewfdwefeewdewed

Sistema desenvolvido em Spring Boot para gerenciar trabalhos acadêmicos. Ele permite que administradores, professores e alunos realizem ações como criação de disciplinas, envio de trabalhos e correção de entregas.

Descrição do Sistema

Um sistema acadêmico online que centraliza a gestão de trabalhos: professores podem
cadastrar disciplinas e trabalhos com prazos, enquanto os alunos podem enviar suas entregas
digitalmente. O sistema organiza as submissões, notifica prazos dentro do sistema, permite
avaliação pelo professor e mantém um histórico acessível para ambas as partes.


Funções principais:

Administradores: gerenciam alunos, professores e disciplinas.

Professores: criam disciplinas, registram trabalhos e corrigem entregas.

Alunos: visualizam atividades, enviam trabalhos e consultam notas.

Tecnologias Utilizadas

    Java 17

    Spring Boot 3

    Spring Security com JWT

    Spring Data JPA

    Banco H2 para desenvolvimento

    MySQL para produção e Docker

    Maven e Lombok

    Swagger/OpenAPI para documentação

    Docker e Docker Compose

    Pré-requisitos
    Execução Local

    Java 17

    Maven ou o wrapper mvnw

    H2 ou MySQL 

    Execução com Docker

    Docker instalado

    Docker Compose

    Instalação e Execução
    Modo Local

Acesse a pasta do projeto.

Compile com mvnw clean install.

Execute com mvnw spring-boot:run ou rode o JAR gerado.

A aplicação ficará disponível em:

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console

Modo Docker

    Certifique-se de que o Docker esteja rodando.

    Execute:

    docker-compose up -d


Acesse:

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html

phpMyAdmin: http://localhost:8081

Para encerrar:

docker-compose down


Com remoção de volumes:

docker-compose down -v

Configuração do Sistema

É possível configurar o banco, JWT e outras variáveis por ambiente.
Por padrão, o sistema usa H2 em memória.
Para MySQL, é necessário ajustar a URL, usuário, senha e dialeto no arquivo application.properties.

Endpoints Principais
Autenticação

POST /auth/login – Login e retorno do token JWT

POST /auth/register – Cadastro (somente administradores)

Alunos

    Criar, listar, atualizar e remover alunos

Professores

    Criar, listar, atualizar e remover professores

Disciplinas

    Criar disciplinas

    Listar por aluno ou professor

    Matrícula de aluno

Trabalhos

    Registro, listagem e edição de trabalhos

Entregas

    Envio de arquivos

    Consulta de entregas

    Correção com nota e feedback

Autenticação com JWT

    O usuário realiza login e recebe um token.

    O token deve ser enviado no header:

    Authorization: Bearer seu_token


    Apenas usuários autorizados podem acessar endpoints protegidos.

    Credenciais de Teste

Administrador padrão:

    Usuário: adm

    Senha: definida como hash no arquivo import.sql

    O banco padrão no Docker usa:

usuário appuser

senha secret

O phpMyAdmin (no Docker) pode ser acessado para visualizar o banco.

Documentação da API

Toda a API está disponível via Swagger:
http://localhost:8080/swagger-ui.html

Permite visualizar endpoints e testar requisições.

Estrutura do Projeto

Inclui pastas para controllers, serviços, segurança, entidades JPA, DTOs e repositórios, além do Dockerfile e docker-compose.

Solução de Problemas

    Porta ocupada: alterar server.port.

    Problemas com MySQL: verificar containers.

    Token inválido: gerar um novo via login.

    Erro 403: verificar permissões do usuário.

    Observações Importantes

    H2 é recriado a cada inicialização.

    Em produção, usar MySQL e alterar a chave JWT.

    O Swagger pode ser desativado para ambientes de produção.





