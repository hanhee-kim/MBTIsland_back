package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.entity.Report;
import com.kosta.mbtisland.service.ReportService;

@RestController
public class ReportController {
	@Autowired
	ReportService reportService;
	
	@PostMapping("/report")
	public ResponseEntity<Object> report(@RequestBody Report sendReport) {
		try {
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			
			sendReport.setReportDate(writeDate);
//			Report report = Report.builder().reportDate(writeDate).build();
			System.out.println("신고 타입 : " + sendReport.getReportType());
			System.out.println("게시판 타입 : " + sendReport.getTableType());
			reportService.insertReport(sendReport);
			
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
