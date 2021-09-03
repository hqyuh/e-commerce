package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    public final CategoryRepository repo;

    @Autowired
    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> listAll(){
        return (List<Category>) repo.findAll();
    }

    public Category save(Category category){
        return repo.save(category);
    }

    // load option
    public List<Category> listCategoriesUsedInForm(){

        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = repo.findAll();

        for (Category category: categoriesInDB) {
            if(category.getParent() == null){
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = category.getChildren();
                for (Category subCategory: children) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                    // list children
                    listChildren(categoriesUsedInForm, subCategory, 1);
                }

            }
        }

        return categoriesUsedInForm;
    }

    // list children
    public void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel){

        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory: children) {

            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

            listChildren(categoriesUsedInForm, subCategory, subLevel);
        }

    }


}
