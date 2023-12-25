package com.kosta.mbtisland.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.MbtmiDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.Notice;
import com.kosta.mbtisland.repository.MbtmiCommentRepository;
import com.kosta.mbtisland.repository.MbtmiDslRepository;
import com.kosta.mbtisland.repository.MbtmiRepository;
import com.querydsl.core.Tuple;

@Service
public class MbtmiServiceImpl implements MbtmiService {
	
	@Autowired
	private MbtmiRepository mbtmiRepository;
	@Autowired
	private MbtmiDslRepository mbtmiDslRepository;
	@Autowired
	private MbtmiCommentRepository mbtmiCommentRepository;

	
	// 주간인기글 목록
	@Override
	public List<MbtmiDto> weeklyHotMbtmiList() throws Exception {
		List<Mbtmi> weeklyHotMbtmiList = mbtmiDslRepository.findWeeklyHotMbtmiListByCategoryAndRecommendCnt();
		if(weeklyHotMbtmiList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		List<MbtmiDto> dtoList = new ArrayList<MbtmiDto>();
		// Entity->Dto로 변경하며 각 게시글마다 댓글수 넣기
		for (Mbtmi mbtmi : weeklyHotMbtmiList) {
			Integer commentCnt = mbtmiCommentCnt(mbtmi.getNo());
			MbtmiDto dto = MbtmiDto.builder().no(mbtmi.getNo()).title(mbtmi.getTitle()).content(mbtmi.getContent()).category(mbtmi.getCategory())
								.viewCnt(mbtmi.getViewCnt()).recommendCnt(mbtmi.getRecommendCnt()).writeDate(mbtmi.getWriteDate())
								.isBlocked(mbtmi.getIsBlocked()).writerId(mbtmi.getWriterId()).writerNickname(mbtmi.getWriterNickname())
								.writerMbti(mbtmi.getWriterMbti()).writerMbtiColor(mbtmi.getWriterMbtiColor()).fileIdxs(mbtmi.getFileIdxs())
								.commentCnt(commentCnt).build();
			dtoList.add(dto);
		}
		return dtoList;
	}

	// 최신글 목록
	@Override
	public List<MbtmiDto> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo, String sort, String username) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
//		List<Mbtmi> mbtmiList = mbtmiDslRepository.findNewlyMbtmiListByCategoryAndTypeAndSearchAndPaging(category, type, searchTerm, pageRequest, sort);
		List<Mbtmi> mbtmiList = mbtmiDslRepository.findNewlyMbtmiListByCategoryAndTypeAndSearchAndPaging(category, type, searchTerm, pageRequest, sort, username);
		
		if(mbtmiList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		List<MbtmiDto> dtoList = new ArrayList<MbtmiDto>();
		// Entity->Dto로 변경하며 각 게시글마다 댓글수 넣기
		for (Mbtmi mbtmi : mbtmiList) {
			Integer commentCnt = mbtmiCommentCnt(mbtmi.getNo());
			MbtmiDto dto = MbtmiDto.builder().no(mbtmi.getNo()).title(mbtmi.getTitle()).content(mbtmi.getContent()).category(mbtmi.getCategory())
								.viewCnt(mbtmi.getViewCnt()).recommendCnt(mbtmi.getRecommendCnt()).writeDate(mbtmi.getWriteDate())
								.isBlocked(mbtmi.getIsBlocked()).writerId(mbtmi.getWriterId()).writerNickname(mbtmi.getWriterNickname())
								.writerMbti(mbtmi.getWriterMbti()).writerMbtiColor(mbtmi.getWriterMbtiColor()).fileIdxs(mbtmi.getFileIdxs())
								.commentCnt(commentCnt).build();
			dtoList.add(dto);
		}
		
		Integer allCount = mbtmiCntByCriteria(category, type, searchTerm, username);
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if(pageInfo.getCurPage()>allPage) pageInfo.setCurPage(allPage); // 게시글 삭제시 예외처리
		
		return dtoList;
	}

	// 최신글수 조회 (PageInfo의 allPage값 계산시 필요)
	@Override
	public Integer mbtmiCntByCriteria(String category, String type, String searchTerm, String username) throws Exception {
		// 경우의 수 2^3=8
//		Long mbtmiCnt = 0L;
//		if (category == null && type == null && searchTerm == null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(null, null, null);
//		
//		else if (category != null && type == null && searchTerm == null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(category, null, null);
//		else if (category == null && type != null && searchTerm == null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(null, type, null);
//		else if (category == null && type == null && searchTerm != null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(null, null, searchTerm);
//		
//		else if (category != null && type != null && searchTerm == null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(category, type, null);
//		else if (category != null && type == null && searchTerm != null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(category, null, searchTerm);
//		else if (category == null && type != null && searchTerm != null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(null, type, searchTerm);
//		
//		else if (category != null && type != null && searchTerm != null) mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(category, type, searchTerm);
		
		Long mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(category, type, searchTerm, username);
		return mbtmiCnt.intValue();
	}
	
	// mbtmi 상세 조회
	@Override
	public Mbtmi mbtmiDetail(Integer no) throws Exception {
		Optional<Mbtmi> ombtmi = mbtmiRepository.findById(no);
		if(ombtmi.isEmpty()) throw new Exception(no + "번 게시글이 존재하지 않습니다.");
		Mbtmi mbtmi = ombtmi.get();
		return mbtmi;
	}

	// 조회수 증가
	@Override
	public void increaseViewCount(Integer no) throws Exception {
		Mbtmi mbtmi = mbtmiDetail(no);
		mbtmi.setViewCnt(mbtmi.getViewCnt()+1);
		mbtmiRepository.save(mbtmi);
		
	}
	
	// 댓글 목록
	@Override
	public List<MbtmiComment> mbtmiCommentListByMbtmiNo(Integer mbtmiNo, PageInfo pageInfo) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<MbtmiComment> mbtmiCommentList = mbtmiDslRepository.findMbtmiCommentListByMbtmiNoAndPaging(mbtmiNo, pageRequest);
		
		Integer allCount = mbtmiCommentCnt(mbtmiNo);
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if(pageInfo.getCurPage()>allPage) pageInfo.setCurPage(allPage); // 게시글 삭제시 예외처리
		
		return mbtmiCommentList;
	}
	
	// 특정 게시글의 댓글수 조회 (PageInfo의 allPage값 계산시 필요)
	@Override
	public Integer mbtmiCommentCnt(Integer mbtmiNo) throws Exception {
//		Long mbtmiCommentCnt = mbtmiCommentRepository.countByMbtmiNo(mbtmiNo);
		Long mbtmiCommentCnt = mbtmiDslRepository.countCommentByMbtmiNo(mbtmiNo);
		return mbtmiCommentCnt.intValue();
	}

	// 게시글 삭제
	@Override
	public void deleteMbtmi(Integer no) throws Exception {
		Mbtmi mbtmi = mbtmiRepository.findById(no).get();
		if(mbtmi==null) throw new Exception("게시글이 존재하지 않습니다.");
		mbtmiRepository.deleteById(no);
	}
	
	
	

	// 댓글 삭제(IS_REMOVED 컬럼값 업데이트)
	@Override
	public void deleteMbtmiComment(Integer commentNo) throws Exception {
		Optional<MbtmiComment> targetComment = mbtmiCommentRepository.findById(commentNo);
		if(targetComment.isEmpty()) throw new Exception("댓글이 존재하지 않습니다");
		targetComment.get().setIsRemoved("Y");
		mbtmiCommentRepository.save(targetComment.get());
	}

	// 댓글 등록
	@Override
	public void addMbtmiComment(MbtmiComment mbtmiComment) throws Exception {
		mbtmiCommentRepository.save(mbtmiComment);
	}

	// 게시글 등록
	@Override
	public Mbtmi addMbtmi(MbtmiDto mbtmiDto) throws Exception {
		LocalDate currentDate = LocalDate.now();
		Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
		Mbtmi mbtmi = Mbtmi.builder()
						.title(mbtmiDto.getTitle())
						.content(mbtmiDto.getContent())
						.category(mbtmiDto.getCategory())
						.writeDate(writeDate)
						.writerId(mbtmiDto.getWriterId())
						.writerNickname(mbtmiDto.getWriterNickname())
						.writerMbti(mbtmiDto.getWriterMbti())
						.writerMbtiColor(mbtmiDto.getWriterMbtiColor())
						.build();
		mbtmiRepository.save(mbtmi);
		Optional<Mbtmi> ombtmi = mbtmiRepository.findById(mbtmi.getNo());
		if(ombtmi.isPresent()) return ombtmi.get(); 
		else return null;
	}

	// 추천수 증가, 감소
	@Override
	public void increaseRecommendCnt(Integer no) throws Exception {
		Mbtmi mbtmi = mbtmiDetail(no);
		mbtmi.setRecommendCnt(mbtmi.getRecommendCnt()+1);
		mbtmiRepository.save(mbtmi);
	}
	@Override
	public void decreaseRecommendCnt(Integer no) throws Exception {
		Mbtmi mbtmi = mbtmiDetail(no);
		mbtmi.setRecommendCnt(mbtmi.getRecommendCnt()-1);
		mbtmiRepository.save(mbtmi);
	}

	// 댓글의 대댓글 수 조회
	@Override
	public Integer mbtmiChildCommentCnt(Integer mbtmiCommentNo) throws Exception {
		Long mbtmiChildCommentCnt = mbtmiDslRepository.countCommentByParentcommentNo(mbtmiCommentNo);
		return mbtmiChildCommentCnt.intValue();
	}

	// 게시글 수정
	@Override
	public Mbtmi modifyMbtmi(MbtmiDto mbtmiDto) throws Exception {
		Mbtmi existMbtmi = mbtmiRepository.findById(mbtmiDto.getNo()).get();
		if(existMbtmi==null) throw new Exception("해당 게시글이 존재하지 않습니다.");
		
		// 변경 가능한 값
		existMbtmi.setTitle(mbtmiDto.getTitle());
		existMbtmi.setContent(mbtmiDto.getContent());
		existMbtmi.setCategory(mbtmiDto.getCategory());

		if(existMbtmi.getViewCnt()>2) existMbtmi.setViewCnt(existMbtmi.getViewCnt()-2);
		else existMbtmi.setViewCnt(0);

		mbtmiRepository.save(existMbtmi); // 업데이트
		return existMbtmi;
	}

	@Override
	public void deleteMbtmiList(List<Integer> noList) throws Exception {
		for (Integer no : noList) {
			Optional<Mbtmi> optionalMbtmi = mbtmiRepository.findById(no);
			if (optionalMbtmi.isPresent()) {
				mbtmiRepository.deleteById(no);
			} else {
				throw new Exception("해당 번호 Mbtmi게시글 없음");
			}
		}
	}
	
	// 게시글 등록 (업데이트용)
	@Override
	public void addMbtmiForUpdate(Mbtmi mbtmi) throws Exception {
		mbtmiRepository.save(mbtmi);
	}
	
	// 댓글 조회
	@Override
	public MbtmiComment mbtmiComment(Integer no) throws Exception {
		Optional<MbtmiComment> ombtmiComment = mbtmiCommentRepository.findById(no);
		if(ombtmiComment.isEmpty()) throw new Exception(no + "번 댓글이 존재하지 않습니다.");
		MbtmiComment mbtmiComment = ombtmiComment.get();
		return mbtmiComment;
	}
}
