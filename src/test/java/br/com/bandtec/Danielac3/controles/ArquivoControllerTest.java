package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.dominios.Arquivo;
import br.com.bandtec.Danielac3.repositorios.ArquivoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ArquivoControllerTest {

    @Autowired
    ArquivoController controller;

    @MockBean
    ArquivoRepository repository;


    @Test
    @DisplayName("Post /uploads - Quando o arquivo tem o formato certo - status 200")
    void postArquivo() throws IOException {
    MockMultipartFile upload = new MockMultipartFile( "Teste.txt","upload.txt",
            "text/plain",
            ("00LIVRO0220101123-05-202114:11:1001\n" +
                "03J. K. Rownling                          11/11/1950\n" +
                "02A Volta dos que não foram               0100,0011/11/2002J. K. Rownling" +
                "01002").getBytes());
        ResponseEntity respota = controller.postArquivo(upload);
        assertEquals(200, respota.getStatusCodeValue());
    }

    @Test
    @DisplayName("Post /uploads - Quando o arquivo tem o formato errado - status 400")
    void postArquivoErrado() throws IOException {
        MockMultipartFile upload = new MockMultipartFile( "Teste.txt",
                "upload.pdf",
                "application/pdf",
                ("00LIVRO0220101123-05-202114:11:1001\n" +
                        "03J. K. Rownling                          11/11/1950\n" +
                        "02A Volta dos que não foram               0100,0011/11/2002J. K. Rownling" +
                        "01002").getBytes());
        ResponseEntity respota = controller.postArquivo(upload);
        assertEquals(400, respota.getStatusCodeValue());
    }

    @Test
    @DisplayName("Get /uploads/{uuid} - Existe o UUID na lista - status 200")
    void getArquivoByIdExistente() {

        Arquivo arquivo = new Arquivo();
        arquivo.setUuid("AAAAAA");
        arquivo.setConteudo("123");
        arquivo.setResultado("Foi");
        repository.save(arquivo);
        Mockito.when(repository.findById(arquivo.getUuid())).thenReturn(Optional.of(arquivo));

        ResponseEntity respota = controller.getArquivoById(arquivo.getUuid());
        assertEquals(200, respota.getStatusCodeValue());
        assertEquals("Foi", arquivo.getResultado());
    }

    @Test
    @DisplayName("Get /uploads/{uuid} - Não existe UUID - status 400")
    void getArquivoByIdErrado() {

        Arquivo arquivo = new Arquivo();
        arquivo.setUuid("AAAAAA");
        arquivo.setConteudo("ABC");
        arquivo.setResultado("Não Foi");
        repository.save(arquivo);
        Mockito.when(repository.findById(arquivo.getUuid())).thenReturn(Optional.empty());

        ResponseEntity respota = controller.getArquivoById(arquivo.getUuid());
        assertEquals(400, respota.getStatusCodeValue());
        assertEquals("Ainda não está pronto", respota.getBody());
    }
}