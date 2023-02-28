package com.ringme.cms.controller;

import com.ringme.cms.excelhelper.ExcelReadFile;
import com.ringme.cms.excelhelper.ExcelWriteFile;
import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import com.ringme.cms.repository.DepartmentRepository;
import com.ringme.cms.repository.StaffRepository;
import com.ringme.cms.service.MenuService;
import com.ringme.cms.service.QueueService;
import com.ringme.cms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class StaffController {
    @Autowired
    StaffService staffService;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    QueueService queueService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    MenuService menuService;


    @GetMapping("/queue/staff/{id}")
    public String getAllStaffNotInQueue(@PathVariable("id") Long id,Model model) {
        return getAllStaffNotInQueue(1,id,0L,null,model);
    }
    @GetMapping("/queue/staff/search")
    public String searchStaff(@RequestParam("id") Long id,
                              @RequestParam(value = "department", required = false) Long department, @RequestParam(value = "name", required = false) String name, Model model){
        return getAllStaffNotInQueue(1,id,department,name,model);
    };

    @GetMapping("/queue/staff/search/{page}")
    public String getAllStaffNotInQueue( @PathVariable("page") int pageNo,@RequestParam("id") Long id,
                                        @RequestParam(value = "department", required = false) Long department, @RequestParam(value = "name", required = false) String name, Model model) {
        System.err.println("department:"+department);
        if ((name != null && !name.equals("")) && (department != 0 )) {
            Page<Staff> staffPage = staffService.findStaffFilterByDepartmentAndName(department,name,pageNo,15);
            model.addAttribute("staffs",staffPage.toList());
            model.addAttribute("totalPages", staffPage.getTotalPages());
            model.addAttribute("totalItems", staffPage.getTotalElements());
        } else if ((name != null && !name.equals("")) && (department == 0 )) {
            Page<Staff> staffPage = staffService.findStaffFilterByName(name,pageNo,15);
            model.addAttribute("staffs",staffPage.toList());
            model.addAttribute("totalPages", staffPage.getTotalPages());
            model.addAttribute("totalItems", staffPage.getTotalElements());
        } else if ((name == null || name.equals("")) && (department != 0 )) {
            Page<Staff> staffPage = staffService.findStaffFilterByDepartment(department,pageNo,15);
            model.addAttribute("staffs",staffPage.toList());
            model.addAttribute("totalPages", staffPage.getTotalPages());
            model.addAttribute("totalItems", staffPage.getTotalElements());
        } else {
            Page<Staff> staffPage = staffService.findAllStaffNotInQueue(pageNo,15);
            model.addAttribute("staffs",staffPage.toList());
            model.addAttribute("totalPages", staffPage.getTotalPages());
            model.addAttribute("totalItems", staffPage.getTotalElements());
        }
        model.addAttribute("queue",queueService.findQueueById(id).get());
        model.addAttribute("currentPage", pageNo);

        model.addAttribute("nameq",name);
        model.addAttribute("department",department);
        model.addAttribute("departments",departmentRepository.findAll());
        model.addAttribute("listMenu", menuService.getListMenuNoParent());
        model.addAttribute("mapMenu", menuService.getMapMenuParent());
        return "queue-staff";
    }

    @GetMapping("/queue/remove-staff/{id}")
    public String removeStaffInQueue(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            staffService.removeStaffToQueueByIdStaffAndQueueId(staff.get().getId());
            redirectAttributes.addFlashAttribute("success", "Delete success");
            return "redirect:/queue/view/" + staff.get().getQueue().getId();
        } else {
            redirectAttributes.addFlashAttribute("error", "Staff not found");
            return "redirect:/queue/index";
        }

    }
    @GetMapping("/queue/add-staff/{id}/{queue}")
    public String addStaffToQueue(@PathVariable("id") Long id, @PathVariable("queue") Long idQueue, RedirectAttributes redirectAttributes){
        Optional<Staff> staff = staffRepository.findById(id);
        Optional<Queue> queue = queueService.findQueueById(idQueue);
        if (staff.isPresent()&&queue.isPresent()){
            staff.get().setQueue(queue.get());
            staffRepository.save(staff.get());
            redirectAttributes.addFlashAttribute("Success", "Success");
            return "redirect:/queue/view/"+queue.get().getId();
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Error");
            return "redirect:/queue/index";
        }
    }
    @GetMapping("/queue/staff-import/{id}")
    public String loadimportStaffByFileExcel(@PathVariable("id")Long id, Model model) throws IOException {
        Optional<Queue> queue = queueService.findQueueById(id);
        if (queue.isPresent()) {
            model.addAttribute("queue",queue.get());
            model.addAttribute("listMenu", menuService.getListMenuNoParent());
            model.addAttribute("mapMenu", menuService.getMapMenuParent());
            return "queue-addexcel";
        }
        return "redirect:/queue/index";
    }
    @PostMapping("/queue/staff-import")
    public String importStaffByFileExcel(@ModelAttribute("file") MultipartFile file, @RequestParam("id")Long pathId, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes) throws IOException {
        Optional<Queue> queue = queueService.findQueueById(pathId);
        if (queue.isPresent()) {
            try {
                Map<Staff, Boolean> staffList = staffService.readFileStaff(file, queue.get());
                System.err.println(staffList.keySet());
                 String url = ExcelWriteFile.createExcel(staffList);
                model.addAttribute("url",url);
                model.addAttribute("queue",queue.get());
                model.addAttribute("listMenu", menuService.getListMenuNoParent());
                model.addAttribute("mapMenu", menuService.getMapMenuParent());
                return "queue-addexcel-success";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error","Error");
                return "redirect:/queue/index";
            }
        }
        redirectAttributes.addFlashAttribute("error","Queue not found");
        return "redirect:/queue/index";
    }
    @GetMapping("/queue/export/file/{file}")
    public ResponseEntity<InputStreamResource> downloadExcel(@PathVariable("file")String url) throws IOException {
        String uploadDir = "F:\\CMS\\execlcms";
        File file = new File(uploadDir,url);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=excel_file.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }
//    @GetMapping("/queue/export/staff")
//    public void export(Map<Staff, Boolean> booleanMap, HttpServletResponse response) {
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename= " + "StaffError.xlsx");
//        ExcelWriteFile.createExcel(response, booleanMap);
//    }
}
