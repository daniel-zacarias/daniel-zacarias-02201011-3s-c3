package br.com.bandtec.Danielac3.repositorios;

import br.com.bandtec.Danielac3.dominios.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoRepository extends JpaRepository<Arquivo, String> {
}
