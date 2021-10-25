package com.shopme.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static Category copyIdAndName(Category category) {
        Category copyCategory = new Category();

        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        return copyCategory;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category copyCategory = new Category();

        copyCategory.setId(id);
        copyCategory.setName(name);
        return copyCategory;
    }

    public static Category copyFull(Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setImage(category.getImage());
        copyCategory.setAlias(copyCategory.getAlias());
        copyCategory.setEnabled(copyCategory.isEnabled());
        return copyCategory;
    }

    public static Category copyFull(Category category, String name) {
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(String name, Category parent) {
        // this(name);
        this.name = name;
        this.parent = parent;
    }

    @Transient
    public String getImagePath() {
        if(this.id == null)
            return "/images/image-thumbnail.png";

        return "/category-images/" + this.id + "/" + this.image;
    }

}
