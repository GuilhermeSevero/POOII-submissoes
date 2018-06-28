package br.edu.ulbra.submissoes.controller;

import br.edu.ulbra.submissoes.exception.UserException;
import br.edu.ulbra.submissoes.input.UserInput;
import br.edu.ulbra.submissoes.model.User;
import br.edu.ulbra.submissoes.service.SecurityServiceImpl;
import br.edu.ulbra.submissoes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    SecurityServiceImpl securityService;

    private final UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/detalhes-usuario-logado")
    public String detalhesUsuarioLogado() {
        return "redirect:/usuario/" + securityService.findLoggedInUser().getId();
    }

    @GetMapping("/{id}")
    public ModelAndView listarUsuario(@PathVariable("id") Long userId){
        ModelAndView mv = new ModelAndView("usuario/detalhes");
        try {
            UserInput user = userService.getInputDetalhes(userId);
            mv.addObject("user", user);
        } catch (UserException e){
            mv.addObject("ERRO", e.getMessage());
            mv.addObject("user", null);
        }
        return mv;
    }

    @PostMapping("/{id}")
    public String alterarUsuario(@PathVariable("id") Long userId,
                                 UserInput userInput,
                                 RedirectAttributes redirectAttrs) {
        try {
            User usuario = userService.save(userInput, true);
            redirectAttrs.addFlashAttribute("SUCESSO", "Usuario alterado com sucesso.");
            return "redirect:/usuario/" + usuario.getId();
        }catch (UserException e) {
            redirectAttrs.addFlashAttribute("ERRO", e.getMessage());
            redirectAttrs.addFlashAttribute("user", userInput);
            return "redirect:/usuario/" + userId;
        }
    }

    @PostMapping("/cadastro")
    public String insertUsuario(UserInput userInput, RedirectAttributes redirectAttrs) {
        if (userService.getUsuarioLogado() != null){
            redirectAttrs.addFlashAttribute("ERRO", "Não é possível criar novo usuário já estando logado!");
            return "redirect:/usuario/detalhes-usuario-logado";
        }

        userInput.setId(null);
        try {
            userService.save(userInput, false);
        } catch (UserException e) {
            redirectAttrs.addFlashAttribute("ERRO", e.getMessage());
            redirectAttrs.addFlashAttribute("user", userInput);
            return "redirect:/usuario/cadastro";
        }
        return "redirect:/";
    }

    @GetMapping("/cadastro")
    public ModelAndView formularioCadastro(@ModelAttribute UserInput userInput){
        if (userService.getUsuarioLogado() != null){
            ModelAndView mv = this.listarUsuario(userService.getUsuarioLogado().getId());
            mv.addObject("ERRO", "Não é possível criar novo usuário já estando logado!");
            return mv;
        }
        ModelAndView mv = new ModelAndView("usuario/registro");
        mv.addObject("user", userInput);
        return mv;
    }
}
