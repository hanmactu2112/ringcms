package com.ringme.cms.service;

import com.ringme.cms.excelhelper.ExcelReadFile;
import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import com.ringme.cms.repository.QueueRepository;
import com.ringme.cms.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService{
    @Autowired
    StaffRepository staffRepository;

    @Autowired
    QueueRepository queueRepository;
    @Override
    public List<Staff> listStaffHaveQueue(Queue Queue) {
        return staffRepository.findAllByQueue(Queue);
    }

    @Override
    public Page<Staff> findStaffPageByIdQueue(Long id, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return staffRepository.findAllByQueueId(id,pageable);
    }

    @Override
    public void addStaffToQueueByIdStaffAndQueueId(Long queueId, Long idStaff) {
        staffRepository.addStaffToQueueByIdStaffAndQueueId(queueId,idStaff);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void removeStaffToQueueByIdStaffAndQueueId(Long idStaff) {
        staffRepository.removeStaffToQueueByIdStaffAndQueueId(idStaff);
    }

    @Override
    public Page<Staff> findStaffFilterByName(String name, int pageNo,int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);

        return staffRepository.findStaffFilterByName(name,pageable);
    }

    @Override
    public Page<Staff> findStaffFilterByDepartment(Long id, int pageNo,int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return staffRepository.findStaffFilterByDepartment(id,pageable);
    }

    @Override
    public Page<Staff> findStaffFilterByDepartmentAndName(Long id, String name, int pageNo,int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return staffRepository.findStaffFilterByDepartmentAndName(id,name,pageable);
    }
    @Override
    public Page<Staff> findAllStaffNotInQueue(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return staffRepository.findAllStaffNotInQueue(pageable);
    }

    @Override
    public Map<Staff,Boolean> readFileStaff(MultipartFile file,Queue queue) throws IOException {
        Map<Staff,Boolean> mapStaff = new LinkedHashMap<>();
        List<Staff> staffList = ExcelReadFile.readFileExcel(file);

        List<String> usernameStaffs = staffList.stream().map(Staff::getUserName).collect(Collectors.toList());
        List<Staff> staffListDb = staffRepository.findAllStaffByListUserName(usernameStaffs);

        Iterator<Staff> iterator = staffListDb.listIterator();
        while (iterator.hasNext()){
            if (iterator.next().getQueue()!=null){
                iterator.remove();
            }
        }

        List<Staff> staffSaveDb = staffListDb.stream().peek(e-> e.setQueue(queue)).collect(Collectors.toList());
        staffRepository.saveAll(staffSaveDb);
        for (Staff staff: staffList){
            boolean itemExistsBasedOnProp = staffSaveDb.stream().map(Staff::getUserName).anyMatch(staff.getUserName()::equals);
            mapStaff.put(staff,itemExistsBasedOnProp);
        }

        return mapStaff;
    }
}
