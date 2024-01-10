package me.dio.sacola.repository;

import me.dio.sacola.models.Item;
import me.dio.sacola.models.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
