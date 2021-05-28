package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.dominios.Autor;
import br.com.bandtec.Danielac3.dominios.Livro;
import br.com.bandtec.Danielac3.repositorios.AutorRepository;
import br.com.bandtec.Danielac3.repositorios.LivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AutorControllerTest {

    @Autowired
    AutorController controller;

    @MockBean
    AutorRepository repository;


    @Test
    @DisplayName("Get /autores - Quando tem autores no Banco - status 200 e tamanho 3")
    void getAutorCerto() {

        List<Autor> autorTeste = Arrays.asList(new Autor(), new Autor(), new Autor());
        Mockito.when(repository.findAll()).thenReturn(autorTeste);

        ResponseEntity<List<Livro>> resposta = controller.getAutor();


        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(3, resposta.getBody().size());
    }

    @Test
    @DisplayName("Get /autores - Quando não tem autores no Banco - status 204")
    void getAutorErrado() {
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<Autor>());

        ResponseEntity<List<Livro>> resposta = controller.getAutor();


        assertEquals(204, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Post /autores - Post de autor 1 - status 201")
    void postAutor1() {
        Autor autor = new Autor();
        autor.setId(1);
        autor.setNome("Alencar");
        autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
        Mockito.when(repository.existsById(autor.getId())).thenReturn(true);

        ResponseEntity<List<Livro>> resposta = controller.postAutor(autor);

        assertEquals(201, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Post /autores - Post de autor 2 - status 201")
    void postAutor2(){
            Autor autor = new Autor();
            autor.setId(10);
            autor.setNome("Clarice lispector");
            autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
            Mockito.when(repository.existsById(autor.getId())).thenReturn(true);

            ResponseEntity<List<Livro>> resposta = controller.postAutor(autor);

            assertEquals(201, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Put /autores - Alterar livro certo - status 201")
    void putAutorCerto() {
        Autor autor = new Autor();
        autor.setId(10);
        autor.setNome("Clarice lispector");
        autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
        Mockito.when(repository.findById(autor.getId())).thenReturn(Optional.of(autor));


        ResponseEntity<List<Livro>> resposta = controller.putAutor(autor);

        assertEquals(201, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Put /autores - Alteração de livro inexistente - status 201")
    void putAutorErrado() {
        Autor autor = new Autor();
        autor.setId(90);
        autor.setNome("Clarice lispector");
        autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
        Mockito.when(repository.findById(autor.getId())).thenReturn(Optional.empty());

        ResponseEntity<List<Livro>> resposta = controller.putAutor(autor);

        assertEquals(400, resposta.getStatusCodeValue());
        assertEquals("Autor não encontrado", resposta.getBody());
    }

    @Test
    @DisplayName("Post /autores/desfazer - Desfazer de Post - status 201")
    void getDesfazerPost() {
        ResponseEntity<List<Autor>> resposta = controller.postDesfazer();

        assertEquals(201, resposta.getStatusCodeValue());
        assertEquals("Retirando Autor",resposta.getBody());
    }

    @Test
    @DisplayName("Post /autores/desfazer - Desfazer de Put - status 201")
    void getDesfazerPut() {
        ResponseEntity<List<Autor>> resposta = controller.postDesfazer();

        assertEquals(201, resposta.getStatusCodeValue());
        assertEquals("Desfeita a alteração",resposta.getBody());
    }

}