package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

}
