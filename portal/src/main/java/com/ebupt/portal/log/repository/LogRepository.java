package com.ebupt.portal.log.repository;

import com.ebupt.portal.log.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LogRepository extends JpaRepository<LogEntity, Integer> {
}
