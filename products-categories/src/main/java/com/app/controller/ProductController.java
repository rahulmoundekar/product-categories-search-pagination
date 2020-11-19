package com.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Category;
import com.app.entity.Product;
import com.app.exception.RecordNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.repository.ProductRepository;
import com.app.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping(value = "/")
	public String posts(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", required = false, defaultValue = "4") int size, Model model) {
		model.addAttribute("products", productService.getProduct(pageNumber, size));
		model.addAttribute("categories", categoryRepository.findAll());
		return "index";
	}

	@PostMapping(value = "/product/filter")
	public String filterCategory(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", required = false, defaultValue = "5") int size,
			@RequestParam(value = "category") Integer categoryId, Model model) {
		model.addAttribute("products", productService.filterByCategory(pageNumber, size, categoryId));
		return "index";
	}

	@PostMapping(value = "/product/search")
	public String search(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", required = false, defaultValue = "5") int size,
			@RequestParam(value = "searchKey") String searchKey, Model model) {
		model.addAttribute("products", productService.search(pageNumber, size, searchKey));
		return "index";
	}

	@PostMapping("/product/create")
	public String saveBank(Model model, @Validated @ModelAttribute("product") Product product,
			BindingResult bindingResult, RedirectAttributes flashMessages) throws Exception {
		if (bindingResult.hasErrors()) {
			model.addAttribute("product", product);
			return "product";
		}
		Product optinalProduct = productService.saveOrUpdate(product);
		if (optinalProduct != null) {
			flashMessages.addFlashAttribute("success", "Product added Successfully!! ");
		} else {
			flashMessages.addFlashAttribute("error", "Details are not saved!! Please retry.");
		}
		return "redirect:/";
	}

	@GetMapping(value = { "/product", "/product/{id}" })
	public String get(Model model, @PathVariable("id") Optional<Integer> id) throws RecordNotFoundException {
		if (id.isPresent()) {
			Optional<Product> product = productRepository.findById(id.get());
			if (product.isPresent()) {
				model.addAttribute("product", product);
			} else {
				throw new RecordNotFoundException("No product record exist for given id : " + id.get());
			}
		} else {
			model.addAttribute("product", new Product());
		}
		model.addAttribute("categories", categoryRepository.findAll());
		return "product_form";
	}

	@ExceptionHandler(Exception.class)
	public String catchCustomException(Exception ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("error", "Something Went Wrong try again..!!");
		return "error";
	}

}
