CREATE TABLE table_sequence(
    id_table     VARCHAR(100)    NOT NULL,
    key_value    INT             NOT NULL,
    PRIMARY KEY (id_table)
);

CREATE TABLE test
(
   chiave integer PRIMARY KEY not null,
   stringa varchar(45) not null,
   importo decimal(12,2),
   qty integer,
   data date not null,
   dataora TIMESTAMP not null
);
