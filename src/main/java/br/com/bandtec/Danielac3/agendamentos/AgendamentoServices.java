package br.com.bandtec.Danielac3.agendamentos;

import br.com.bandtec.Danielac3.auxiliares.FilaObj;
import br.com.bandtec.Danielac3.dominios.Arquivo;
import br.com.bandtec.Danielac3.dominios.Autor;
import br.com.bandtec.Danielac3.dominios.Livro;
import br.com.bandtec.Danielac3.repositorios.ArquivoRepository;
import br.com.bandtec.Danielac3.repositorios.AutorRepository;
import br.com.bandtec.Danielac3.repositorios.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AgendamentoServices {

    public static FilaObj<String> filaUUID = new FilaObj<String>(50);

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Scheduled(fixedRate = 10000)
    public ResponseEntity lerArquivo() {
        if(filaUUID.isEmpty()){
            System.out.println("Não existe");
            return ResponseEntity.status(204).build();
        }
        String uuid = filaUUID.pool();
        Optional<Arquivo> optionalArquivo = arquivoRepository.findById(uuid);
        if(!optionalArquivo.isPresent()){
            return ResponseEntity.status(201).body("tente novamento em 5 minuto");
        }
        Arquivo arquivo = optionalArquivo.get();

        String[] conteudo = arquivo.getConteudo().split(";|\\r\\n");;
        String mensagem = leArquivoRecursivo(conteudo, 0);
        if(!mensagem.equals("Livro adicionado com sucesso")){
            return ResponseEntity.status(400).body(mensagem);
        }
        arquivo.setResultado(mensagem);
        arquivoRepository.save(arquivo);
        return ResponseEntity.status(200).body(mensagem);

    }

    private String leArquivoRecursivo(String[] conteudo, int i) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (conteudo[i].startsWith("01")) {
            return "Livro adicionado com sucesso";
        }else if (conteudo[i].startsWith("02")) {
            Livro livro = new Livro();
            String linha = conteudo[i];
            try {
                LocalDate dataDeLancamento = LocalDate.parse(linha.substring(49,59), formatter);
                livro.setTitulo(linha.substring(2, 42).trim());
                livro.setPreco(Double.parseDouble(linha.substring(42, 49).replace(",", ".")));
                String nomeAutor = linha.substring(59, 99).trim();
                livro.setDataDeLancamento(dataDeLancamento);
                livro.setAutor(autorRepository.findByNome(nomeAutor)
                        .get());
                livroRepository.save(livro);
                return leArquivoRecursivo(conteudo, i + 1);
            }catch (StringIndexOutOfBoundsException e){
                return "Erro na formatação da linha " +  (i + 1) + ": " + e.getMessage();
            }

        }
        else if  (conteudo[i].startsWith("03")){
            String linha = conteudo[i];
            String nome = linha.substring(2,42).trim();
            try {
                if (autorRepository.findByNome(nome).isPresent()) {
                    return "Nome do autor da linha " + i  + " já registrado";
                }
                Autor autor = new Autor();

                LocalDate date = LocalDate.parse(linha.substring(42, 52), formatter);
                autor.setDataDeNascimento(date);
                autor.setNome(nome);
                autorRepository.save(autor);
                return leArquivoRecursivo(conteudo, i + 1);
            } catch (StringIndexOutOfBoundsException e){
                return "Erro na formatação da linha " + (i + 1) + ": " + e.getMessage();
            }
        }
        else {
            return leArquivoRecursivo(conteudo, i + 1);
        }


    }
}
