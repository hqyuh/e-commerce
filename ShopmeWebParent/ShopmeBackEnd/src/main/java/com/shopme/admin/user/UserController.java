package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model){

        // List<User> listUsers = service.listAll();
        // model.addAttribute("listUsers", listUsers);
        // return "users";

        return listByPage(1, model, "id", "asc", null);

    }


    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             Model model,
                             String sortField,
                             String sortDir,
                             String keyword){



        Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = page.getContent();

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("listUsers", listUsers);
        //
        model.addAttribute("currentPage", pageNum);
        // total number of pages
        model.addAttribute("totalPages", page.getTotalPages());
        // total number of elements
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        // sort
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        // search
        model.addAttribute("keyword", keyword);

        return "users";

    }


    @GetMapping("/users/new")
    public String newUser(Model model){

        User user = new User();
        List<Role> listRoles = service.listRoles();

        // set enabled
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {


        // System.out.println(multipartFile.getOriginalFilename()); // photo name

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);

            User savedUser = service.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();
            // System.out.println(multipartFile);

            FileUploadUtil.clearDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            if(user.getPhotos().isEmpty())
                user.setPhotos(null);
            service.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");


        // return "redirect:/users";

        return getRedirectURLtoAffectedUser(user);
    }

    private static String getRedirectURLtoAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        // System.out.println(firstPartOfEmail);
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }


    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Integer id,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        try {

            User user = service.get(id);
            List<Role> listRoles = service.listRoles();


            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            return "user_form";

        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                           RedirectAttributes redirectAttributes,
                           Model model){

        try {

            service.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The user ID " + id + " has been deleted successfully");
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";

    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes){

        service.updateUserEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }

    // export to CSV
    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException{
        List<User> listUsers = service.listAll();
        UserCsvExporter exportCSV = new UserCsvExporter();
        exportCSV.export(listUsers, response);
    }

    // export to Excel
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{
        List<User> listUsers = service.listAll();
        UserExcelExporter exportExcel = new UserExcelExporter();
        exportExcel.export(listUsers, response);
    }

}
