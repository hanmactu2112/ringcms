package com.ringme.cms.controller;

import com.ringme.cms.model.User;
import com.ringme.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/user/index")
    public String getAllUser(Model model){
        return getAllUserPage(1,"0","","",model);
    }
    @GetMapping("/user/create")
    public String createUser(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "create-user";
    }
    @PostMapping("/user/save")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Errors error, Model model,RedirectAttributes redirectAttributes){
        if (error.hasErrors()){
            return "create-user";
        }
        else {
            if (user.getId()!=null){
                Optional<User> userOptional = userService.findByIdUser(user.getId());
                if (!userOptional.isPresent()){
                    model.addAttribute("error","User not present");
                    return getAllUser(model);
                }
                else {
                    if (user.getPassword().trim().equals("")){
                        user.setPassword(userOptional.get().getPassword());
                    }
                    else {
                        if (user.getPassword().matches("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[^a-zA-Z]).{8,40}$")&&user.getPassword().matches("\\S+")){
                            user.setPassword(passwordEncoder.encode(user.getPassword()));
                        }
                        else {
                            model.addAttribute("errorpass","Password is not in the correct format");
                            return updateUser(user.getId(), model,redirectAttributes);
                        }
                    }
                }
            }
            else {
                if (user.getPassword().matches("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[^a-zA-Z]).{8,40}$")&&user.getPassword().matches("\\S+")){
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                else {
                    model.addAttribute("errorpass","Password is not in the correct format");
                    return createUser(model);
                }
            }
            userService.saveUser(user);
            model.addAttribute("success","Success");
            return getAllUser(model);
        }
    }
    @GetMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id")Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()){
            user.get().setPassword("");
            model.addAttribute("user",user.get());
            return "create-user";
        }
        else {
            redirectAttributes.addFlashAttribute("error","User not present");
            return "redirect:/user/index/1";
        }
    }
    @GetMapping("/user/index/{page}")
    public String getAllUserPage(@PathVariable("page")int page, @RequestParam(value = "id")String id
            , @RequestParam("username")String username, @RequestParam("type")String type, Model model){
        Long idUser=0L;
        Page<User> users;
        if (id.trim().equals("")||id.trim().matches("\\d+")) {
            if (id.trim().matches("\\d+"))idUser= Long.parseLong(id);
            users = userService.pageUser(page,20,idUser,username,type);
        }
        else {
            users = userService.pageUser(page,20,0L,"","");
            model.addAttribute("error","Id is number");
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalItems", users.getTotalElements());
        model.addAttribute("users", users.toList());
        return "user";
    }
    @GetMapping("/user/search")
    public String searchUser(@RequestParam(value = "id",required = false)String id
            , @RequestParam(value = "username",required = false)String username, @RequestParam(value = "type",required = false)String type, Model model){

        return getAllUserPage(1,id,username,type,model);
    }
}
