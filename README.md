# Documentação do Projeto

## Descrição do Projeto

Este projeto foi desenvolvido em Java e utiliza SQLite como banco de dados. O projeto foi dockerizado para facilitar a configuração e a execução.

## Requisitos

- Java 21 ou superior
- Docker

## Configuração do Banco de Dados

O banco de dados SQLite está localizado dentro de src/main/resources/banco.db do repositório.

## Estrutura do Projeto

A estrutura do projeto é organizada em camadas para melhorar a manutenibilidade e escalabilidade. As principais camadas são:

- **Controller**: Responsável por lidar com as requisições HTTP.
- **Service**: Contém a lógica de negócios.
- **Repository**: Gerencia a persistência dos dados.

## Endpoints

### Controller Usuário

- **`/usuario/login`**: Endpoint para login do usuário.
- **`/usuario/registo`**: Endpoint para registro de novos usuários. Verifica se o nome do usuário e a senha existem no banco de dados.
- **`/usuario/acompanhar`**: Recebe via POST o `usuarioId` e o ativo que o usuário deseja acompanhar. Exemplo: `1, PETR`.
- **`/usuario/nao-acompanhar`**: Recebe via POST o `usuarioId` e o ativo que o usuário não deseja mais acompanhar. Exemplo: `1, PETR`.
- **`/usuario/{usuarioid}/ativos-acompanhados`**: Retorna um array com todos os ativos acompanhados pelo usuário especificado.

### Transação Controller

- **`/transacao/enviarOrdem`**: Envia ordem de compra ou venda para uma ação. Exemplo de payload:
  ```json
  {
    "usuarioId": 3,
    "quantidade": 10,
    "valor": 10,
    "ativo": "ABEV",
    "ordem": "VENDA"
  }
**Nota**: `ordem` (VENDA, COMPRA) e `ativo` (está no enum Ativos) devem ser sempre em letras maiúsculas.

### Livro de Ofertas Controller

- **`/ofertas`**: Retorna todas as ofertas registradas pelo sistema.
- **`/ofertas/{id}`**: Recebe o `id` do usuário e retorna todas as ofertas feitas por ele.

### Notificação Controller

As notificações são geradas quando o usuário marca para acompanhar uma ação e alguém envia uma ordem para essa ação.

- **`/notificacoes/nao-lidas/{userid}`**: Mostra todas as notificações não lidas pelo usuário.
- **`/notificacoes/marcar-como-lida/{notificacaoId}`**: Marca a notificação especificada como lida.

## Swagger da aplicação:
https://bolsa-valores-java.onrender.com/swagger-ui/index.html

## Instruções para Rodar o Projeto com Docker

### Clonar o Repositório

```bash
git clone https://github.com/diegooow/bolsa-valores-java.git
cd bolsa-valores-java

docker build -t nome-da-imagem .
docker run -d -p 8080:8080 --name nome-do-container nome-da-imagem

### Verificar o Funcionamento

http://localhost:8080/swagger-ui/index.html

Docker Compose
Você também pode usar o Docker Compose para facilitar a execução do projeto.
docker-compose up 
