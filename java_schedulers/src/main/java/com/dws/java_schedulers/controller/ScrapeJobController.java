package com.dws.java_schedulers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.java_schedulers.dto.CreateJobRequest;
import com.dws.java_schedulers.entity.ScrapeJob;
import com.dws.java_schedulers.service.ScrapeJobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class ScrapeJobController {

    private final ScrapeJobService service;

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

    @PostMapping
    public ScrapeJob createJob(
            @RequestBody CreateJobRequest request
    ) {

        System.out.println(request.getTargetUrl());

        return service.createJob(request);
    }
}