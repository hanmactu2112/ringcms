package com.ringme.cms.service;

import com.ringme.cms.logrepository.LogRepository;
import com.ringme.cms.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService{
    @Autowired
    LogRepository logRepository;
    @Override
    public Page<Log> findAllLogPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return logRepository.findAllLogPage(pageable);
    }
}
