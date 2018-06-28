package br.edu.ulbra.submissoes.controller;

import br.edu.ulbra.submissoes.exception.EventoException;
import br.edu.ulbra.submissoes.model.User;
import br.edu.ulbra.submissoes.service.EventoService;
import br.edu.ulbra.submissoes.service.SecurityServiceImpl;
import br.edu.ulbra.submissoes.service.SubmissaoService;
import br.edu.ulbra.submissoes.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Paths;

@Controller
public class HomeController {

    @Value("${submissao.uploadFilePath}")
    private String uploadFilePath;

    private final EventoService eventoService;

    private final SubmissaoService submissaoService;


    @Autowired
    UserService userService;

    @Autowired
    HomeController(EventoService eventoService, SubmissaoService submissaoService){
        this.eventoService = eventoService;
        this.submissaoService = submissaoService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");

        mv.addObject("eventos", eventoService.findFirst5ByUsuarioLogadoOrderByDataDesc());
        mv.addObject("submissoes", submissaoService.findFirst5ByUsuarioLogadoOrderByDataSubmissaoDesc());
        mv.addObject("user", userService.getUsuarioLogado());

        return mv;
    }
    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView("usuario/login");
        return mv;
    }

    @GetMapping("/artigos/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = new FileSystemResource(Paths.get(uploadFilePath, filename).toFile());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename().substring(37) + "\"").body(file);
    }
}
