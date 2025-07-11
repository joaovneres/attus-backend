CREATE TABLE processo (
                          id              BIGSERIAL PRIMARY KEY,
                          numero          VARCHAR(30)  NOT NULL,
                          data_abertura   DATE         NOT NULL,
                          descricao       TEXT,
                          status          VARCHAR(15)  NOT NULL,
                          ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
                          CONSTRAINT uk_numero_processo UNIQUE (numero)
);

CREATE INDEX idx_processo_status          ON processo (status);
CREATE INDEX idx_processo_data_abertura   ON processo (data_abertura);

CREATE TABLE parte (
                       id            BIGSERIAL PRIMARY KEY,
                       processo_id   BIGINT       NOT NULL REFERENCES processo(id) ON DELETE CASCADE,
                       nome          VARCHAR(255) NOT NULL,
                       cpf_cnpj      VARCHAR(14)  NOT NULL,
                       tipo          VARCHAR(15)  NOT NULL,
                       email         VARCHAR(255),
                       telefone      VARCHAR(30),
                       ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
                       CONSTRAINT uk_cpf_cnpj_processo UNIQUE (processo_id, cpf_cnpj)
);

CREATE INDEX idx_parte_cpf_cnpj ON parte (cpf_cnpj);

CREATE TABLE acao (
                      id             BIGSERIAL PRIMARY KEY,
                      processo_id    BIGINT      NOT NULL REFERENCES processo(id) ON DELETE CASCADE,
                      tipo           VARCHAR(15) NOT NULL,
                      data_registro  DATE        NOT NULL,
                      descricao      TEXT,
                      ativo          BOOLEAN     NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_acao_tipo ON acao (tipo);
