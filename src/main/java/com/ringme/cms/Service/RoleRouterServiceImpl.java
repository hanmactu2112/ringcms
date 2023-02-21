package com.ringme.cms.Service;

import com.ringme.cms.model.RouterRole;
import com.ringme.cms.repository.RouterRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleRouterServiceImpl implements RoleRouterService{
    @Autowired
    RouterRoleRepository routerRoleRepository;
    @Override
    public List<RouterRole> findAllRouterRole() {
        return routerRoleRepository.findAll();
    }

    @Override
    public List<RouterRole> findAllRouterRoleByRoleId(Long roleId) {
        return routerRoleRepository.findAllRouterRoleByRoleId(roleId);
    }
}
