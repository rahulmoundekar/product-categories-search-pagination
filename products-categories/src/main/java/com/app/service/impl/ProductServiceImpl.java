package com.app.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.app.beans.Paged;
import com.app.beans.Paging;
import com.app.entity.Product;
import com.app.repository.ProductRepository;
import com.app.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository proudctRepository;

	@Override
	public Paged<Product> getProduct(int pageNumber, int size) {
		PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
		Page<Product> productPage = proudctRepository.findAll(request);
		return new Paged<>(productPage, Paging.of(productPage.getTotalPages(), pageNumber, size));
	}

	@Override
	public Product saveOrUpdate(Product product) {
		return proudctRepository.save(product);
	}

	@Override
	public Paged<Product> filterByCategory(int pageNumber, int size, Integer categoryId) {
		PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
		Page<Product> productPage = proudctRepository.findByCategoryId(categoryId, request);
		return new Paged<>(productPage, Paging.of(productPage.getTotalPages(), pageNumber, size));
	}

	@Override
	public Paged<Product> search(int pageNumber, int size, String searchKey) {
		PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
		Page<Product> productPage = proudctRepository.search(searchKey, request);
		return new Paged<>(productPage, Paging.of(productPage.getTotalPages(), pageNumber, size));
	}

}
