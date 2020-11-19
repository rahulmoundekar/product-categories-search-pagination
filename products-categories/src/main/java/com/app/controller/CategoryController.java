package com.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Category;
import com.app.entity.Product;
import com.app.exception.RecordNotFoundException;
import com.app.repository.CategoryRepository;

@Controller
public class CategoryController {

	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping(value = { "/category", "/category/{id}" })
	public String get(Model model, @PathVariable("id") Optional<Integer> id) throws RecordNotFoundException {
		if (id.isPresent()) {
			Optional<Category> categroy = categoryRepository.findById(id.get());
			if (categroy.isPresent()) {
				model.addAttribute("category", categroy);
			} else {
				throw new RecordNotFoundException("No category record exist for given id : " + id.get());
			}
		} else {
			model.addAttribute("category", new Category());
		}
		return "category_form";
	}

	@PostMapping("/category/create")
	public String saveBank(Model model, @Validated @ModelAttribute("categroy") Category categroy,
			BindingResult bindingResult, RedirectAttributes flashMessages) throws Exception {
		if (bindingResult.hasErrors()) {
			model.addAttribute("category", categroy);
			return "category";
		}
		Category optinalCategroy = categoryRepository.save(categroy);
		if (optinalCategroy != null) {
			flashMessages.addFlashAttribute("success", "Product added Successfully!! ");
		} else {
			flashMessages.addFlashAttribute("error", "Details are not saved!! Please retry.");
		}
		return "redirect:/";
	}

}
