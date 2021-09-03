package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public String listAll(Model model){

        List<Category> listCategories = service.listAll();
        model.addAttribute("listCategory", listCategories);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){

        List<Category> listCategories = service.listCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategory", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        

        return "categories/category_form";
    }

}