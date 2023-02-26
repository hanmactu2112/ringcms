package com.ringme.cms.controller;

import com.ringme.cms.service.RouterService;
import com.ringme.cms.model.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RouterController {
    @Autowired
    RouterService routerService;

    @GetMapping("/admin/router")
    public String indexRouter(Model model){
        List<Router> routerActive = routerService.findAllRouterActive();
        List<Router> routerUnActive = routerService.findAllRouterUnActive();
        model.addAttribute("routerActive",routerActive);
        model.addAttribute("routerUnActive",routerUnActive);
        return "Router";
    }

    @PostMapping("/add-router")
    public String addRouter(@RequestParam("router") String router, RedirectAttributes redirectAttributes){
        if (router==null||router.equals("")){
            redirectAttributes.addFlashAttribute("error","Router is not null");
            return "redirect:/admin/router";
        }
        else {
            Router r = new Router();
            r.setRouter_link(router);
            r.setActive(false);
            try {
                routerService.addRouter(r);
                redirectAttributes.addFlashAttribute("success","Add router success!!!");
                return "redirect:/admin/router";
            }
            catch (Exception e){
                redirectAttributes.addFlashAttribute("error","Router already exists!!!");
                return "redirect:/admin/router";
            }
        }
    }
    @PostMapping("/active-router")
    public String activeRouter(@RequestParam("router") List<Long> routers){
        for (Long i: routers){
            routerService.updateStatus(true,i);
        }
        return "redirect:/admin/router";
    }
    @PostMapping("/unactive-router")
    public String unActiveRouter(@RequestParam("router") List<Long> routers){
        for (Long i: routers){
            routerService.updateStatus(false,i);
        }
        return "redirect:/admin/router";
    }
}
