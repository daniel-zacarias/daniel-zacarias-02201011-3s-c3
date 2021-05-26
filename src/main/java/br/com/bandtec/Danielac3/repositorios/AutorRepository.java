package br.com.bandtec.Danielac3.repositorios;

import br.com.bandtec.Danielac3.dominios.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Integer> {
    Optional<Autor> findByNome(String nome);
}
