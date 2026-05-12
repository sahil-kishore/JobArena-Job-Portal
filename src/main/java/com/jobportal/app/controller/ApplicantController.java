package com.jobportal.app.controller;

import com.jobportal.app.model.Application;
import com.jobportal.app.model.Job;
import com.jobportal.app.model.User;
import com.jobportal.app.repository.ApplicationRepository;
import com.jobportal.app.repository.JobRepository;
import com.jobportal.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ApplicantController {

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ApplicationRepository appRepo;

    @GetMapping("/applicant/applicant_dashboard")
    public String applicantDashboard(Model model, Principal principal) {

        User applicant = userRepo.findByUsername(principal.getName()).orElse(null);

        model.addAttribute("user", applicant);

        List<Application> applications = appRepo.findByApplicant(applicant);

        model.addAttribute("applications", applications);

        return "applicant_dashboard";
    }

    @GetMapping("/applicant/profile")
    public String applicantProfile(Model model, Principal principal) {

        User applicant = userRepo.findByUsername(principal.getName()).orElse(null);

        model.addAttribute("user", applicant);

        return "profile";
    }

    @GetMapping("/jobs/apply_jobs")
    public String applyJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<Job> jobs;

        if (keyword != null && !keyword.trim().isEmpty()) {

            jobs = jobRepo.findByTitleContainingIgnoreCase(keyword);

        } else {

            jobs = jobRepo.findAll();
        }

        model.addAttribute("jobs", jobs);

        return "apply_jobs";
    }

    @PostMapping("/jobs/apply/{id}")
    public String applyForJob(@PathVariable Long id, Principal principal) {

        User applicant = userRepo.findByUsername(principal.getName()).orElse(null);

        Job job = jobRepo.findById(id).orElse(null);

        if (applicant == null || job == null) {

            return "redirect:/jobs/apply_jobs?error";
        }

        boolean alreadyApplied = appRepo.existsByJobAndApplicant(job, applicant);

        if (alreadyApplied) {

            return "redirect:/jobs/apply_jobs?alreadyApplied";
        }

        Application application = new Application();

        application.setApplicant(applicant);

        application.setJob(job);

        application.setStatus("Applied Successfully");

        appRepo.save(application);

        return "redirect:/applications/track_status?success";
    }

    @GetMapping("/applications/track_status")
    public String trackApplications(Model model, Principal principal) {

        User applicant = userRepo.findByUsername(principal.getName()).orElse(null);

        List<Application> applications = appRepo.findByApplicant(applicant);

        model.addAttribute("applications", applications);

        return "track_status";
    }
}
