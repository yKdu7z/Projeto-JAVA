# Sistema de Apostas - Campeonato de Futebol

Projeto desenvolvido para a disciplina de Programacao Orientada a Objetos / Laboratorio de Programacao Orientada a Objetos.

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

## Estrutura principal

- `GerenciadorApostas.java`: classe principal para iniciar o sistema.
- `Sistema.java`: camada central de regras de negocio.
- `TelaPrincipal.java`: janela principal da aplicacao.
- `PainelParticipantes.java`: cadastro de grupos e participantes.
- `PainelApostas.java`: cadastro de clubes, campeonatos, partidas e apostas.
- `PainelResultados.java`: registro dos resultados das partidas.
- `PainelClassificacao.java`: exibicao da classificacao.

## O que instalar na maquina

Para rodar o projeto voce precisa instalar um **JDK**, nao apenas o Java comum.

Instale uma destas opcoes:

1. `JDK 8`
2. `JDK 11`
3. `JDK 17`

Recomendacao: usar `JDK 17`, porque costuma ser mais estavel para desenvolvimento atual.

Voce pode instalar por uma destas formas:

1. Eclipse Temurin JDK
2. Oracle JDK
3. Microsoft Build of OpenJDK

Durante a instalacao, marque a opcao para adicionar o Java ao `PATH`, se existir.

## Como verificar se instalou certo

Depois de instalar, abra o `Prompt de Comando` ou `PowerShell` e rode:

```powershell
java -version
javac -version
```

Os dois comandos precisam funcionar.

Se `java` funcionar e `javac` nao funcionar, significa que voce instalou apenas o ambiente de execucao ou o `PATH` nao foi configurado corretamente.

## Como compilar e executar

Entre na pasta do projeto:

```powershell
cd D:\wamp64\www\sistemaDeApostas-main
```

Compile os arquivos:

```powershell
javac *.java
```

Depois execute:

```powershell
java GerenciadorApostas
```

## Como rodar pelo VS Code

1. Instale o `Visual Studio Code`.
2. Instale a extensao `Extension Pack for Java`.
3. Abra a pasta do projeto no VS Code.
4. Com o JDK instalado, o VS Code deve reconhecer o projeto automaticamente.
5. Abra o arquivo `GerenciadorApostas.java`.
6. Clique em `Run` ou `Executar`.

## Como rodar pelo Eclipse

1. Instale o `Eclipse IDE for Java Developers`.
2. Crie um novo projeto Java.
3. Importe os arquivos deste repositorio.
4. Execute a classe `GerenciadorApostas`.

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

## Exemplo de data e hora

Use este formato:

```text
15/04/2026 20:30
```

## Possiveis erros comuns

- `javac nao reconhecido`: o JDK nao foi instalado corretamente ou o `PATH` nao foi configurado.
- Erro ao cadastrar partida: os clubes escolhidos precisam estar cadastrados dentro do campeonato.
- Erro ao apostar: a partida pode estar com menos de 20 minutos para comecar.
- Erro ao adicionar participante: pode ter sido atingido o limite do grupo ou do sistema.

## Sugestao para a apresentacao em video

No video, vale mostrar:

1. A tela principal do sistema.
2. O cadastro de grupos e participantes.
3. O cadastro de clubes e campeonato.
4. O cadastro de partidas.
5. O registro de apostas.
6. O registro do resultado real.
7. A classificacao final.
8. Trechos do codigo destacando:
   - classe abstrata
   - interface
   - heranca
   - polimorfismo
   - regra de pontuacao
   - regra dos 20 minutos

## Integrantes

Preencher com:

- Nome completo dos alunos
- Matricula dos alunos
- Link do repositorio no GitHub
- Link do video de apresentacao
