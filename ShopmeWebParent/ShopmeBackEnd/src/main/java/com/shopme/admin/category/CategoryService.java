package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CategoryService {

    public final CategoryRepository repo;

    @Autowired
    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> listAll(){
        List<Category> rootCategories = repo.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory: rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = rootCategory.getChildren();
            for (Category subCategory: children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
        Set<Category> children = parent.getChildren();

        int newSubLevel = subLevel + 1;
        for (Category subCategory: children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel + 1);
        }
    }

    public Category save(Category category){
        return repo.save(category);
    }


    // Form
    // load option for Form
    public List<Category> listCategoriesUsedInForm(){

        // list xu???t tr??n form (option)
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = repo.findAll();

        for (Category category: categoriesInDB) {
            if(category.getParent() == null){

                // ch??? l???y id v?? name cho parent-category (form)
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                // l???y children c???p 1 (--)
                Set<Category> children = category.getChildren();
                for (Category subCategory: children) {
                    String name = "--" + subCategory.getName();
                    // l???y id v?? name cho sub-category (form c???n id v?? "--" + name c???a sub ????? ch???n)
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                    // list children
                    // l???y children c???p 2 (----)
                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }

            }
        }

        return categoriesUsedInForm;
    }

    // list children for Form, c???p 2.
    public void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,
                                            Category parent,
                                            int subLevel){

        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory: children) {

            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subCategory.getName();
            // System.out.println(name);
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }

    }
    // End Form


    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    /*
     * check unique name/ alias
     *
    * */
    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        System.out.println(isCreatingNew);
        Category categoryByName = repo.findByName(name);
        Category categoryByAlias = repo.findByAlias(alias);
        if(isCreatingNew) {
            if(categoryByName != null)
                return "DuplicateName";
            else {
                if(categoryByAlias != null)
                    return "DuplicateAlias";
            }
        } else {
            if(categoryByName != null && categoryByName.getId() != id)
                return "DuplicateName";
            if(categoryByAlias != null && categoryByAlias.getId() != id)
                return "DuplicateAlias";
        }
        return "OK";
    }

}
