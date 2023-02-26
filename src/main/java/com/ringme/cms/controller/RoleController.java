package com.ringme.cms.controller;

import com.ringme.cms.service.RoleService;
import com.ringme.cms.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/role/index")
    public String loadAllRole(Model model){
        return pageRole(1,model);
    }
    @GetMapping("/role/index/{page}")
    public String pageRole(@PathVariable("page")Integer page, Model model){
        Page<Role> roles = roleService.pageRole(page,10);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roles.getTotalPages());
        model.addAttribute("totalItems", roles.getTotalElements());
        model.addAttribute("roles",roles);
        return "role";
    }
    @GetMapping("/role/create")
    public String createRole(Model model){
        model.addAttribute("role",new Role());
        return "create-role";
    }
    @GetMapping("/role/update/{id}")
    public String updateRole(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Role> role = roleService.findRoleById(id);
        if (role.isPresent()){
            role.get().setRoleName(role.get().getRoleName().split("ROLE_")[1]);
            model.addAttribute("role",role.get());
            return "create-role";
        }
        else {
            redirectAttributes.addFlashAttribute("error","Role not found!!!");
            return "redirect:/role/index";
        }

    }
    @PostMapping("/role/update")
    public String saveRole(@Valid @ModelAttribute("role")Role role, Errors error,  RedirectAttributes redirectAttributes){
        if (error.hasErrors()){
            return "create-role";
        }
        else {
            try {
                roleService.saveRole(role);
                redirectAttributes.addFlashAttribute("success","Success");
                return "redirect:/role/index";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error","Role already exists!!!");
                return "redirect:/role/index";
            }
        }
    }
    @GetMapping("/role/delete/{id}")
    public String deleteRole(@PathVariable("id")Long id,RedirectAttributes redirectAttributes){
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("success","Delete role success");
            return "redirect:/role/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success","Delete role error");
            return "redirect:/role/index";
        }
    }
}
