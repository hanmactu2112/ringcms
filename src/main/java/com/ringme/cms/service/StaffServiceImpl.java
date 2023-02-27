package com.ringme.cms.service;

import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import com.ringme.cms.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService{
    @Autowired
    StaffRepository staffRepository;
    @Override
    public List<Staff> listStaffHaveQueue(Queue Queue) {
        return staffRepository.findAllByQueue(Queue);
    }
}
