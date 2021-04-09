package uz.pdp.lesson4tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson4tasks.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Integer> {



}
