package com.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    private final CategoryService service;

    @Autowired
    public CategoryRestController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/categories/check_unique")
    public String checkUnique(Integer id, String name, String alias) {
        return service.checkUnique(id, name, alias);
    }

}
