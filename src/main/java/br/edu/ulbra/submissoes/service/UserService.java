package br.edu.ulbra.submissoes.service;

import br.edu.ulbra.submissoes.exception.UserException;
import br.edu.ulbra.submissoes.input.UserInput;
import br.edu.ulbra.submissoes.model.Role;
import br.edu.ulbra.submissoes.model.User;
import br.edu.ulbra.submissoes.repository.RoleRepository;
import br.edu.ulbra.submissoes.repository.UserRepository;
import liquibase.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SecurityServiceImpl securityService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User save(UserInput userInput, Boolean isUpdate) throws UserException {

        validarDados(userInput);

        if (isUpdate){
            User usuarioAnterior = this.findById(userInput.getId());
            if (!StringUtils.isEmpty(userInput.getPassword())) {
                userInput.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            } else {
                userInput.setPassword(usuarioAnterior.getPassword());
            }
        } else {
            if (this.findByUsername(userInput.getUsername()) != null) {
                throw new UserException("Login já cadastrado!");
            }
            if (this.findByEmail(userInput.getEmail()) != null) {
                throw new UserException("Email já cadastrado!");
            }
            userInput.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
        }

        User user;
        user = modelMapper.map(userInput, User.class);

        setUserRole(user);

        return userRepository.save(user);
    }

    private void setUserRole(User user) {
        Role role = roleRepository.findByName("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
    }

    private void validarDados(UserInput userInput) throws UserException {
        if (userInput == null){
            throw new UserException("Dados não informados");
        }
        if (StringUtils.isEmpty(userInput.getUsername())){
            throw new UserException("Username não informado");
        }
        if (StringUtils.isEmpty(userInput.getNome())){
            throw new UserException("Nome não informado");
        }
        if (StringUtils.isEmpty(userInput.getEmail())){
            throw new UserException("E-mail não informado");
        }
        if ((!StringUtils.isEmpty(userInput.getPassword())) && (!userInput.getPassword().equals(userInput.getPasswordConfirm()))){
            throw new UserException("Senhas nao conferem");
        }
    }

    public User findById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        } else {
            throw new UserException("Usuário não encontrado");
        }
    }

    public void delete(Long userId) throws UserException{
        User user = findById(userId);
        userRepository.delete(user);
    }

    public UserInput getInputDetalhes(Long id) throws UserException {
        UserInput input = new UserInput();
        modelMapper.map(this.findById(id), input);
        input.setPassword("");
        input.setPasswordConfirm("");
        return input;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUsuarioLogado() {
        return securityService.findLoggedInUser();
    }

    public boolean pertenceAoUsuarioLogado(User user) {
        return user.getId() == this.getUsuarioLogado().getId();
    }

}
