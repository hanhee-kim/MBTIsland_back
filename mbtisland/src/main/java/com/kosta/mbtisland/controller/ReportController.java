package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Ban;
import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleComment;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.entity.Report;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.BanService;
import com.kosta.mbtisland.service.FileVoService;
import com.kosta.mbtisland.service.MbattleService;
import com.kosta.mbtisland.service.MbtmiService;
import com.kosta.mbtisland.service.MbtwhyService;
import com.kosta.mbtisland.service.ReportService;
import com.kosta.mbtisland.service.UserService;

@RestController
public class ReportController {
	@Autowired
	ReportService reportService;
	
	@Autowired
	private FileVoService fileVoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MbtmiService mbtmiService;
	
	@Autowired
	private MbtwhyService mbtwhyService;
	
	@Autowired
	private MbattleService mbattleService;
	
	@Autowired
	private BanService banService;
	
	@Autowired
	private AlarmService alarmService;
	
	// 신고
	@PostMapping("/report")
	public ResponseEntity<Object> report(@RequestBody Report sendReport) {
		try {
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			
			sendReport.setReportDate(writeDate);
//			Report report = Report.builder().reportDate(writeDate).build();
			reportService.insertReport(sendReport);
			
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 신고 목록 조회
	@GetMapping("/adminreport/{page}/{filter}/{boardType}/{reportType}")
	public ResponseEntity<Object> adminReportList(@PathVariable Integer page, @PathVariable String filter,
			@PathVariable String boardType, @PathVariable String reportType) {
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1 : page).build();
			List<Report> reportList = reportService.selectReportListByPageAndFilterAndBoardTypeAndReportType(pageInfo, filter, boardType, reportType);
			
			System.out.println("신고 목록 : " + reportList);
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("reportList", reportList);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 신고 조회
	@GetMapping("/reportdetail/{no}")
	public ResponseEntity<Object> adminReportList(@PathVariable Integer no) {
		try {
			Report report = reportService.selectReportByNo(no);
			
			Map<String, Object> res = new HashMap<>();
			res.put("report", report);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 이미지 출력
	@GetMapping("/reportimg/{fileIdx}")
	public void imageView(@PathVariable Integer fileIdx, HttpServletResponse response) {
		try {
			fileVoService.readImage(fileIdx, response.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 경고 처리
	@PostMapping("/reportwarning")
	public ResponseEntity<Object> reportWarning(@RequestParam(required = false) String username,
			@RequestParam(required = false) String reportType, @RequestParam(required = false) String tableType,
			@RequestParam(required = false) Integer postNo, @RequestParam(required = false) Integer commentNo) {
		try {
			// report 테이블에 해당하는 no의 isWarned, isCompleted 컬럼을 Y로 업데이트
			List<Report> reportList = null;
			if(reportType.equals("게시글")) {		
				reportList = reportService.selectReportListByTableTypeAndPostNo(tableType, postNo);
			} else if(reportType.equals("댓글")) {
				reportList = reportService.selectReportListByTableTypeAndPostNoCommentNo(tableType, postNo, commentNo);
			}
			
			for(int i = 0;i < reportList.size();i++) {
				reportList.get(i).setIsCompleted("Y");
				reportList.get(i).setIsWarned("Y");
				reportService.insertReport(reportList.get(i));	
			}
			
			// user 테이블의 userWarnCnt 1 증가
			UserEntity user = userService.getUserByUsername(username);
			user.setUserWarnCnt(user.getUserWarnCnt() + 1);
			
			// user 테이블의 userWarnCnt가 3, 6, 9...처럼 3의 배수일 때마다 isBanned를 Y로 업데이트
			// user 테이블에 제재 기간에 해당되는 DATE 타입 컬럼을 추가하여 기간 지정
			LocalDate currentDate = LocalDate.now();
			LocalDate plusDate = null;
			Timestamp banEndDate = null;
			
			// Ban 테이블에서 username으로 조회
			Ban ban = banService.selectBanByUsername(username);
			
			// 경고 횟수가 3의 배수가 되어 제재 처리되는 경우
			if(user.getUserWarnCnt() != 0 && user.getUserWarnCnt() % 3 == 0) {
				if(user.getIsBanned().equals("N") && ban == null) { // 현재 정지당한 상태가 아닐 경우
					user.setIsBanned("Y");
					
					if(user.getUserWarnCnt() >= 3) {
						plusDate = currentDate.plusDays(30);
						banEndDate = Timestamp.valueOf(plusDate.atStartOfDay());
					} else if(user.getUserWarnCnt() >= 6) {
						plusDate = currentDate.plusDays(60);
						banEndDate = Timestamp.valueOf(plusDate.atStartOfDay());
					} else if(user.getUserWarnCnt() >= 9) {
						plusDate = currentDate.plusDays(90);
						banEndDate = Timestamp.valueOf(plusDate.atStartOfDay());
					}
					// user 테이블에 set
					user.setBanDate(banEndDate);
					// ban 테이블 build
					ban = Ban.builder()
							.username(username)
							.banStartDate(Timestamp.valueOf(currentDate.atStartOfDay()))
							.banEndDate(banEndDate).build();
				} else if(user.getIsBanned().equals("Y") && ban != null) { // 이미 정지당한 상태일 경우
					// Timestamp => Instant => LocalDate 형변환
					Instant instant = ban.getBanEndDate().toInstant();
					// 기존 정지 종료일에 새로운 정지 기간을 더해줌
			        LocalDate oldBanEndDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
					if(user.getUserWarnCnt() >= 6) {
						plusDate = oldBanEndDate.plusDays(60);
						banEndDate = Timestamp.valueOf(plusDate.atStartOfDay());
					} else if(user.getUserWarnCnt() >= 9) {
						plusDate = oldBanEndDate.plusDays(90);
						banEndDate = Timestamp.valueOf(plusDate.atStartOfDay());
					}					
					// user 테이블에 set
					user.setBanDate(banEndDate);
					// ban 테이블에 set
					ban.setBanEndDate(banEndDate);
				}
			}
			
			// user 업데이트
			userService.modifyUser(user);
			// ban 삽입(업데이트)
			banService.insertBan(ban);
			
			// tableType에 해당하는 테이블의 postNo 게시글의 isBlocked를 Y로 업데이트
			if(tableType=="mbtmi") {
				if(reportType=="게시글") {
					Mbtmi mbtmi = mbtmiService.mbtmiDetail(postNo);
					mbtmi.setIsBlocked("Y");
					mbtmiService.addMbtmiForUpdate(mbtmi);
				} else if(reportType=="댓글") {
					MbtmiComment mbtmiComment = mbtmiService.mbtmiComment(commentNo);
					mbtmiComment.setIsBlocked("Y");
					mbtmiService.addMbtmiComment(mbtmiComment);
				}
			} else if(tableType=="mbtwhy") {
				if(reportType=="게시글") {
					Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(postNo);
					mbtwhy.setIsBlocked("Y");
					mbtwhyService.insertMbtwhy(mbtwhy);
				} else if(reportType=="댓글") {
					MbtwhyComment mbtwhyComment = mbtwhyService.selectMbtwhyComment(commentNo);
					mbtwhyComment.setIsBlocked("Y");
					mbtwhyService.insertMbtwhyComment(mbtwhyComment);
				}
			} else if(tableType=="mbattle") {
				if(reportType=="게시글") {
					Mbattle mbattle = mbattleService.selectMbattleByNo(postNo);
					mbattle.setIsBlocked("Y");
					mbattleService.insertMbattle(mbattle);
				} else if(reportType=="댓글") {
					MbattleComment mbattleComment = mbattleService.selectMbattleComment(commentNo);
					mbattleComment.setIsBlocked("Y");
					mbattleService.insertMbattleComment(mbattleComment);
				}
			}
			
			// 경고 알람 처리
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			Alarm warningAlarm = Alarm.builder().username(username).alarmType("경고").alarmTargetNo(reportList.get(0).getNo())
					.alarmTargetFrom("report").alarmUpdateDate(writeDate).build();
			alarmService.addAlarm(warningAlarm);
			
			// 새로운 밴이 삽입되는 경우 알람 처리
			if(user.getUserWarnCnt() != 0 && user.getUserWarnCnt() % 3 == 0) {
				Alarm banAlarm = Alarm.builder().username(username).alarmType("제재").alarmTargetNo(reportList.get(0).getNo())
						.alarmTargetFrom("ban").alarmUpdateDate(writeDate).build();
				alarmService.addAlarm(banAlarm);
			}
			
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 처리 (미경고)
	@PostMapping("/reportprocess")
	public ResponseEntity<Object> reportProcess(@RequestParam(required = false) String reportType,
			@RequestParam(required = false) String tableType, @RequestParam(required = false) Integer postNo,
			@RequestParam(required = false) Integer commentNo) {
		try {
			// report 테이블에 해당하는 no의 isCompleted 컬럼을 Y로 업데이트
			List<Report> reportList = null;
			if(reportType.equals("게시글")) {		
				System.out.println(1);
				reportList = reportService.selectReportListByTableTypeAndPostNo(tableType, postNo);
			} else if(reportType.equals("댓글")) {
				System.out.println(2);
				reportList = reportService.selectReportListByTableTypeAndPostNoCommentNo(tableType, postNo, commentNo);
			}
			
			for(int i = 0;i < reportList.size();i++) {
				reportList.get(i).setIsCompleted("Y");
				reportService.insertReport(reportList.get(i));	
			}
			
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
