package com.ringme.cms.service;

import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffService {

    List<Staff> listStaffHaveQueue(Queue queue);
}
