package com.mjt.recruitment.repository;

import com.mjt.recruitment.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JobRepository extends JpaRepository<Job, Long> {

}
