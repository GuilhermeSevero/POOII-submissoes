create table evento (
  id bigint auto_increment not null,
  nome varchar(255) not null,
  usuario_id bigint not null,
  data timestamp not null,
  data_abertura timestamp,
  data_fechamento timestamp,
  primary key (id),
  foreign key (usuario_id) references user(id)
)