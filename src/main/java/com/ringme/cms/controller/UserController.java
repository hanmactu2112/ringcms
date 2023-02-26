package com.ringme.cms.controller;

import com.ringme.cms.model.User;
import com.ringme.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/user/index")
    public String getAllUser(Model model){

        return getAllUserPage(1,"0","","",model);
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
