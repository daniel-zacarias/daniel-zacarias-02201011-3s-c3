package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.dominios.Autor;
import br.com.bandtec.Danielac3.dominios.Livro;
import br.com.bandtec.Danielac3.dominios.PilhaObj;
import br.com.bandtec.Danielac3.dominios.Processo;
import br.com.bandtec.Danielac3.repositorios.AutorRepository;
import br.com.bandtec.Danielac3.repositorios.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {
    @Autowired
    private LivroRepository repository;

    @Autowired
    private AutorRepository autorRepository;

    private PilhaObj<Processo> pilhaObj = new PilhaObj(20);

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

    @PostMapping("/upload")
    public ResponseEntity criarAnexo(@RequestParam MultipartFile arquivo) throws IOException {
        if (arquivo.getContentType().equals("text/plain")) {
            String[] conteudo = new String(arquivo.getBytes()).split(";|\\r\\n");
            if (!leArquivoRecursivo(conteudo, 0)) {
                return ResponseEntity.status(400).body("Autor não encontrado");
            }
            return ResponseEntity.status(201).body("Livros adicionados");
        } else {
            return ResponseEntity.status(204).body("Não é um arquivo TXT");
        }

    }

    private boolean leArquivoRecursivo(String[] conteudo, int i) {
        if (conteudo[i].startsWith("01")) {
            return true;
        }else if (conteudo[i].startsWith("02")) {
            Livro livro = new Livro();
            String linha = conteudo[i];

            livro.setTitulo(linha.substring(2, 42).trim());
            livro.setPreco(Double.parseDouble(linha.substring(42, 49).replace(",", ".")));
            String nome = linha.substring(49, 89).trim();
            livro.setAutor(autorRepository.findByNome(nome)
                    .get());
            postLivro(livro);
            return leArquivoRecursivo(conteudo, i + 1);
        }
        else if  (conteudo[i].startsWith("03")){
            String linha = conteudo[i];
            if(autorRepository.findByNome(linha.substring(2,41)).isPresent()){
                return leArquivoRecursivo(conteudo, i + 1);
            }
            Autor autor = new Autor();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(linha.substring(42,52), formatter);
            autor.setDataDeNascimento(date);
            autor.setNome(linha.substring(2,42).trim());
            autorRepository.save(autor);
            return leArquivoRecursivo(conteudo, i + 1);

        }
        else {
            return leArquivoRecursivo(conteudo, i + 1);
        }


    }
}
