package br.edu.ulbra.submissoes.service;

import br.edu.ulbra.submissoes.exception.SubmissaoException;
import br.edu.ulbra.submissoes.input.SubmissaoInput;
import br.edu.ulbra.submissoes.model.Evento;
import br.edu.ulbra.submissoes.model.Submissao;
import br.edu.ulbra.submissoes.model.User;
import br.edu.ulbra.submissoes.repository.SubmissaoRepository;
import br.edu.ulbra.submissoes.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmissaoService {

    @Value("${submissao.uploadFilePath}")
    private String uploadFilePath;

    private final SubmissaoRepository submissaoRepository;

    private final ModelMapper modelMapper;
    @Autowired
    UserService userService;

    @Autowired
    public SubmissaoService(SubmissaoRepository submissaoRepository, ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.submissaoRepository = submissaoRepository;
    }

    @Autowired
    UserRepository userRepository;

    public List<Submissao> findAll(){
        return (List<Submissao>) submissaoRepository.findAll();
    }

    public List<Submissao> findByUsuario(User user){ return submissaoRepository.findByUsuario(user); }

    public List<Submissao> findByUsuarioLogado() {
        return this.findByUsuario(userService.getUsuarioLogado());
    }

    public List<Submissao> findByUsuarioLogadoOrderByDataSubmissaoDesc() {
        return submissaoRepository.findByUsuarioOrderByDataSubmissaoDesc(userService.getUsuarioLogado());
    }
    public List<Submissao> findFirst5ByUsuarioLogadoOrderByDataSubmissaoDesc() {
        return submissaoRepository.findFirst5ByUsuarioOrderByDataSubmissaoDesc(userService.getUsuarioLogado());
    }

    public List<Submissao> findByEvento(Evento evento) {
        return submissaoRepository.findByEvento(evento);
    }

    public Submissao findById(Long id) throws SubmissaoException {
        Optional<Submissao> optional = submissaoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new SubmissaoException("Submissão não existe");
    }
    public SubmissaoInput getInputDetalhes(Long id) throws SubmissaoException {
        Submissao submissao = this.findById(id);
        SubmissaoInput input = new SubmissaoInput();
        modelMapper.map(submissao, input);
        return input;
    }

    public Submissao save(SubmissaoInput submissaoInput, boolean isUpdate) throws SubmissaoException{
        Submissao submissao = this.modelMapper.map(submissaoInput, Submissao.class);

        if (isUpdate) {
            if (!userService.pertenceAoUsuarioLogado(submissao.getUsuario())) {
                throw new SubmissaoException("Não pode alterar submissões de outro usuário!");
            }
            Submissao submissaoAnterior = this.findById(submissaoInput.getId());
            submissao.setNomeArtigo(submissaoAnterior.getNomeArtigo());
            submissao.setEvento(submissaoAnterior.getEvento());
        }

        submissao.setUsuario(userService.getUsuarioLogado());

        MultipartFile artigoFile = submissaoInput.getArtigo();
        if (artigoFile.getSize() > 0) {
            File folderPath = new File(uploadFilePath);
            folderPath.mkdirs();

            String fileName = UUID.randomUUID().toString() + "-" + artigoFile.getOriginalFilename();
            File file = new File(Paths.get(uploadFilePath, fileName).toString());

            try {
                if (!file.createNewFile()) {
                    throw new SubmissaoException("Arquivo de upload nao pode ser criado.");
                }
                artigoFile.transferTo(file);
                submissao.setNomeArtigo(fileName);
            } catch (IOException e) {
                throw new SubmissaoException(e.getMessage());
            }
        }
        return submissaoRepository.save(submissao);
    }

    public void delete(Long id) throws SubmissaoException {
        Submissao submissao = this.findById(id);
        if (!userService.pertenceAoUsuarioLogado(submissao.getUsuario())) {
            throw new SubmissaoException("Não pode excluir submissões de outro usuário!");
        }
        this.submissaoRepository.delete(submissao);
    }
}
