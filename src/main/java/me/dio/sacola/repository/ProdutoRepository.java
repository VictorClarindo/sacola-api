package me.dio.sacola.repository;

import me.dio.sacola.models.Produto;
import me.dio.sacola.models.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
