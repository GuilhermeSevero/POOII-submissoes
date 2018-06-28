package br.edu.ulbra.submissoes.service.interfaces;

import br.edu.ulbra.submissoes.model.User;

public interface SecurityService {
    String findLoggedInUsername();

    User findLoggedInUser();

    void autologin(String username, String password);
}
