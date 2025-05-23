package com.markdev.inventoryManagmentsSystem.repository;

import com.markdev.inventoryManagmentsSystem.entity.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE YEAR(t.createAt) = :year AND MONTH(t.createAt) = :month ")
    List<Transaction> findAllByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN t.product p " +
           "WHERE (:searchText IS NULL OR " +
            "LOWER (t.description) LIKE LOWER (CONCAT('%', :searchText, '%')) OR " +
            "LOWER (t.status) LIKE LOWER (CONCAT('%',:searchText,'%')) OR " +
            "LOWER (p.name) LIKE LOWER (CONCAT('%',:searchText,'%')) OR "+
            "LOWER (p.sku) LIKE LOWER (CONCAT('%',:searchText,'%')))")
    Page searchTransaction(@Param("searchText") String searchText, Pageable pageable);

}
