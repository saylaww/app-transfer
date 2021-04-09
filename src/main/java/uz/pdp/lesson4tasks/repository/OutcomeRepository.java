package uz.pdp.lesson4tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson4tasks.entity.Outcome;

public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {
}
