package br.com.bandtec.Danielac3.controles;

import br.com.bandtec.Danielac3.dominios.Autor;
import br.com.bandtec.Danielac3.dominios.Livro;
import br.com.bandtec.Danielac3.repositorios.AutorRepository;
import br.com.bandtec.Danielac3.repositorios.LivroRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LivroControllerTest {

    @Autowired
    LivroController controller;

    @MockBean
    LivroRepository repository;

    @MockBean
    AutorRepository autorRepository;


    @Test
    @DisplayName("Get /livros - quando houverem registros - status 200 e número certo de registros")
    void getLivrosExistente() {

        List<Livro> livroTeste = Arrays.asList(new Livro(), new Livro(), new Livro());
        Mockito.when(repository.findAll()).thenReturn(livroTeste);

        ResponseEntity<List<Livro>> resposta = controller.getLivros();


        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(3, resposta.getBody().size());
    }

    @Test
    @DisplayName("Get /livros - quando houverem registros - status 204 e sem contéudo")
    void getLivrosVazios() {

        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Livro>> resposta = controller.getLivros();


        assertEquals(204, resposta.getStatusCodeValue());
        assertEquals(resposta.getBody(), "Não foi encontrado nenhum livro");
    }

    @Test
    @DisplayName("Post /livros - quando o body seguir todas as normas - status 201")
    void postLivroCerto() {
        Livro livro = new Livro();
        livro.setPreco(100.0);
        livro.setTitulo("A volta dos que não foram");
        livro.setDataDeLancamento(LocalDate.now());
        Autor autor = new Autor();

        autor.setId(1);
        autor.setNome("José de Alencar");
        autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
        livro.setAutor(autor);
        Mockito.when(autorRepository.existsById(autor.getId())).thenReturn(true);
        ResponseEntity<List<Livro>> resposta = controller.postLivro(livro);

        assertEquals(201, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Post /livros - quando o body não contém o autor no banco - status 400")
    void postLivroErrado() {
        Livro livro = new Livro();
        livro.setPreco(100.0);
        livro.setTitulo("A volta dos que não foram");
        livro.setDataDeLancamento(LocalDate.now());
        Autor autor = new Autor();
        autor.setId(1);
        autor.setNome("José de Alencar");
        autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
        livro.setAutor(autor);
        Mockito.when(autorRepository.existsById(autor.getId())).thenReturn(false);
        ResponseEntity<List<Livro>> resposta = controller.postLivro(livro);

        assertEquals(400, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Delete /livros - quando existe um ID válido - status 200")
    void deleteLivroCorreto() {
        Livro livro = new Livro();
        livro.setPreco(100.0);
        livro.setTitulo("A volta dos que não foram");
        livro.setDataDeLancamento(LocalDate.now());
        Autor autor = new Autor();
        autor.setId(1);
        autor.setNome("José de Alencar");
        autor.setDataDeNascimento(LocalDate.parse("1829-05-01"));
        livro.setAutor(autor);
        Mockito.when(repository.findById(livro.getId())).thenReturn(Optional.of(livro));

        ResponseEntity<List<Livro>> resposta = controller.deleteLivro(livro.getId());

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals("O Livro foi deletado",resposta.getBody());
    }

    @Test
    void testGetLivros() {
    }

    @Test
    void testPostLivro() {
    }

    @Test
    void testDeleteLivro() {
    }

    @Test
    void getDesfazer() {
    }
}