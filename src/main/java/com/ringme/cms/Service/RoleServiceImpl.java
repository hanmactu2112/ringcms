package com.ringme.cms.Service;

import com.ringme.cms.model.Role;
import com.ringme.cms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;
    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }
}
