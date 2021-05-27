package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.agendamentos.AgendamentoServices;
import br.com.bandtec.Danielac3.dominios.Arquivo;
import br.com.bandtec.Danielac3.dominios.Autor;
import br.com.bandtec.Danielac3.dominios.Livro;
import br.com.bandtec.Danielac3.auxiliares.PilhaObj;
import br.com.bandtec.Danielac3.auxiliares.Processo;
import br.com.bandtec.Danielac3.repositorios.AutorRepository;
import br.com.bandtec.Danielac3.repositorios.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
public class LivroController {
    @Autowired
    private LivroRepository repository;

    @Autowired
    private AutorRepository autorRepository;

    private PilhaObj<Processo> pilhaObj = new PilhaObj(50);

    @GetMapping
    public ResponseEntity getLivros() {
        List<Livro> livros = repository.findAll();
        if (!livros.isEmpty()) {
            return ResponseEntity.status(200).body(livros);
        } else {
            return ResponseEntity.status(204).body("Não foi encontrado nenhum livro");
        }
    }

    @PostMapping
    public ResponseEntity postLivro(@RequestBody @Valid Livro novoLivro) {
        if (autorRepository.existsById(novoLivro.getAutor().getId())) {
            pilhaObj.push(new Processo("POST", novoLivro));
            repository.save(novoLivro);
            return ResponseEntity.status(201).body("Autor inserido com sucesso");
        } else {
            return ResponseEntity.status(400).body("Autor não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLivro(@PathVariable Integer id) {
        Optional<Livro> optionalLivro = repository.findById(id);
        if (optionalLivro.isPresent()) {
            pilhaObj.push(new Processo("DELETE", optionalLivro.get()));
            repository.delete(optionalLivro.get());
            return ResponseEntity.status(200).body("O Livro foi deletado");
        } else {
            return ResponseEntity.status(204).body("Livro não encontrado");
        }
    }

    @PostMapping("/desfazer")
    public ResponseEntity getDesfazer() {
        if (pilhaObj.isEmpty()) {
            return ResponseEntity.status(400).body("Pilha vazia!");
        }
        Processo processo = pilhaObj.pop();
        if (processo.getProtocolo().equals("DELETE")) {
            repository.save((Livro) processo.getObjeto());
            return ResponseEntity.status(201).body("Autor inserido com sucesso");
        } else {
            repository.delete(((Livro) processo.getObjeto()));
            return ResponseEntity.status(201).body("O Livro foi deletado");
        }
    }

}
