package uz.pdp.lesson4tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.lesson4tasks.entity.Card;

import java.util.UUID;


@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsById(Integer id);

    Card getCardById(Integer id);

    boolean existsByIdAndUserId(Integer id, UUID user_id);

    @Query(value = "select money from card where id = ?1", nativeQuery = true)
    Double getMoney(Integer cardId);

    @Query(value = "select * from card where user_id = ?1", nativeQuery = true)
    Card getCardByUserId(UUID user_id);

    @Query(value = "update card set money = ?1 where id = ?2 returning money", nativeQuery = true)
    void editMoney(Double money, Integer cardId);
}
