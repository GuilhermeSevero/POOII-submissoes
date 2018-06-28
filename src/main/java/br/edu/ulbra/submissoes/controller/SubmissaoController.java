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
@RequestMapping("/submissoes")
public class SubmissaoController {
    private final SubmissaoService submissaoService;

    private final EventoService eventoService;

    @Autowired
    SubmissaoController(SubmissaoService submissaoService, EventoService eventoService){
        this.eventoService = eventoService;
        this.submissaoService = submissaoService;
    }


    @GetMapping("")
    public ModelAndView listarSubmissoes(){
        ModelAndView mv = new ModelAndView("submissao/listar");
        mv.addObject("submissoes", submissaoService.findByUsuarioLogadoOrderByDataSubmissaoDesc());
        return mv;
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Página que lista todos os detalhes de uma determinada edição e um link para ver os detalhes do evento para o qual foi submetido. Permite também a edição da submissão")
    public ModelAndView listarDetalhesDaSubmissao(@PathVariable("id") Long id, RedirectAttributes redirectAttrs){
        try {
            ModelAndView mv = new ModelAndView("submissao/detalhes");
            Submissao submissao = submissaoService.findById(id);
            mv.addObject("submissao", submissao);
            return mv;
        } catch (SubmissaoException e) {
            redirectAttrs.addFlashAttribute("ERRO", e.getMessage());
            return new ModelAndView("redirect:/submissoes");
        }
    }

    @PostMapping("/{id}")
    public String atualizarSubmissao(@PathVariable("id") Long id,
                                     SubmissaoInput submissaoInput,
                                     RedirectAttributes redirectAttributes){
        try {
            submissaoInput.setEvento(submissaoInput.getEvento());
            submissaoInput.setDataSubmissao(new Date());

            Submissao submissao = submissaoService.save(submissaoInput, true);
            redirectAttributes.addFlashAttribute("SUCESSO", "Evento alterado com sucesso.");
            return "redirect:/submissoes/" + submissao.getId();
        } catch (SubmissaoException e) {
            redirectAttributes.addFlashAttribute("ERRO", e.getMessage());
            redirectAttributes.addFlashAttribute("submissao", submissaoInput);
            return "redirect:/submissoes/" + id;
        }
    }

    @GetMapping("/{id}/delete")
    public String excluirSubmissao(@PathVariable("id") Long id, RedirectAttributes redirectAttrs){
        try {
            submissaoService.delete(id);
            redirectAttrs.addFlashAttribute("SUCESSO", "Submissão excluida com sucesso.");
        } catch (SubmissaoException e) {
            redirectAttrs.addFlashAttribute("ERRO", e.getMessage());
        }
        return "redirect:/submissoes";
    }
}
