package com.repository;

import com.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sanjaya on 12/4/16.
 */
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

}
