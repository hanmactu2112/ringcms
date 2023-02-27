package com.ringme.cms.service;

import com.ringme.cms.model.Log;
import org.springframework.data.domain.Page;


public interface LogService {

    Page<Log> findAllLogPage(int pageNo,int pageSize);
}
