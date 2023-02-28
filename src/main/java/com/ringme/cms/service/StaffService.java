package com.ringme.cms.service;

import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StaffService {

    List<Staff> listStaffHaveQueue(Queue queue);

    Page<Staff> findStaffPageByIdQueue(Long id,int pageNo, int pageSize);

    void addStaffToQueueByIdStaffAndQueueId(Long queueId,Long idStaff);

    void removeStaffToQueueByIdStaffAndQueueId(Long idStaff);

    Page<Staff> findStaffFilterByName(String name, int pageNo,int pageSize);

    Page<Staff> findStaffFilterByDepartment(Long id,int pageNo,int pageSize);

    Page<Staff> findStaffFilterByDepartmentAndName(Long id,String name,int pageNo,int pageSize);

    Map<Staff,Boolean> readFileStaff(MultipartFile file, Queue queue) throws IOException;
     Page<Staff> findAllStaffNotInQueue(int pageNo,int pageSize);


}
