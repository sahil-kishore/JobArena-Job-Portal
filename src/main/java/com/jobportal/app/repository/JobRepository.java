
package com.jobportal.app.repository;

import com.jobportal.app.model.Job;
import com.jobportal.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByEmployer(User employer);

    java.util.List<com.jobportal.app.model.Job> findByTitleContainingIgnoreCase(String keyword);
}
