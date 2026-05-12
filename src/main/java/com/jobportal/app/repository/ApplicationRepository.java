package com.jobportal.app.repository;

import com.jobportal.app.model.Application;
import com.jobportal.app.model.Job;
import com.jobportal.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByApplicant(User user);

    List<Application> findByJobId(Long jobId);

  //  List<Application> findByStatus(ApplicationStatus status);

  //  List<Application> findByJobIdAndStatus(Long jobId, ApplicationStatus status);

    boolean existsByJobAndApplicant(Job job, User applicant);
}
