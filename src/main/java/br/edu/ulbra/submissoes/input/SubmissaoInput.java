package br.edu.ulbra.submissoes.input;

import br.edu.ulbra.submissoes.model.Evento;
import br.edu.ulbra.submissoes.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class SubmissaoInput {
    private Long id;
    private Evento evento;
    private User usuario;
    private String titulo;
    private String resumo;
    private Date dataSubmissao;
    private MultipartFile artigo;
    private String nomeArtigo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Date getDataSubmissao() {
        return dataSubmissao;
    }

    public void setDataSubmissao(Date dataSubmissao) {
        this.dataSubmissao = dataSubmissao;
    }

    public MultipartFile getArtigo() {
        return artigo;
    }

    public void setArtigo(MultipartFile artigo) {
        this.artigo = artigo;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getNomeArtigo() {
        return nomeArtigo;
    }

    public void setNomeArtigo(String nomeArtigo) {
        this.nomeArtigo = nomeArtigo;
    }
}
