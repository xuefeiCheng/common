package com.ebupt.portal.log.service.impl;

import com.ebupt.portal.log.entity.LogEntity;
import com.ebupt.portal.log.repository.LogRepository;
import com.ebupt.portal.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("logService")
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void save(LogEntity entity) {
        this.logRepository.save(entity);
    }

}
