package com.ringme.cms.Service;

import com.ringme.cms.model.Role;
import com.ringme.cms.model.Router;
import com.ringme.cms.model.RouterRole;
import com.ringme.cms.repository.RoleRepository;
import com.ringme.cms.repository.RouterRepository;
import com.ringme.cms.repository.RouterRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class RoleRouterServiceImpl implements RoleRouterService{
    @Autowired
    RouterRoleRepository routerRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RouterRepository routerRepository;
    @Override
    public List<RouterRole> findAllRouterRole() {
        return routerRoleRepository.findAll();
    }

    @Override
    public List<RouterRole> findAllRouterRoleByRoleId(Long roleId) {
        return routerRoleRepository.findAllRouterRoleByRoleId(roleId);
    }

    @Override
    public List<RouterRole> findAllRouterRoleByListRoleId(List<Long> roleIds) {
        return routerRoleRepository.findAllRouterRoleByListRoleId(roleIds);
    }

    @Override
    public void saveRoleRouter(RouterRole routerRole) {
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        routerRole.setCreatedDate(date);
        routerRoleRepository.save(routerRole);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteRoleRouter(List<Long> id) throws Exception {
        for (Long i:id){
            try{
                routerRoleRepository.deleteById(i);
            }
            catch (Exception e){
                throw new Exception();
            }
        }
    }

    @Override
    public void createRoleRouter(Long idRole, List<Long> idRouter)throws Exception {
        Optional<Role> role = roleRepository.findById(idRole);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        if (role.isPresent()){
          List<Router> routers = routerRepository.findAllRouterByListId(idRouter);
          for(Router router: routers){
              RouterRole routerRole = new RouterRole();
              routerRole.setRouter(router);
              routerRole.setRole(role.get());
              routerRole.setCreatedDate(date);
              routerRoleRepository.save(routerRole);
          }
        }
        else throw new Exception();
    }

}
