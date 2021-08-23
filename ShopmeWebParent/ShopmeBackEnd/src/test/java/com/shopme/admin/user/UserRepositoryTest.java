package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    // lấy data đã có trong database
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){

        Role roleAdmin = entityManager.find(Role.class, 1);

        User userHuyHQ = new User(
                "hoquang.huyy@gmail.com",
                "123456",
                "Hồ",
                "Quang Huy");

        userHuyHQ.addRole(roleAdmin);

        User savedUser = repo.save(userHuyHQ);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateNewUserWithTwoRole(){
        User userRavi = new User("ravi@gmail.com", "ravi2021", "Ravi", "Kumar");

        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);

        User savedUser = repo.save(userRavi);

        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testListAllUser(){
        Iterable<User> listUsers = repo.findAll();
        // listUsers.forEach(user -> System.out.println(user));
        listUsers.forEach(System.out::println);
    }

    @Test
    public void testGetUserById(){
        User userHuy = repo.findById(1).get();
        System.out.println(userHuy);

        assertThat(userHuy).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userHuy = repo.findById(1).get();
        userHuy.setEnabled(true);
        repo.save(userHuy);
    }

    @Test
    public void testUpdateUserRole(){
        User userRavi = repo.findById(2).get();

        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        userRavi.getRoles().remove(roleEditor);
        userRavi.addRole(roleSalesperson);

        repo.save(userRavi);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){

        String email = "hoquang.huyy@gmail.com";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();

    }

    @Test
    public void testCouldById(){

        Integer id = 1;

        Long countId = repo.countById(id);
        System.out.println(countId);
        assertThat(countId).isNotNull().isGreaterThan(0);

    }

    @Test
    public void testDisableUser(){
        Integer id = 21;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 21;
        repo.updateEnabledStatus(id, true);
    }


    @Test
    public void testListFirstPage(){

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber,pageSize );
        Page<User> page =  repo.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isEqualTo(pageSize);

    }

}