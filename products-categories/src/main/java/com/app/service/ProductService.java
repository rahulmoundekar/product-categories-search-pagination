package com.app.service;

import java.util.List;
import com.app.beans.Paged;
import com.app.entity.Product;

public interface ProductService {
	public Paged<Product> getProduct(int pageNumber, int size);

	public Product saveOrUpdate(Product product);

	public Paged<Product> search(int pageNumber, int size, String searchKey);

	public Paged<Product> filterByCategory(int pageNumber, int size, Integer categoryId);
}
