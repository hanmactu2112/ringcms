package com.ringme.cms.Service;

import com.ringme.cms.model.Router;
import com.ringme.cms.model.RouterRole;
import com.ringme.cms.repository.RouterRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface RouterService {
    void addRouter(Router router) throws Exception;
    List<Router> findAllRouterUnActive();
    List<Router> findAllRouterActive();
    void updateStatus(boolean check, Long id);

}
