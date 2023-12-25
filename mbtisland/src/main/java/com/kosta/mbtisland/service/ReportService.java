package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Report;

public interface ReportService {
	public void insertReport(Report report) throws Exception; // 신고 삽입
	public List<Report> selectReportListByPageAndFilterAndBoardTypeAndReportType(PageInfo pageInfo, String filter, String boardType, String reportType) throws Exception; // 신고 목록 조회
	public Report selectReportByNo(Integer no) throws Exception; // 신고 조회
	public List<Report> selectReportListByTableTypeAndPostNo(String tableType, Integer postNo) throws Exception; // 신고 목록 조회 (테이블 타입, 게시글 번호)
	public List<Report> selectReportListByTableTypeAndPostNoCommentNo(String tableType, Integer postNo, Integer commetNo) throws Exception; // 신고 목록 조회 (테이블 타입, 게시글 번호, 댓글 번호)
}
