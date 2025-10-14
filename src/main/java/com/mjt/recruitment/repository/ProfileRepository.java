package com.mjt.recruitment.repository;

import com.mjt.recruitment.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<Profile, Long> {

}