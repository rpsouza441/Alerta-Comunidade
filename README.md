# Alerta-Comunidade

Este repositório foi reorganizado em um conjunto de microserviços. A estrutura atual contempla:

- **alerta-common**: módulo compartilhado com modelos, enums e DTOs.
- **api-producer**: serviço HTTP responsável por receber os alertas e publicá-los no RabbitMQ.
- **persistence-consumer**: serviço que consome as mensagens e persiste os alertas no banco de dados.
- **failed-management**: expõe rotas para inspecionar e reprocessar alertas que falharam.
- **notification-service**: componente futuro dedicado ao envio de notificações.

Cada módulo é um projeto Spring Boot independente que depende do módulo `alerta-common`.
