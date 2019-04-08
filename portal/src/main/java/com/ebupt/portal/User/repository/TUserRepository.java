package com.ebupt.portal.User.repository;

import com.ebupt.portal.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TUserRepository extends JpaRepository<User, Integer> {

    User findByName(String name);

}
