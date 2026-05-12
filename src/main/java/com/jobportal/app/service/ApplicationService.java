/*package com.jobportal.app.service;

import com.jobportal.app.model.Application;
import com.jobportal.app.model.Job;
import com.jobportal.app.model.User;
import com.jobportal.app.repository.ApplicationRepository;
import com.jobportal.app.repository.JobRepository;
import com.jobportal.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired private ApplicationRepository appRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private JobRepository jobRepo;

    public void applyToJob(Long jobId, String applicantUsername) {
        User user = userRepo.findByUsername(applicantUsername).orElseThrow();
        Job job = jobRepo.findById(jobId).orElseThrow();
        Application app = new Application();
        app.setApplicant(user);
        app.setJob(job);
        app.setStatus("APPLIED");
        appRepo.save(app);
    }

    public List<Application> getMyApplications(String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        return appRepo.findByApplicant(user);
    }
}
*/