package com.ringme.cms.service;

import com.ringme.cms.model.Router;

import java.util.List;

public interface RouterService {
    void addRouter(Router router) throws Exception;
    List<Router> findAllRouterUnActive();
    List<Router> findAllRouterActive();
    void updateStatus(boolean check, Long id);

    List<Router> findAllRouterNotInRole(List<Long> roleIds) ;

}
