package org.fasttrackit.onlineshop.persisteance;

import org.fasttrackit.onlineshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository  extends JpaRepository<Product,Long> {

    Page<Product> findByNameContaining(String partialName, Pageable pageable);

    Page<Product> findByNameContainingAndQuantityGreaterThanEqual(
            String partialName, int minQuantity, Pageable pageable);
    //JPQL syntax(java persistence query language
   // @Query("SELECT  product FROM Product  product WHERE name LIKE '%:partialName'")
    //daca avem nume de tabele le incadram in ` `
    @Query(value = "SELECT * FROM product WHERE `name` LIKE '%?0%'",nativeQuery = true)
    Page<Product> findByPartialName(String partialName,Pageable pageable);

}
