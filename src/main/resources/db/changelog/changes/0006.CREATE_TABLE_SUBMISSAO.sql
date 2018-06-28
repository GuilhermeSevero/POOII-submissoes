create table submissao (
  id bigint auto_increment not null,
  usuario_id bigint not null,
  evento_id bigint not null,
  titulo varchar(255) not null,
  resumo varchar(255) not null,
  data_submissao timestamp not null,
  nome_artigo varchar(255) not null,
  primary key (id),
  foreign key (usuario_id) references user(id),
  foreign key (evento_id) references evento(id)
)