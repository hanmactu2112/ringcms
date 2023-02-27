package com.ringme.cms.service;

import com.ringme.cms.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<Department> findAllDepartment();

    Optional<Department> findDepartmentById(Long id);
}
