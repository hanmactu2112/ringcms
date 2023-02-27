package com.ringme.cms.controller;

import com.ringme.cms.model.*;
import com.ringme.cms.repository.DepartmentRepository;
import com.ringme.cms.repository.MissionRepository;
import com.ringme.cms.repository.QueueRepository;
import com.ringme.cms.service.QueueService;
import com.ringme.cms.service.StaffService;
import com.ringme.cms.service.TimeQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Controller
public class QueueController {

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

        return "add-queue";
    }

    @PostMapping("/queue/save")
    public String saveQueue(@Valid @ModelAttribute("queue") Queue queue, Errors error,
                            @RequestParam("department") Long departmentId,
                            @RequestParam("nextQueue") Long nextQueueId,
                            @RequestParam("mission") Long missionId,
                            @RequestParam("startTime") Long startTimeId, @RequestParam("endTime") Long endTimeId
            , Model model, RedirectAttributes redirectAttributes) {
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
        if (startTime.get().getTime().after(endTime.get().getTime())) {
            model.addAttribute("error", "Start Time must be BeFore End Time");
            return getQueueById(queue.getId(), model, redirectAttributes);
        }

        queue.setNextQueue(queue1.get());
        queue.setDepartment(department.get());
        queue.setMission(mission.get());
        queue.setStartTime(startTime.get());
        queue.setEndTime(endTime.get());
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
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Delete Queue Error");
        }
        return "redirect:/queue/index";
    }
}
