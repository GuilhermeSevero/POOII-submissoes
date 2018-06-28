package br.edu.ulbra.submissoes.input;

import br.edu.ulbra.submissoes.model.User;

public class EventoInput {
    private Long id;
    private String nome;
    private String data;
    private String dataAbertura;
    private String dataFechamento;
    private User usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        if (data != null) {
            this.data = data.replaceFirst(" ", "T");
        }
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
        if (dataAbertura != null) {
            this.dataAbertura = dataAbertura.replaceFirst(" ", "T");
        }
    }

    public String getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(String dataFechamento) {
        this.dataFechamento = dataFechamento;
        if (dataFechamento != null) {
            this.dataFechamento = dataFechamento.replaceFirst(" ", "T");
        }
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
