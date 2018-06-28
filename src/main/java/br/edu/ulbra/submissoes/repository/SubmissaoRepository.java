package br.edu.ulbra.submissoes.repository;

import br.edu.ulbra.submissoes.model.Evento;
import br.edu.ulbra.submissoes.model.Submissao;
import br.edu.ulbra.submissoes.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubmissaoRepository extends CrudRepository<Submissao, Long> {
    List<Submissao> findByUsuario(User usuario);
    List<Submissao> findByUsuarioOrderByDataSubmissaoDesc(User usuario);
    List<Submissao> findFirst5ByUsuarioOrderByDataSubmissaoDesc(User usuario);
    List<Submissao> findByEvento(Evento evento);
}
