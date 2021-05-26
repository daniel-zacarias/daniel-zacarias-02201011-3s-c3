package br.com.bandtec.Danielac3.repositorios;

import br.com.bandtec.Danielac3.dominios.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro,Integer> {
}
