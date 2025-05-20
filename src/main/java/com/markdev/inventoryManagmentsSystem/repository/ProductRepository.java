package com.markdev.inventoryManagmentsSystem.repository;

import com.markdev.inventoryManagmentsSystem.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {




    @Query("SELECT p FROM Product p WHERE p.name LIKE( LOWER (CONCAT('%',:searchText,'%'))) OR " +
            "p.sku LIKE (LOWER(CONCAT('%',:searchText,'%')))")
    Page searchProductByWord(@Param("searchText") String searchText, Pageable pageable);
}
