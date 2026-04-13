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

## Estrutura principal

- `GerenciadorApostas.java`: classe principal para iniciar o sistema.
- `Sistema.java`: camada central de regras de negocio.
- `TelaPrincipal.java`: janela principal da aplicacao.
- `PainelParticipantes.java`: cadastro de grupos e participantes.
- `PainelApostas.java`: cadastro de clubes, campeonatos, partidas e apostas.
- `PainelResultados.java`: registro dos resultados das partidas.
- `PainelClassificacao.java`: exibicao da classificacao.


## Como rodar pelo VS Code

1. Instale o `Visual Studio Code`.
2. Instale a extensao `Extension Pack for Java`.
3. Abra a pasta do projeto no VS Code.
4. Com o JDK instalado, o VS Code deve reconhecer o projeto automaticamente.
5. Abra o arquivo `GerenciadorApostas.java`.
6. Clique em `Run` ou `Executar`.

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

## Integrantes

- Bianca Medina RA: 842786
- Bárbara Garcia de Figueiredo  RA: 841308
- Carlos Edurdo Michelle Marques RA: 842844
