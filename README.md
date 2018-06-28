# POOII-submissoes
Trabalho final G2 de POO-II com o prof Tales


# Aplicação para submissão de Artigos Científicos

## Definição
O trabalho consiste em desenvolver uma aplicação para que possa ser possível realizar a submissão de artigos científicos para um determinado evento.

## Cadastros
A aplicação deverá permitir cadastros de:
* Usuários
Usuários que irão utilizar a aplicação. Os usuários deverão possuir um username, um e-mail, um nome e uma senha que deve ser armazenada criptografada na base de dados.
* Eventos
Eventos para os quais os artigos serão submetidos. Os eventos deverão possuir um usuário responsável, um nome, uma data, uma data de abertura de incrições e uma data de fechamento de inscrições.
* Submissões
Submissões de artigos para um determinado evento. Os artigos submetidos deverão possuir um evento, um usuário, o título do artigo, um resumo do artigo, a data de submissão e o arquivo contendo o texto do artigo.

## Regras de Negócio
* Qualquer pessoa pode se cadastrar na aplicação, através de um formulário a ser exibido na página inicial.
* Qualquer usuário cadastrado e logado na aplicação pode criar um evento.
* Qualquer usuário cadastrado e logado pode enviar submissões de artigos para um evento, desde que esteja no período correto de abertura de submissões.

## Rotas do Site
* GET / - Página inicial do site. Deve exibir os eventos que o usuário criou e os eventos aos quais o usuário submeteu os arquivos. Além disso, deve exibir links para: edição do perfil, busca de eventos, criação de eventos
* GET /evento - Página que lista todos os eventos que o usuário criou
* GET /evento/{id} - Página que lista os detalhes de um determinado evento e um link para a submissão de artigo. Caso o evento tenha sido criado pelo usuário corrente, permite a edição do evento.
* POST /evento - Cria um novo evento e redireciona para a página de detalhes do evento
* POST /evento/{id} - Atualiza os detalhes de um evento e redireciona para a página de detalhes do evento
* GET /evento/{id}/delete - Exclui um evento e redireciona para a página de listagem de eventos do usuário

* GET /usuario/{id} - Página que lista os detalhes de um usuário e permite a edição mesmo.
* POST /usuario/{id} - Atualiza os detalhes de um usuário e redireciona para a página de detalhes do evento
* GET /usuario/cadastro - Página que exibe o formulário de cadastro de usuário (apenas se não está logado)
* POST /usuario/cadastro - Página que cadsatra um novo usuário (apenas se não está logado) e redireciona para a página inicial do site

* GET /submissoes - Página que lista todos os eventos para os quais o usuário submeteu um artigo, em ordem decrescente de data de submissão
* GET /submissoes/{id} - Página que lista todos os detalhes de uma determinada edição e um link para ver os detalhes do evento para o qual foi submetido. Permite também a edição da submissão
* POST /submissoes/{id} - Atualiza os detalhes de uma submissão e redireciona para a página e detalhes da submissão
* POST /submissoes/{id}/delete - Exclui uma submissão e redireciona para a lista de submissões
* GET /submissoes/evento/{idEvento} - Página com o formulário de submissão de artigo
* POST /submissoes/evento/{idEvento} - Cria uma nova submissão e redireciona para a página de detalhes da submissão

## Login do Site
O login do site será feito utilizando o Spring Security. As rotas de login/logout são definidas automaticamente por ele.
