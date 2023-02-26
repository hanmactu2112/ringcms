package com.ringme.cms.service;

import com.ringme.cms.model.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAllRole();

    Page<Role> pageRole(int pageNo,int pageSize);

    Optional<Role> findRoleById(Long id);

    void saveRole(Role role) throws Exception;

    void deleteRole(Long id) throws Exception;

    List<Role> findAllRoleNotInListIdRole(List<Long> idRole);
}
