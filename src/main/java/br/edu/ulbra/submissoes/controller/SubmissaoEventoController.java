package br.edu.ulbra.submissoes.controller;

import br.edu.ulbra.submissoes.exception.EventoException;
import br.edu.ulbra.submissoes.exception.SubmissaoException;
import br.edu.ulbra.submissoes.input.SubmissaoInput;
import br.edu.ulbra.submissoes.model.Submissao;
import br.edu.ulbra.submissoes.service.EventoService;
import br.edu.ulbra.submissoes.service.SubmissaoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/submissoes/evento")
public class SubmissaoEventoController {

    private final SubmissaoService submissaoService;

    private final EventoService eventoService;

    @Autowired
    EventoController eventoController;

    @Autowired
    SubmissaoEventoController(EventoService eventoService, SubmissaoService submissaoService){
        this.submissaoService = submissaoService;
        this.eventoService = eventoService;
    }

    @GetMapping("/{idEvento}")
    public ModelAndView paginaSubmissao(@PathVariable("idEvento") Long idEvento,
                                        @ModelAttribute("submissao") SubmissaoInput submissaoInput,
                                        RedirectAttributes redirectAttrs) {

        ModelAndView mv = null;
        try {
            if (!eventoService.estaNoPeriodo(idEvento)) {
                mv = eventoController.listarDetalhesDoEvento(idEvento, redirectAttrs);
                mv.addObject("ERRO", "Você não pode submeter artigo em um evento fora do período!");
            } else {
                mv = new ModelAndView("submissao/novo");
                mv.addObject("idEvento", idEvento);
                mv.addObject("submissao", submissaoInput);
            }
        } catch (EventoException e) {
            mv = eventoController.listarDetalhesDoEvento(idEvento, redirectAttrs);
            mv.addObject("ERRO", e.getMessage());
        }
        return mv;

    }

    @PostMapping("/{idEvento}")
    public String submeterArtigo(@PathVariable("idEvento") Long idEvento,
                                 SubmissaoInput submissaoInput,
                                 RedirectAttributes redirectAttributes){
        try {
            if (!eventoService.estaNoPeriodo(idEvento)) {
                throw new EventoException("Você não pode submeter artigo em um evento fora do período!");
            }
            submissaoInput.setEvento(eventoService.findById(idEvento));
            submissaoInput.setDataSubmissao(new Date());

            Submissao submissao = submissaoService.save(submissaoInput, false);

            redirectAttributes.addFlashAttribute("SUCESSO", "Submetido com sucesso.");
            return "redirect:/submissoes/" + submissao.getId();

        } catch (SubmissaoException e) {
            redirectAttributes.addFlashAttribute("ERROR", e.getMessage());
            redirectAttributes.addFlashAttribute("submissao", submissaoInput);
            return "redirect:/submissoes/evento/" + idEvento;

        } catch (EventoException e) {
            redirectAttributes.addFlashAttribute("ERROR", e.getMessage());
            return "redirect:/evento/";
        }
    }
}
