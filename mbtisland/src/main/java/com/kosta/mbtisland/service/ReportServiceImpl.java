package com.kosta.mbtisland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Report;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.ReportDslRepository;
import com.kosta.mbtisland.repository.ReportRepository;
import com.kosta.mbtisland.repository.UserDslRepository;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private ReportDslRepository reportDslRepository;
	
	@Autowired
	private UserDslRepository userDslRepository;

	// 신고 삽입
	@Override
	public void insertReport(Report report) throws Exception {
		reportRepository.save(report);
	}
	
	// 신고 목록 조회
	@Override
	public List<Report> selectReportListByPageAndFilterAndBoardTypeAndReportType(PageInfo pageInfo, String filter,
			String boardType, String reportType) throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<Report> reportList = reportDslRepository.findReportListByPageAndFilterAndBoardTypeAndReportType(pageRequest, filter, boardType, reportType);
		
		if (reportList.size() != 0) {
			// 페이징 계산
			Map<String, Integer> counts = new HashMap<>();			
			Long allCount = reportDslRepository.findReportCountByFilterAndBoardTypeAndReportType(filter, boardType, reportType);
			Integer allPage = allCount.intValue() / pageRequest.getPageSize();
			if (allCount % pageRequest.getPageSize() != 0)
				allPage += 1;
			Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
			Integer endPage = Math.min(startPage + 10 - 1, allPage);

			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			return reportList;
		}

		return reportList;
	}
	
	// 밴 페이지에서의
	// 신고 목록 조회 (페이지 번호, 유저 아이디)
	@Override
	public List<Report> selectReportListByPageAndUsername(PageInfo pageInfo, String username) throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<Report> reportList = reportDslRepository.findReportListByPageAndUsername(pageRequest, username);
		
		if (reportList.size() != 0) {
			// 페이징 계산
			Map<String, Integer> counts = new HashMap<>();			
			Long allCount = reportDslRepository.findReportCountByUsername(username);
			Integer allPage = allCount.intValue() / pageRequest.getPageSize();
			if (allCount % pageRequest.getPageSize() != 0)
				allPage += 1;
			Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
			Integer endPage = Math.min(startPage + 10 - 1, allPage);

			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			return reportList;
		}

		return reportList;
	}
	
	// 신고 조회
	@Override
	public Report selectReportByNo(Integer no) throws Exception {
		Optional<Report> oreport = reportRepository.findById(no);
		if(oreport.isEmpty()) throw new Exception(no + "번 게시글이 존재하지 않습니다.");
		Report report = oreport.get();
		return report;
	}
	
	// 게시글형 신고 목록 조회 (테이블 타입, 게시글 번호)
	@Override
	public List<Report> selectReportListByTableTypeAndPostNo(String tableType, Integer postNo) throws Exception {
		List<Report> reportList = reportDslRepository.findReportListByBoardTypeAndPostNo(tableType, postNo);
		System.out.println("서비스:" + reportList);
		return reportList;
	}
	
	// 댓글형 신고 목록 조회 (테이블 타입, 게시글 번호, 댓글 번호)
	@Override
	public List<Report> selectReportListByTableTypeAndPostNoCommentNo(String tableType, Integer postNo, Integer commentNo) throws Exception {
		List<Report> reportList = reportDslRepository.findReportListByBoardTypeAndPostNoAndCommentNo(tableType, postNo, commentNo);
		System.out.println("서비스:" + reportList);
		return reportList;
	}
	
	// 전체 경고된 유저 목록
	@Override
	public List<UserEntity> selectBannedUserListByPageAndFilterAndUsername(PageInfo pageInfo, String filter, String username) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<UserEntity> banList = userDslRepository.findBannedUserListByPageAndFilterAndUsername(pageRequest, filter, username);
		
		if (banList.size() != 0) {
			// 페이징 계산
			Map<String, Integer> counts = new HashMap<>();			
			Long allCount = userDslRepository.findBanCountByFilterAndUsername(filter, username);
			Integer allPage = allCount.intValue() / pageRequest.getPageSize();
			if (allCount % pageRequest.getPageSize() != 0)
				allPage += 1;
			Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
			Integer endPage = Math.min(startPage + 10 - 1, allPage);

			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			return banList;
		}

		return banList;
	}
	
	// 경고 유저 (단일 조회)
	@Override
	public UserEntity selectBannedUserByUsername(String username) throws Exception {
		return userDslRepository.findBannedUserByUsername(username);
	}
}
