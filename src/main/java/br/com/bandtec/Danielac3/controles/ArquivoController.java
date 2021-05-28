package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.agendamentos.AgendamentoServices;
import br.com.bandtec.Danielac3.dominios.Arquivo;
import br.com.bandtec.Danielac3.repositorios.ArquivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("uploads")
public class ArquivoController {


    @Autowired
    private ArquivoRepository repository;

    @PostMapping
    public ResponseEntity postArquivo(@RequestParam MultipartFile arquivo) throws IOException {
        Arquivo upload = new Arquivo();
        if (arquivo.getContentType().equals("text/plain")) {
            upload.setConteudo(new String(arquivo.getBytes()));
            upload.setResultado("");
            upload.setUuid(UUID.randomUUID().toString());
            repository.save(upload);
            AgendamentoServices.filaUUID.insert(upload.getUuid());
            return ResponseEntity.status(200).body("Espere até o acabar o processamento o seu protocolo é " +
                    upload.getUuid());
        } else {
            return ResponseEntity.status(400).body("Não é um arquivo TXT");
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity getArquivoById(@PathVariable String uuid){
        Optional<Arquivo> arquivo = repository.findById(uuid);
        if(arquivo.isPresent()){
            return ResponseEntity.status(200).body(arquivo.get().getResultado());
        }

        return ResponseEntity.status(400).body("Ainda não está pronto");

    }

}
