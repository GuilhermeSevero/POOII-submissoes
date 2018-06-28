package br.edu.ulbra.submissoes.service;

import br.edu.ulbra.submissoes.exception.EventoException;
import br.edu.ulbra.submissoes.input.EventoInput;
import br.edu.ulbra.submissoes.model.Evento;
import br.edu.ulbra.submissoes.model.User;
import br.edu.ulbra.submissoes.repository.EventoRepository;
import br.edu.ulbra.submissoes.repository.UserRepository;
import br.edu.ulbra.submissoes.service.interfaces.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    private final ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @Autowired
    SubmissaoService submissaoService;

    @Autowired
    public EventoService(EventoRepository eventoRepository, ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> findAll(){
        return (List<Evento>) this.eventoRepository.findAll();
    }

    public Evento findById(Long id) throws EventoException {
        Optional<Evento> optional = eventoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EventoException("Evento não existe");
    }

    public List<Evento> findByUsuario(User user) { return eventoRepository.findByUsuario(user); }

    public List<Evento> findByUsuarioLogado() {
        return this.findByUsuario(userService.getUsuarioLogado());
    }

    public List<Evento> findByUsuarioLogadoOrderByDataDesc() {
        return eventoRepository.findByUsuarioOrderByDataDesc(userService.getUsuarioLogado());
    }
    public List<Evento> findFirst5ByUsuarioLogadoOrderByDataDesc() {
        return eventoRepository.findFirst5ByUsuarioOrderByDataDesc(userService.getUsuarioLogado());
    }

    public EventoInput getInputDetalhes(Long id) throws EventoException {
        Evento evento = this.findById(id);
        EventoInput input = new EventoInput();
        modelMapper.map(evento, input);
        return input;
    }

    public Evento save(EventoInput eventoInput, boolean isUpdate) throws EventoException {
        if (isUpdate) {
            Evento evento = this.findById(eventoInput.getId());
            if (!userService.pertenceAoUsuarioLogado(evento.getUsuario())) {
                throw new EventoException("Não pode alterar evento de outro usuário!");
            }
        }

        Evento evento = this.myMapper(eventoInput);

        if(evento.getDataAbertura().after(evento.getDataFechamento())) {
            throw new EventoException("Data de fechamento deve ser posterior à data de abertura!");
        }

        return eventoRepository.save(evento);
    }

    public void delete(Long id) throws EventoException{
        Evento evento = this.findById(id);
        if (!userService.pertenceAoUsuarioLogado(evento.getUsuario())) {
            throw new EventoException("Não pode excluir evento de outro usuário!");
        }
        if (submissaoService.findByEvento(evento).size() > 0){
            throw new EventoException("Não pode excluir evento que possua submissões!");
        }
        eventoRepository.delete(evento);
    }

    public boolean estaNoPeriodo(Long idEvento) throws EventoException {
        Evento evento = this.findById(idEvento);
        return evento.getDataFechamento().after(new Date());
    }

    private Evento myMapper(EventoInput eventoInput) {
        Evento evento = new Evento();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        evento.setId(eventoInput.getId());
        evento.setNome(eventoInput.getNome());
        evento.setUsuario(userService.getUsuarioLogado());

        // Gambis na área...
        try {
            evento.setData(formatter.parse(eventoInput.getData().replaceAll("Z$", "+0000")));
        } catch (ParseException | NullPointerException e) {
            evento.setData(new Date());
        }
        try {
            evento.setDataAbertura(formatter.parse(eventoInput.getDataAbertura().replaceAll("Z$", "+0000")));
        } catch (NullPointerException | ParseException e) {
            evento.setDataAbertura(new Date());
        }
        try {
            evento.setDataFechamento(formatter.parse(eventoInput.getDataFechamento().replaceAll("Z$", "+0000")));
        } catch (NullPointerException | ParseException e) {
            evento.setDataFechamento(null);
        }
        return evento;
    }
}
