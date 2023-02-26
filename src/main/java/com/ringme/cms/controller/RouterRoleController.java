package com.ringme.cms.controller;

import com.ringme.cms.service.RoleRouterService;
import com.ringme.cms.service.RoleService;
import com.ringme.cms.service.RouterService;
import com.ringme.cms.model.Role;
import com.ringme.cms.model.Router;
import com.ringme.cms.model.RouterRole;
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
public class RouterRoleController {
    @Autowired
    RoleRouterService roleRouterService;
    @Autowired
    RouterService routerService;
    @Autowired
    RoleService roleService;

    @GetMapping("/role/view/{id}")
    public String addRouterToRole(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Role> role = roleService.findRoleById(id);
        if (role.isPresent()){
            List<RouterRole> routerRoles = roleRouterService.findAllRouterRoleByRoleId(id);
            List<Long> listId = routerRoles.stream().map(e->e.getRouter().getId()).collect(Collectors.toList());
            List<Router> routers;
            System.err.println("listId: "+ listId.isEmpty());
            if (!listId.isEmpty()) {
                 routers = routerService.findAllRouterNotInRole(listId);
            } else {
                 routers = routerService.findAllRouterActive();
            }
            model.addAttribute("role",role.get());
            model.addAttribute("routers",routers);
            model.addAttribute("routerRoles",routerRoles);
            return "role-router";
        }
        else {
            redirectAttributes.addFlashAttribute("error","Role not found!!!");
            return "redirect:/role/index";
        }
    }
    @PostMapping("/router-role/delete")
    public String deleteRouterRole(@RequestParam("id")Long id,@RequestParam("router2")List<Long> idRouterRole,RedirectAttributes redirectAttributes){
        if (idRouterRole !=null){
            try {
                roleRouterService.deleteRoleRouter(idRouterRole);
                redirectAttributes.addFlashAttribute("success","Success");
                return "redirect:/role/view/"+id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error","Error");
                return "redirect:/role/view/"+id;
            }
        }
        redirectAttributes.addFlashAttribute("error","Error");
        return "redirect:/role/view/"+id;
    }
    @PostMapping("/router-role/create")
    public String createRouterRole(@RequestParam("id")Long id, @RequestParam("router1")List<Long> idRouterRole,RedirectAttributes redirectAttributes){
        System.err.println("role-create: "+idRouterRole);
        if (!idRouterRole.isEmpty()&& idRouterRole != null){
            try {
                roleRouterService.createRoleRouter(id,idRouterRole);
                redirectAttributes.addFlashAttribute("success","Success");
                return "redirect:/role/view/"+id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error","Error");
                return "redirect:/role/view/"+id;
            }
        }
        redirectAttributes.addFlashAttribute("error","Error");
        return "redirect:/role/view/"+id;
    }
}
