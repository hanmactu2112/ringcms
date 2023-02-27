package com.ringme.cms.controller;

import com.ringme.cms.model.Icon;
import com.ringme.cms.model.Log;
import com.ringme.cms.model.Router;
import com.ringme.cms.service.LogService;
import com.ringme.cms.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class LogController {
    @Autowired
    LogService logService;
    @Autowired
    MenuService menuService;

    @GetMapping("/log/index")
    public String getLog(Model model){
       return getLog(1,model);
    }
    @GetMapping("/log/index/{page}")
    public String getLog(@PathVariable int page, Model model){
        Page<Log> logPage = logService.findAllLogPage(page,20);
        model.addAttribute("logs",logPage.toList());
        model.addAttribute("listMenu",menuService.getListMenuNoParent());
        model.addAttribute("mapMenu",menuService.getMapMenuParent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logPage.getTotalPages());
        model.addAttribute("totalItems", logPage.getTotalElements());
        return "log";
    }


}
