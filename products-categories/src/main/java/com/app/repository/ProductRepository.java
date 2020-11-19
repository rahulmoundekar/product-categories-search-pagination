package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	public Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%" + " OR p.name LIKE %?1%" + " OR p.description LIKE %?1%")
	public Page<Product> search(String searchKey, Pageable pageable);

}
