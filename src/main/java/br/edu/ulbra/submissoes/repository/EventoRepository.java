package br.edu.ulbra.submissoes.repository;

import br.edu.ulbra.submissoes.model.Evento;
import br.edu.ulbra.submissoes.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventoRepository extends CrudRepository<Evento, Long> {
    List<Evento> findByUsuario(User user);
    List<Evento> findByUsuarioOrderByDataDesc(User usuario);
    List<Evento> findFirst5ByUsuarioOrderByDataDesc(User usuario);

}
