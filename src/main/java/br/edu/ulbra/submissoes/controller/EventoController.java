package br.edu.ulbra.submissoes.controller;

import br.edu.ulbra.submissoes.exception.EventoException;
import br.edu.ulbra.submissoes.input.EventoInput;
import br.edu.ulbra.submissoes.model.Evento;
import br.edu.ulbra.submissoes.service.EventoService;
import br.edu.ulbra.submissoes.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/evento")
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    UserService userService;

    @Autowired
    EventoController(EventoService eventoService){
        this.eventoService = eventoService;
    }

    @GetMapping()
    public ModelAndView listarEventos() {
        ModelAndView mv = new ModelAndView("evento/listar");
        mv.addObject("eventos", eventoService.findAll());
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novoEvento(@ModelAttribute("evento") EventoInput eventoInput) {
        ModelAndView mv = new ModelAndView("evento/novo");
        mv.addObject("evento", eventoInput);
        return mv;
    }

    @PostMapping()
    public String criarEvento(EventoInput eventoInput, RedirectAttributes redirectAttributes) {
        eventoInput.setId(null);
        try {
            Evento evento = eventoService.save(eventoInput, false);
            redirectAttributes.addFlashAttribute("SUCESSO", "Evento criado com sucesso.");
            return "redirect:/evento/" + evento.getId();
        } catch (EventoException e) {
            redirectAttributes.addFlashAttribute("ERRO", e.getMessage());
            redirectAttributes.addFlashAttribute("evento", eventoInput);
            return "redirect:/evento/novo";
        }
    }

    @GetMapping("/{id}")
    public ModelAndView listarDetalhesDoEvento(@PathVariable("id") Long id, RedirectAttributes redirectAttrs) {
        try {
            ModelAndView mv = new ModelAndView("evento/detalhes");
            EventoInput eventoInput = eventoService.getInputDetalhes(id);

            boolean podeAlterar = eventoInput.getUsuario().getId() == userService.getUsuarioLogado().getId();

            mv.addObject("evento", eventoInput);
            mv.addObject("podeAlterar", podeAlterar);
            return mv;
        } catch (EventoException e) {
            redirectAttrs.addFlashAttribute("ERRO", e.getMessage());
            return new ModelAndView("redirect:/evento");
        }
    }

    @PostMapping("/{id}")
    public String atualizarEvento(@PathVariable("id") Long id,
                                  EventoInput eventoInput,
                                  RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.save(eventoInput, true);
            redirectAttributes.addFlashAttribute("SUCESSO", "Evento alterado com sucesso.");
            return "redirect:/evento/" + evento.getId();
        } catch (EventoException e) {
            redirectAttributes.addFlashAttribute("ERRO", e.getMessage());
            redirectAttributes.addFlashAttribute("evento", eventoInput);
            return "redirect:/evento/" + id;
        }
    }

    @GetMapping("/{id}/delete")
    public String excluirEvento(@PathVariable("id") Long id, RedirectAttributes redirectAttrs) {
        try {
            eventoService.delete(id);
            redirectAttrs.addFlashAttribute("SUCESSO", "Evento excluido com sucesso.");
            return "redirect:/evento";
        } catch (EventoException e) {
            redirectAttrs.addFlashAttribute("ERRO", e.getMessage());
            return "redirect:/evento";
        }
    }
}
