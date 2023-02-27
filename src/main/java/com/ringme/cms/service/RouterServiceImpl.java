package com.ringme.cms.service;

import com.ringme.cms.model.Router;
import com.ringme.cms.repository.RouterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RouterServiceImpl implements RouterService{
    @Autowired
    RouterRepository routerRepository;
    @Override
    public void addRouter(Router router) throws Exception{
        try{
            routerRepository.save(router);
        }catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public List<Router> findAllRouterUnActive() {
        return routerRepository.findAllRouterUnActive();
    }

    @Override
    public List<Router> findAllRouterActive() {
        return routerRepository.findAllRouterActive();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateStatus(boolean check, Long id) {
        routerRepository.updateStatus(check, id);
    }

    @Override
    public List<Router> findAllRouterNotInRole(List<Long> roleIds) {
        return routerRepository.findAllRouterNotInRole(roleIds);
    }

    @Override
    public Optional<Router> findRouterById(Long id) {
        return routerRepository.findById(id);
    }
}
