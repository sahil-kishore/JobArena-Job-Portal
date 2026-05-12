package com.jobportal.app.controller;

import com.jobportal.app.model.Job;
import com.jobportal.app.model.User;
import com.jobportal.app.repository.JobRepository;
import com.jobportal.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class JobController {

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/jobs/post_job")
    public String postJobForm(Model model) {

        model.addAttribute("job", new Job());

        return "post_job";
    }

    @PostMapping("/jobs/post_job")
    public String postJob(@ModelAttribute Job job, Principal principal) {

        User employer = userRepo
                .findByUsername(principal.getName())
                .orElse(null);

        if (employer != null) {

            job.setEmployer(employer);

            jobRepo.save(job);
        }

        return "redirect:/employer/employer_dashboard";
    }
}
