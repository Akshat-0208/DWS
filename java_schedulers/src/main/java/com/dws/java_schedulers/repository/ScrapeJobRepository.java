package com.dws.java_schedulers.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dws.java_schedulers.entity.ScrapeJob;

public interface ScrapeJobRepository
        extends JpaRepository<ScrapeJob, UUID> {
}