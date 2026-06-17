CREATE DATABASE IF NOT EXISTS projeto_java_poo;
USE projeto_java_poo;

CREATE TABLE IF NOT EXISTS clubes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL UNIQUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS campeonatos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL UNIQUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS grupos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL UNIQUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS participantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL UNIQUE,
    grupo_id INT NOT NULL,
    pontuacao INT NOT NULL DEFAULT 0,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (grupo_id) REFERENCES grupos(id)
);

CREATE TABLE IF NOT EXISTS campeonato_clubes (
    campeonato_id INT NOT NULL,
    clube_id INT NOT NULL,
    PRIMARY KEY (campeonato_id, clube_id),
    FOREIGN KEY (campeonato_id) REFERENCES campeonatos(id),
    FOREIGN KEY (clube_id) REFERENCES clubes(id)
);

CREATE TABLE IF NOT EXISTS partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    campeonato_id INT NOT NULL,
    clube_mandante_id INT NOT NULL,
    clube_visitante_id INT NOT NULL,
    data_hora DATETIME NOT NULL,
    gols_mandante INT NULL,
    gols_visitante INT NULL,
    UNIQUE KEY uk_partida (campeonato_id, clube_mandante_id, clube_visitante_id, data_hora),
    FOREIGN KEY (campeonato_id) REFERENCES campeonatos(id),
    FOREIGN KEY (clube_mandante_id) REFERENCES clubes(id),
    FOREIGN KEY (clube_visitante_id) REFERENCES clubes(id)
);

CREATE TABLE IF NOT EXISTS apostas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    participante_id INT NOT NULL,
    partida_id INT NOT NULL,
    gols_mandante INT NOT NULL,
    gols_visitante INT NOT NULL,
    data_registro DATETIME NOT NULL,
    UNIQUE KEY uk_aposta (participante_id, partida_id),
    FOREIGN KEY (participante_id) REFERENCES participantes(id),
    FOREIGN KEY (partida_id) REFERENCES partidas(id)
);

CREATE TABLE IF NOT EXISTS eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(40) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
