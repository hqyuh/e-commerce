package com.shopme.admin.user.service;

import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Transaction:
 * là 1 giao dịch (1 giao tác) bao gồm 1 loạt các hành động được phải được thực hiện thành công cùng nhau,
 * nếu 1 hành động thất bại thì tất cả các hành động trong loạt hành động đó sẽ trở về trạng thái ban đầu.
 *
 * @Transactional:
 * Spring cung cấp cơ chế hỗ trợ quản lý transaction tự động start, commit, hay rollback transaction tự động.
 *  + Nếu đặt @Transaction ở đầu class thì tất cả các method trong class đó đều nằm trong 1 transaction,
 *  + Nếu đặt @Transaction ở đầu method thì chỉ các method đó được nằm trong 1 transaction.
 *
 */

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 3;

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo,
                       RoleRepository roleRepo,
                       PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public List<User> listAll(){
        return (List<User>) userRepo.findAll();
    }

    public User getByEmail(String email){
        return userRepo.getUserByEmail(email);
    }

    public Page<User> listByPage(int pageNum,
                                 String sortField,
                                 String sortDir,
                                 String keyword){

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

        if(keyword != null)
            return userRepo.findBy(keyword, pageable);

        return userRepo.findAll(pageable);
    }


    public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    // save user
    public User save(User user){

        boolean isUpdatingUser = (user.getId() != null);

        if(isUpdatingUser){
            User existingUser = userRepo.findById(user.getId()).get();

            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }

        return userRepo.save(user);
    }

    // update account
    public User updateAccount(User userInForm){
        User userInDB = userRepo.findById(userInForm.getId()).get();

        if(!userInForm.getPassword().isEmpty()){
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if(userInForm.getPhotos() != null)
            userInDB.setPhotos(userInForm.getPhotos());

        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepo.save(userInDB);
    }

    // hash password
    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    // check duplicate email
    public boolean isEmailUnique(Integer id, String email){

        User userByEmail = userRepo.getUserByEmail(email);

        // if email not in database
        if(userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if(isCreatingNew){
            // if email exists
            if(userByEmail != null)
                return false;
        }else {
            // if id exists
            if(userByEmail.getId() != id)
                return false;
        }

        return true;
    }

    // get user by ID
    public User get(Integer id) throws UserNotFoundException {
        try{
            return userRepo.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    // delete user by ID
    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);

        if(countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepo.deleteById(id);

    }

    // enabled
    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepo.updateEnabledStatus(id, enabled);
    }


}
