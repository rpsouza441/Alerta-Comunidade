# Alerta Comunidade

Central de notificações para emergências comunitárias construída em Spring Boot. O projeto recebe alertas, publica em filas do RabbitMQ e notifica assinantes por e-mail e SMS. Os dados são armazenados em MariaDB e as migrações são gerenciadas pelo Flyway.

## Funcionalidades principais

- API REST para registrar e consultar alertas
- Publicação de mensagens em filas do RabbitMQ com tratamento de falhas
- Registro de alertas que falharam e possibilidade de reprocessar
- Cadastro e gerenciamento de assinantes (ativar/desativar)
- Envio de notificações por e-mail e SMS aos assinantes
- Documentação automática via Swagger (`/swagger-ui.html`)

## Executando com Docker

1. Compile o projeto para gerar o JAR:
   ```bash
   cd AlertaComunidade
   ./mvnw clean package
   ```
2. Volte para a raiz e suba os containers:
   ```bash
   docker compose up --build
   ```
   A aplicação ficará disponível em `http://localhost:8080`.
   O RabbitMQ pode ser acessado em `http://localhost:15672` (usuário/senha padrão: guest/guest).
   Ajustes de configuração podem ser realizados no arquivo `.env`.
   Para servidores SMTP que exigem autenticação e STARTTLS (ex.: Gmail na porta 587),
   defina `MAIL_SMTP_AUTH=true` e `MAIL_SMTP_STARTTLS_ENABLE=true`.

## Arquitetura e boas práticas

Este projeto foi organizado seguindo conceitos de design que facilitam a manutenção e a evolução do código:

- **Arquitetura Hexagonal (Ports and Adapters)**: a camada de domínio expõe portas (interfaces) e a infraestrutura fornece adaptadores para persistência, mensageria e notificações, mantendo as regras de negócio independentes de detalhes externos.
- **Princípios SOLID**: casos de uso, controladores e adaptadores possuem responsabilidades bem definidas e dependem de abstrações, reduzindo o acoplamento entre as partes.
- **DRY (Don't Repeat Yourself)**: mapeadores e classes utilitárias evitam duplicação de código ao converter entidades para DTOs e vice-versa.
- **Separação de preocupações/Clean Architecture**: domínio, aplicação e infraestrutura estão isolados em módulos próprios, facilitando testes e novas implementações.

## Licença

Distribuído sob a licença [MIT](LICENSE).
