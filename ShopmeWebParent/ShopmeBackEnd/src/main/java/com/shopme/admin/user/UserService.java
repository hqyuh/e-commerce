package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

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

    public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    // save user
    public void save(User user){

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

        userRepo.save(user);
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



}
