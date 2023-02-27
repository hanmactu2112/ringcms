package com.ringme.cms.service;

import com.ringme.cms.model.Department;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Override
    public List<Department> findAllDepartment() {
        return null;
    }

    @Override
    public Optional<Department> findDepartmentById(Long id) {
        return Optional.empty();
    }
}
