package com.ringme.cms.controller;

import com.ringme.cms.criteria.QueueSearchCriteria;
import com.ringme.cms.model.*;
import com.ringme.cms.model.Queue;
import com.ringme.cms.repository.DepartmentRepository;
import com.ringme.cms.repository.MissionRepository;
import com.ringme.cms.repository.QueueRepository;
import com.ringme.cms.repository.StaffRepository;
import com.ringme.cms.service.MenuService;
import com.ringme.cms.service.QueueService;
import com.ringme.cms.service.StaffService;
import com.ringme.cms.service.TimeQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class QueueController {
    private static final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @Autowired
    QueueService queueService;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    QueueRepository queueRRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffService staffService;
    @Autowired
    MenuService menuService;

    @Autowired
    TimeQueueService timeQueueService;

    @GetMapping("/queue/index")
    public String findAllQueue(Model model) {
        return findAllQueuePage(1,null,null,null,null,null,null,null,null, model);
    }

    @GetMapping("/queue/index/{page}")
    public String findAllQueuePage(@PathVariable int page,@RequestParam(value = "id",required = false)Long id,@RequestParam(value = "queueName",required = false)String queueName,
                                   @RequestParam(value = "hostName",required = false)String hostName,@RequestParam(value = "displayName",required = false)String displayName,@RequestParam(value = "department",required = false)String department,
                                   @RequestParam(value = "queueType",required = false)String queueType,@RequestParam(value = "mission",required = false)String mission,@RequestParam(value = "province",required = false)String province,
                                   Model model) {
        QueueSearchCriteria queueSearchCriteria = new QueueSearchCriteria(id,queueName,hostName,displayName,department,queueType,mission,province);
        Page<Queue> queues = queueService.findQueuesByCriteria(queueSearchCriteria,page,15);
        System.out.println(queues.toList());
        model.addAttribute("queues", queues.toList());
        model.addAttribute("departments",departmentRepository.findAll());
        model.addAttribute("missions", missionRepository.findAll());
        model.addAttribute("typeQueues",queueRRepository.findAllTypeQueue());
        model.addAttribute("provinces",queueRRepository.findAllProvince());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", queues.getTotalPages());
        model.addAttribute("totalItems", queues.getTotalElements());
        model.addAttribute("listMenu", menuService.getListMenuNoParent());
        model.addAttribute("mapMenu", menuService.getMapMenuParent());
        model.addAttribute("id",id);
        model.addAttribute("queueName",queueName);
        model.addAttribute("hostName",hostName);
        model.addAttribute("displayName",displayName);
        model.addAttribute("department",department);
        model.addAttribute("queueType",queueType);
        model.addAttribute("mission",mission);
        model.addAttribute("province",province);
        return "queue";
    }
    @GetMapping("/queue/search")
    public String searchQueue(@RequestParam(value = "id",required = false)Long id, @RequestParam(value = "queueName",required = false)String queueName,
                               @RequestParam(value = "hostName",required = false)String hostName,@RequestParam(value = "displayName",required = false)String displayName,@RequestParam(value = "department",required = false)String department,
                               @RequestParam(value = "queueType",required = false)String queueType,@RequestParam(value = "mission",required = false)String mission,@RequestParam(value = "province",required = false)String province,Model model){
        return findAllQueuePage(1,id,queueName,hostName,displayName,department,queueType,mission,province,model);
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
    @GetMapping({"/queue/view/{id}","/queue/view/{id}/{page}"})
    public String viewMenu(@PathVariable("id")Long id,@PathVariable(value = "page",required = false)Optional<Integer> page,Model model, RedirectAttributes redirectAttributes){
        System.err.println("page"+ page);
        int page2 = 1;
        if (page.isPresent()) page2 = Math.max(page.get(),page2);
        Optional<Queue> queue = queueService.findQueueById(id);
        if (queue.isPresent()){
            Page<Staff> staffs = staffService.findStaffPageByIdQueue(id,page2,20);
            model.addAttribute("staffs",staffs);
            model.addAttribute("queue",queue.get());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", staffs.getTotalPages());
            model.addAttribute("totalItems", staffs.getTotalElements());
            model.addAttribute("listMenu", menuService.getListMenuNoParent());
            model.addAttribute("mapMenu", menuService.getMapMenuParent());
            return "queue-view";
        }
        else {
            redirectAttributes.addFlashAttribute("error","Queue not found");
            return "redirect:/queue/index";
        }

    }

}
