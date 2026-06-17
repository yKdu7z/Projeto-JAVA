# Sistema de Apostas - Campeonato de Futebol

Projeto desenvolvido para a disciplina de Laboratorio de Programacao Orientada a Objetos.

## Objetivo

Implementar um sistema para gerenciamento de apostas sobre partidas de um campeonato de futebol, permitindo:

- cadastro de clubes
- cadastro de campeonatos
- associacao de clubes a um campeonato
- cadastro de partidas
- cadastro de grupos
- cadastro de participantes
- registro de apostas
- registro de resultados
- exibicao da classificacao por grupo

## Regras implementadas

- O sistema permite no maximo 8 clubes por campeonato.
- O sistema permite no maximo 5 grupos.
- O sistema permite no maximo 5 participantes cadastrados no sistema.
- Cada grupo permite no maximo 5 participantes.
- A aposta so pode ser realizada ate 20 minutos antes do horario da partida.
- Acerto apenas do resultado da partida: 5 pontos.
- Acerto do resultado e do placar exato: 10 pontos.

## Conceitos de POO aplicados

- Encapsulamento com atributos privados e metodos de acesso.
- Construtores padrao e sobrecarregados.
- Heranca simples com `Usuario`, `Participante` e `Administrador`.
- Polimorfismo por sobrescrita de metodos como `getTipoUsuario()` e `descrever()`.
- Classe abstrata: `Usuario`.
- Interface: `Pontuavel`.
- Classes concretas para a modelagem do dominio.
- Interface grafica em Swing.
- Persistencia em banco de dados MySQL com JDBC.
- Padrao DAO/Repository na classe `RepositorioSistema`.

## Estrutura principal

- `GerenciadorApostas.java`: classe principal para iniciar o sistema.
- `Sistema.java`: camada central de regras de negocio.
- `BancoDados.java`: cria conexoes JDBC com o MySQL.
- `RepositorioSistema.java`: camada responsavel por salvar os dados no banco.
- `TelaPrincipal.java`: janela principal da aplicacao.
- `PainelParticipantes.java`: cadastro de grupos e participantes.
- `PainelApostas.java`: cadastro de clubes, campeonatos, partidas e apostas.
- `PainelResultados.java`: registro dos resultados das partidas.
- `PainelClassificacao.java`: exibicao da classificacao.
- `PainelBancoDados.java`: status da conexao e historico dos eventos gravados.

## Banco de dados

O projeto foi ajustado para usar MySQL/MariaDB via JDBC. Se voce usa WAMP, pode usar o MySQL que ja vem nele.

Configuracao padrao usada pelo sistema:

- URL: `jdbc:mysql://localhost:3306/projeto_java_poo?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo`
- Usuario: `root`
- Senha: vazia
- Banco: `projeto_java_poo`

O sistema cria as tabelas automaticamente quando abre, desde que o MySQL esteja ligado e o driver JDBC esteja disponivel. Ao iniciar, ele tambem carrega os dados ja salvos no banco.

As tabelas tambem estao documentadas em `database/schema.sql`.

### Variaveis opcionais

Se seu MySQL tiver outro usuario, senha ou porta, configure estas variaveis de ambiente antes de rodar:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/projeto_java_poo?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo"
$env:DB_USER="root"
$env:DB_PASSWORD="sua_senha"
```

## Como preparar o ambiente

1. Instale um JDK, preferencialmente JDK 17 ou superior.
2. Abra o WAMP e deixe o MySQL/MariaDB rodando.
3. Abra a pasta do projeto no VS Code.
4. Instale a extensao `Extension Pack for Java`.

## Como rodar pelo CMD

Este projeto usa o driver em `lib/mysql-connector-j-8.4.0.jar` e nao precisa de Maven.

No CMD:

```cmd
cd C:\wamp64\www\JAVA
javac -cp "lib\mysql-connector-j-8.4.0.jar" -d bin src\*.java src\controller\*.java src\database\*.java src\model\*.java src\view\*.java
java -cp "bin;lib\mysql-connector-j-8.4.0.jar" GerenciadorApostas
```

## Como rodar pelo VS Code

1. Abra a pasta do projeto no VS Code.
2. Adicione `lib/mysql-connector-j-8.4.0.jar` nas bibliotecas do projeto, se o VS Code nao reconhecer automaticamente.
3. Abra `src/controller/GerenciadorApostas.java`.
4. Clique em `Run` ou `Executar`.
5. No menu lateral da aplicacao, abra `Banco de Dados`.
6. Se aparecer `Conectado ao banco`, a conexao funcionou.
7. Cadastre grupos, participantes, clubes, campeonatos, partidas e apostas.
8. Volte em `Banco de Dados` e clique em `Atualizar historico` para ver os eventos salvos.

## Fluxo de uso do sistema

1. Cadastre um ou mais grupos.
2. Cadastre os participantes e vincule cada um a um grupo.
3. Cadastre os clubes.
4. Cadastre um campeonato.
5. Adicione os clubes ao campeonato.
6. Cadastre as partidas com data e hora no formato `dd/MM/yyyy HH:mm`.
7. Registre as apostas.
8. Registre os resultados das partidas.
9. Acesse a classificacao para visualizar o ranking por grupo.
10. Acesse `Banco de Dados` para verificar os eventos gravados.

## Integrantes

- Bianca Medina RA: 842786
- Bárbara Garcia de Figueiredo  RA: 841308
- Carlos Edurdo Michelle Marques RA: 842844
