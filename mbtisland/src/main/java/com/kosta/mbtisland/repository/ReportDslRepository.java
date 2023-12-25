package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QReport.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Report;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReportDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 신고 목록 조회 (특정 페이지, 필터링, 게시판 유형, 신고 유형)
	public List<Report> findReportListByPageAndFilterAndBoardTypeAndReportType
		(PageRequest pageRequest, String filter, String boardType, String reportType) throws Exception {

		return jpaQueryFactory.selectFrom(report)
				.where(filter.equals("all")? null : report.isCompleted.eq(filter),
						boardType.equals("all")? null : report.tableType.containsIgnoreCase(boardType),
						reportType.equals("전체")? null : report.reportReason.eq(reportType))
						.orderBy(report.no.desc()) // 정렬
						.offset(pageRequest.getOffset()) // 인덱스
						.limit(pageRequest.getPageSize()) // 개수 제한
						.fetch();
	}
	
	// 신고 개수
	public Long findReportCountByFilterAndBoardTypeAndReportType(String filter, String boardType, String reportType) throws Exception {
		return jpaQueryFactory.select(report.count())
				.from(report)
				.where(filter.equals("all")? null : report.isCompleted.eq(filter),
						boardType.equals("all")? null : report.tableType.eq(boardType),
						reportType.equals("전체")? null : report.reportReason.eq(reportType))
				.orderBy(report.no.desc())
				.fetchOne();
	}
	
	// 신고 목록 조회 (테이블 타입, 게시글 번호)
	public List<Report> findReportListByBoardTypeAndPostNo(String tableType, Integer postNo) throws Exception {
		return jpaQueryFactory.selectFrom(report)
				.where(report.tableType.eq(tableType),
						report.reportedPostNo.eq(postNo),
						report.isCompleted.eq("N"))
				.fetch();
	}
	
	// 신고 목록 조회 (테이블 타입, 게시글 번호, 댓글 번호)
	public List<Report> findReportListByBoardTypeAndPostNoAndCommentNo(String tableType, Integer postNo, Integer commentNo) throws Exception {
		System.out.println("댓글 번호" + commentNo);
		return jpaQueryFactory.selectFrom(report)
				.where(report.tableType.eq(tableType),
						report.reportedPostNo.eq(postNo),
						report.reportedCommentNo.eq(commentNo),
						report.isCompleted.eq("N"))
				.fetch();
	}
}
