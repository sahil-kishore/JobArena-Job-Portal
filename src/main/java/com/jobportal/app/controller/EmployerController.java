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
public class EmployerController {

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private UserRepository userRepo;

    /*
     * EMPLOYER DASHBOARD
     */
    @GetMapping("/employer/employer_dashboard")
    public String employerDashboard(Model model, Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        List<Job> jobs =
                jobRepo.findByEmployer(employer);

        model.addAttribute("jobList", jobs);
        model.addAttribute("employer", employer);

        return "employer_dashboard";
    }

    /*
     * EMPLOYER PROFILE
     */
    @GetMapping("/employer/profile")
    public String employerProfile(Model model,
                                  Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        if (employer == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", employer);

        return "profile";
    }

    /*
     * SHOW POST JOB PAGE
     */
    @GetMapping("/employer/jobs/post")
    public String showPostJobForm(Model model) {

        model.addAttribute("job", new Job());

        return "post_job";
    }

    /*
     * SAVE NEW JOB
     */
    @PostMapping("/employer/jobs/post_job")
    public String postJob(@ModelAttribute Job job,
                          Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        if (employer != null) {

            job.setEmployer(employer);

            jobRepo.save(job);
        }

        return "redirect:/employer/jobs/view_jobs_posted";
    }

    /*
     * VIEW EMPLOYER JOBS
     */
    @GetMapping("/employer/jobs/view_jobs_posted")
    public String viewJobsPosted(Model model,
                                 Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        List<Job> jobs =
                jobRepo.findByEmployer(employer);

        model.addAttribute("jobs", jobs);

        return "view_jobs_posted";
    }

    /*
     * EDIT JOB PAGE
     */
    @GetMapping("/employer/jobs/edit/{id}")
    public String editJob(@PathVariable Long id,
                          Model model,
                          Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        Job job =
                jobRepo.findById(id).orElse(null);

        if (job == null ||
                employer == null ||
                !job.getEmployer().getId()
                        .equals(employer.getId())) {

            return "redirect:/employer/jobs/view_jobs_posted";
        }

        model.addAttribute("job", job);

        return "edit_job";
    }

    /*
     * UPDATE JOB
     */
    @PostMapping("/employer/jobs/update")
    public String updateJob(@ModelAttribute Job job,
                            Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        Job existingJob =
                jobRepo.findById(job.getId()).orElse(null);

        if (existingJob == null ||
                employer == null ||
                !existingJob.getEmployer().getId()
                        .equals(employer.getId())) {

            return "redirect:/employer/jobs/view_jobs_posted";
        }

        job.setEmployer(employer);

        jobRepo.save(job);

        return "redirect:/employer/jobs/view_jobs_posted";
    }

    /*
     * DELETE JOB
     */
    @GetMapping("/employer/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id,
                            Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        Job job =
                jobRepo.findById(id).orElse(null);

        if (job != null &&
                employer != null &&
                job.getEmployer().getId()
                        .equals(employer.getId())) {

            jobRepo.delete(job);
        }

        return "redirect:/employer/jobs/view_jobs_posted";
    }

    /*
     * VIEW APPLICANTS
     */
    @GetMapping("/employer/applicants/{jobId}")
    public String viewApplicants(@PathVariable Long jobId,
                                 Model model,
                                 Principal principal) {

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        Job job =
                jobRepo.findById(jobId).orElse(null);

        if (job == null ||
                employer == null ||
                !job.getEmployer().getId()
                        .equals(employer.getId())) {

            return "redirect:/employer/jobs/view_jobs_posted";
        }

        List<Application> applications =
                appRepo.findByJobId(jobId);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);

        return "view_applicants";
    }

    /*
     * UPDATE APPLICATION STATUS
     */
    @PostMapping("/applications/update/{id}")
    public String updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Principal principal) {

        Application application =
                appRepo.findById(id).orElse(null);

        if (application == null) {
            return "redirect:/employer/employer_dashboard";
        }

        User employer =
                userRepo.findByUsername(principal.getName())
                        .orElse(null);

        if (employer == null ||
                !application.getJob()
                        .getEmployer()
                        .getId()
                        .equals(employer.getId())) {

            return "redirect:/employer/employer_dashboard";
        }

        application.setStatus(status);

        appRepo.save(application);

        return "redirect:/employer/applicants/"
                + application.getJob().getId();
    }
}