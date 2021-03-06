package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.dominios.Autor;
import br.com.bandtec.Danielac3.auxiliares.PilhaObj;
import br.com.bandtec.Danielac3.auxiliares.Processo;
import br.com.bandtec.Danielac3.repositorios.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    AutorRepository repository;

    @GetMapping
    public ResponseEntity getAutor(){
        List<Autor> autorList = repository.findAll();
        if(autorList.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(repository.findAll());
    }

    PilhaObj<Processo> pilhaObj = new PilhaObj(20);

    @PostMapping
    public ResponseEntity postAutor(@RequestBody @Valid Autor novoAutor){
        repository.save(novoAutor);
        pilhaObj.push(new Processo("POST", novoAutor));
        return ResponseEntity.status(201).body("Autor adicionado com Sucesso");
    }

    @PutMapping
    public ResponseEntity putAutor(@RequestBody Autor autor){
        Autor AutorAtual = new Autor();

        Optional<Autor> autorOptional = repository.findById(autor.getId());
        if(autorOptional.isPresent()){
            AutorAtual.setId(repository.getOne(autor.getId()).getId());
            AutorAtual.setNome(repository.getOne(autor.getId()).getNome());
            AutorAtual.setDataDeNascimento(repository.getOne(autor.getId()).getDataDeNascimento());
            pilhaObj.push(new Processo("PUT",AutorAtual));
            repository.save(autor);
            return ResponseEntity.status(200).body(autorOptional.get());
        }else{
            return ResponseEntity.status(400).body("Autor não encontrado");
        }
    }

    @PostMapping("/desfazer")
    public ResponseEntity postDesfazer(){
        if(pilhaObj.isEmpty()){
            return ResponseEntity.status(400).body("Pilha vazia!");
        }
        Processo processo = pilhaObj.pop();
        if (processo.getProtocolo().equals("POST")) {
            repository.delete((Autor)processo.getObjeto());
            return ResponseEntity.status(201).body("Retirando Autor");
        } else {
            repository.save(((Autor) processo.getObjeto()));
            return ResponseEntity.status(200).body("Desfeita a alteração");
        }
    }
}
