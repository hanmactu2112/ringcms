package com.ringme.cms.controller;

import com.ringme.cms.model.Icon;
import com.ringme.cms.model.Menu;
import com.ringme.cms.model.Router;
import com.ringme.cms.repository.IconRepository;
import com.ringme.cms.service.MenuService;
import com.ringme.cms.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class MenuController {

    @Autowired
    MenuService menuService;

    @Autowired
    RouterService routerService;

    @Autowired
    IconRepository iconRepository;


    @GetMapping("/menu/index")
    public String getAllMenu(Model model) {
        return getAllMenuPage(1, model);
    }

    @GetMapping("/menu/index/{pageNo}")
    public String getAllMenuPage(@PathVariable int page, Model model) {
        Page<Menu> menuPageNo = menuService.findMenuPage(page, 15);
        model.addAttribute("listMenu",menuService.getListMenuNoParent());
        model.addAttribute("mapMenu",menuService.getMapMenuParent());
        model.addAttribute("menus", menuPageNo.toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", menuPageNo.getTotalPages());
        model.addAttribute("totalItems", menuPageNo.getTotalElements());
        return "menu-index";
    }

    @GetMapping("/menu/create")
    public String createMenu(Model model) {
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        List<Router> routers = routerService.findAllRouterActive();
        List<Icon> icons = iconRepository.findAll();
        model.addAttribute("listMenu",menuService.getListMenuNoParent());
        model.addAttribute("mapMenu",menuService.getMapMenuParent());
        model.addAttribute("routers", routers);
        model.addAttribute("icons", icons);
        return "add-menu";
    }

    @GetMapping("/menu/update/{id}")
    public String updateMenu(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Menu> menu = menuService.findMenuById(id);
        if (menu.isPresent()) {
            List<Router> routers = routerService.findAllRouterActive();
            List<Icon> icons = iconRepository.findAll();
            model.addAttribute("listMenu",menuService.getListMenuNoParent());
            model.addAttribute("mapMenu",menuService.getMapMenuParent());
            model.addAttribute("menu", menu.get());
            model.addAttribute("routers", routers);
            model.addAttribute("icons", icons);
            return "add-menu";
        }
        redirectAttributes.addFlashAttribute("error", "Not found Menu");
        return "redirect:/menu/index";
    }


    @PostMapping("/menu/save")
    public String saveMenu(@Valid @ModelAttribute("menu") Menu menu, BindingResult bindingResult, Errors error, @RequestParam("router") Long routerId, @RequestParam("icon") Long iconId, RedirectAttributes redirectAttributes,Model model) {
        if (error.hasErrors()){
            List<Router> routers = routerService.findAllRouterActive();
            List<Icon> icons = iconRepository.findAll();
            model.addAttribute("routers", routers);
            model.addAttribute("icons", icons);
            model.addAttribute("listMenu",menuService.getListMenuNoParent());
            model.addAttribute("mapMenu",menuService.getMapMenuParent());
            return "add-menu";
        }
        else {
            if (menu.getId() != null) {
                Optional<Menu> menuPresent = menuService.findMenuById(menu.getId());
                if (!menuPresent.isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Not found menu");
                    return "redirect:/menu/index";
                }
            }
            Optional<Router> router = routerService.findRouterById(routerId);
            Optional<Icon> icon = iconRepository.findById(iconId);
            if (!router.isPresent() || !icon.isPresent()){
                redirectAttributes.addFlashAttribute("error", "Error");
                return "redirect:/menu/index";
            }
            menu.setRouter(router.get());
            menu.setIcon(icon.get());
            menuService.saveMenu(menu);
            redirectAttributes.addFlashAttribute("success", "Success");
            return "redirect:/menu/index";
        }
    }
    @GetMapping("/menu/delete/{id}")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Optional<Menu> menu = menuService.findMenuById(id);
        if (menu.isPresent()){
            menuService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Success");
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Menu not fount");
        }
        return "redirect:/menu/index";
    }

}

