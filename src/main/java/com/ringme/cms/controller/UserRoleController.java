package com.ringme.cms.controller;

import com.ringme.cms.model.Role;
import com.ringme.cms.model.User;
import com.ringme.cms.model.UserRole;
import com.ringme.cms.repository.RoleRepository;
import com.ringme.cms.service.RoleService;
import com.ringme.cms.service.UserRoleService;
import com.ringme.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserRoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserController userController;

    @GetMapping("user/view/{id}")
    public String getUserRoleById(@PathVariable Long id, Model model, RedirectAttributes attributes){
        Optional<User> user = userService.findByIdUser(id);
        if(user.isPresent()){
            List<UserRole> roles = userRoleService.findAllUserRoleByIdUser(id);
            if (roles.isEmpty()){
                List<Role> roleList = roleService.findAllRole();
                model.addAttribute("roles",roleList);
            }
            else {
                List<Long> idRole = roles.stream().map(e->e.getRole().getId()).collect(Collectors.toList());
                List<Role> roleList = roleService.findAllRoleNotInListIdRole(idRole);
                model.addAttribute("roles",roleList);
            }
            model.addAttribute("user",user.get());
            model.addAttribute("user_roles",roles);
            return "user-role";
        }
        attributes.addFlashAttribute("error","User not found");
        return "redirect:/user/index";
    }

    @PostMapping("/user-role/create")
    public String updateUserRole(@RequestParam("id") Long id,@RequestParam("role1") List<Long> roles,Model model,RedirectAttributes redirectAttributes){
        Optional<User> user = userService.findByIdUser(id);
        if(user.isPresent()){
            List<Role> rolePresent = roleRepository.findAllById(roles);
            List<UserRole> userRoles = rolePresent.stream().map((e)->{
                return new UserRole(user.get(),e);
            }).collect(Collectors.toList());
            try {
                userRoleService.saveAllUserRole(userRoles);
            } catch (Exception e) {
                model.addAttribute("error","Update error");
                return getUserRoleById(id,model,redirectAttributes);
            }
            model.addAttribute("success","Update success");
            return getUserRoleById(id,model,redirectAttributes);
        }
        model.addAttribute("error","User not found");
        return getUserRoleById(id,model,redirectAttributes);

    }
    @PostMapping("/user-role/delete")
    public String deleteUserRole(@RequestParam("id") Long id,@RequestParam("role2") List<Long> userroles,Model model,RedirectAttributes redirectAttributes){
        Optional<User> user = userService.findByIdUser(id);
        if(user.isPresent()){
            try {
                userRoleService.deleteUserRoleById(userroles);
                model.addAttribute("success","delete success");
                return getUserRoleById(id,model,redirectAttributes);
            } catch (Exception e) {
                model.addAttribute("error","delete error");
                return getUserRoleById(id,model,redirectAttributes);
            }
        }
        else {
            model.addAttribute("error","User not found");
            return getUserRoleById(id,model,redirectAttributes);
        }
    }

}
