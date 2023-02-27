package com.ringme.cms.service;

import com.ringme.cms.model.Router;

import java.util.List;
import java.util.Optional;

public interface RouterService {
    void addRouter(Router router) throws Exception;
    List<Router> findAllRouterUnActive();
    List<Router> findAllRouterActive();
    void updateStatus(boolean check, Long id);

    List<Router> findAllRouterNotInRole(List<Long> roleIds) ;

    Optional<Router> findRouterById(Long id);

}
