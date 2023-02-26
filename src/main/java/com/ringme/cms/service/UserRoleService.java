package com.ringme.cms.service;

import com.ringme.cms.model.UserRole;

import java.util.List;

public interface UserRoleService {
    void deleteById(Long id) throws Exception;

    void addUserRole(UserRole userRole);

    List<UserRole> findAllUserRoleByIdUser(Long id);
}
