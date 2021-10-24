package com.shopme.admin.category;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @PostMapping("/category/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category saveCategory = service.save(category);

            String uploadDir = "../category-images/" + saveCategory.getId();

            FileUploadUtil.clearDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else
            service.save(category);

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
        return "redirect:/categories";

    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Category category = service.get(id);
            List<Category> listCategories = service.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategory", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

}
