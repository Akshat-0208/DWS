package com.dws.java_schedulers.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dws.java_schedulers.dto.CreateJobRequest;
import com.dws.java_schedulers.entity.ScrapeJob;
import com.dws.java_schedulers.repository.ScrapeJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapeJobService {

    private final ScrapeJobRepository repository;

    private final RabbitMQProducer producer;

    public ScrapeJob createJob(CreateJobRequest request) {

        ScrapeJob job = ScrapeJob.builder()
                .targetUrl(request.getTargetUrl())
                .status("PENDING")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ScrapeJob savedJob = repository.save(job);

        try {
            producer.sendMessage(savedJob.getTargetUrl());
        } catch (Exception e) {
            System.out.println(
                "RabbitMQ Error: " + e.getMessage()
            );
        }

        return savedJob;
    }
}