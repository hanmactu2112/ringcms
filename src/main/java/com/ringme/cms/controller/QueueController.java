package com.ringme.cms.controller;

import com.ringme.cms.excelhelper.ExcelReadFile;
import com.ringme.cms.excelhelper.ExcelWriteFile;
import com.ringme.cms.model.*;
import com.ringme.cms.model.Queue;
import com.ringme.cms.repository.DepartmentRepository;
import com.ringme.cms.repository.MissionRepository;
import com.ringme.cms.repository.QueueRepository;
import com.ringme.cms.service.MenuService;
import com.ringme.cms.service.QueueService;
import com.ringme.cms.service.StaffService;
import com.ringme.cms.service.TimeQueueService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;

@Controller
public class QueueController {
    private static final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @Autowired
    QueueService queueService;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    StaffService staffService;
    @Autowired
    MenuService menuService;

    @Autowired
    TimeQueueService timeQueueService;

    @GetMapping("/queue/index")
    public String findAllQueue(Model model) {
        return findAllQueuePage(1, model);
    }

    @GetMapping("/queue/index/{page}")
    public String findAllQueuePage(@PathVariable int page, Model model) {
        Page<Queue> queuePageNo = queueService.findAllQueuePage(page, 15);
        System.out.println(queuePageNo.toList());
        model.addAttribute("queues", queuePageNo.toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", queuePageNo.getTotalPages());
        model.addAttribute("totalItems", queuePageNo.getTotalElements());
        model.addAttribute("listMenu", menuService.getListMenuNoParent());
        model.addAttribute("mapMenu", menuService.getMapMenuParent());
        return "queue";
    }

    @GetMapping("/queue/update/{id}")
    public String getQueueById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Queue> queue = queueService.findQueueById(id);
        if (queue.isPresent()) {
            List<Mission> missions = missionRepository.findAll();
            List<Department> departments = departmentRepository.findAll();
            List<Queue> queues = queueService.findAllQueue();
            List<TimeQueue> timeQueues = timeQueueService.findAllTimeQueue();
            model.addAttribute("missions", missions);
            model.addAttribute("departments", departments);
            model.addAttribute("queues", queues);
            model.addAttribute("timeQueues", timeQueues);
            model.addAttribute("queue", queue.get());
            model.addAttribute("listMenu", menuService.getListMenuNoParent());
            model.addAttribute("mapMenu", menuService.getMapMenuParent());
            return "add-queue";
        }
        redirectAttributes.addFlashAttribute("error", "Queue not found");
        return "redirect:/queue/index";
    }

    @GetMapping("/queue/create")
    public String createQueue(Model model) {
        Queue queue = new Queue();
        List<Mission> missions = missionRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        List<Queue> queues = queueService.findAllQueue();
        List<TimeQueue> timeQueues = timeQueueService.findAllTimeQueue();
        model.addAttribute("queue", queue);
        model.addAttribute("missions", missions);
        model.addAttribute("departments", departments);
        model.addAttribute("queues", queues);
        model.addAttribute("timeQueues", timeQueues);
        model.addAttribute("listMenu", menuService.getListMenuNoParent());
        model.addAttribute("mapMenu", menuService.getMapMenuParent());

        return "add-queue";
    }

    @PostMapping("/queue/save")
    public String saveQueue(@Valid @ModelAttribute("queue") Queue queue, Errors error,
                            @RequestParam("department") Long departmentId,
                            @RequestParam("nextQueue") Long nextQueueId,
                            @RequestParam("mission") Long missionId,
                            @RequestParam("startTime") Long startTimeId, @RequestParam("endTime") Long endTimeId
            ,@RequestParam("fileimg") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        if (error.hasErrors()) {
            return "add-queue";
        }

        if (queue.getId() != null) {
            Optional<Queue> queuePresent = queueService.findQueueById(queue.getId());
            if (!queuePresent.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Not found Queue");
                return "redirect:/queue/index";
            }
        }

        Optional<Department> department = departmentRepository.findById(departmentId);
        Optional<Mission> mission = missionRepository.findById(missionId);
        Optional<Queue> queue1 = queueService.findQueueById(nextQueueId);
        Optional<TimeQueue> startTime = timeQueueService.findTimeQueueById(startTimeId);
        Optional<TimeQueue> endTime = timeQueueService.findTimeQueueById(endTimeId);

        if (!department.isPresent() || !mission.isPresent() || !queue1.isPresent() || !startTime.isPresent() || !endTime.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Error");
            return "redirect:/queue/index";
        }
        if ((startTime.get().getTime()).compareTo(endTime.get().getTime()) >= 0) {
            model.addAttribute("error", "Start Time must be BeFore End Time");
            if (queue.getId() == null) {
                model.addAttribute("error", "Start Time must be BeFore End Time");
                return createQueue(model);
            }
            return getQueueById(queue.getId(), model, redirectAttributes);
        }

        queue.setNextQueue(queue1.get());
        queue.setDepartment(department.get());
        queue.setMission(mission.get());
        queue.setStartTime(startTime.get());
        queue.setEndTime(endTime.get());
        //save img start
        String imageUUID;
        String nameFile="";
        if(!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Random random=new Random();
            String ext="";
            for (int j = imageUUID.length()-1; j >=0 ; j--) {
                if (imageUUID.charAt(j)=='.'){
                    ext = imageUUID.substring(j,imageUUID.length());
                }
            }
            Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
            int t = 1;
            while (t==1){
                 nameFile = String.format("%s%s",System.currentTimeMillis(),random.nextInt(100000)+ext);
                fileNameAndPath = Paths.get(uploadDir,nameFile);
                boolean exists = Files.exists(fileNameAndPath);
                if (!exists) t=0;
            }
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            queue.setImage(nameFile);
        }
        //end save img

        queueService.saveQueue(queue);
        redirectAttributes.addFlashAttribute("success", "Success");
        return "redirect:/queue/index";
    }

    @GetMapping("/queue/delete/{id}")
    public String deleteQueue(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Queue> queue = queueService.findQueueById(id);
        List<Queue> queues = queueRepository.findAllByNextQueueId(id);
        if (queue.isPresent()) {
            List<Staff> staffList = staffService.listStaffHaveQueue(queue.get());
            if (staffList.isEmpty() && queues.isEmpty()) {
                queueService.deleteQueueById(id);
                redirectAttributes.addFlashAttribute("success", "Delete success");
                return "redirect:/queue/index";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Delete Queue Error");
        return "redirect:/queue/index";
    }

//    @PostMapping("/import")
//    public InputStreamResource importFileExcel(MultipartFile multipartFile) throws IOException {
//        Map<String, Boolean> map = new LinkedHashMap<>();
//        XSSFWorkbook w = (XSSFWorkbook) ExcelWriteFile.write(map);
//        ByteArrayOutputStream bout = new ByteArrayOutputStream();
//        w.write(bout);
//        InputStream inputStream = new ByteArrayInputStream(bout.toByteArray());
//        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
//        return inputStreamResource;
//    }
}
