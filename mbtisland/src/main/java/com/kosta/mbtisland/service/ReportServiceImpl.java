package com.kosta.mbtisland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Report;
import com.kosta.mbtisland.repository.ReportRepository;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Override
	public void insertReport(Report report) throws Exception {
		reportRepository.save(report);
	}

}
