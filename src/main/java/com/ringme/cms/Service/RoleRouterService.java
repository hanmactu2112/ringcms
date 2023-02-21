package com.ringme.cms.Service;

import com.ringme.cms.model.RouterRole;

import java.util.List;

public interface RoleRouterService {
    List<RouterRole> findAllRouterRole();

    List<RouterRole> findAllRouterRoleByRoleId(Long roleId);
}
