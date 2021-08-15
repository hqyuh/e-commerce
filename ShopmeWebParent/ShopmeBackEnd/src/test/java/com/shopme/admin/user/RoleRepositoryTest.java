package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
// sử dụng database như thông thường
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin", "manage everything");

        Role savaRole = repo.save(roleAdmin);
        assertThat(savaRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRole(){
        Role roleSalesPerson = new Role("Salesperson",
                "manage product price customer, shipping, orders and sales report");

        Role roleEditor = new Role("Editor",
                "manage categories, brands, products, articles and menus");

        Role roleShipper = new Role("Shipper",
                "view products, view orders and update order status");

        Role roleAssistant = new Role("Assistant", "manage questions and reviews");

        repo.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));
    }
}