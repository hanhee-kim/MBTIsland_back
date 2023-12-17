package com.kosta.mbtisland.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.MbtwhyDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.repository.BookmarkRepository;
import com.kosta.mbtisland.repository.MbtwhyCommentRepository;
import com.kosta.mbtisland.repository.MbtwhyDslRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;
import com.kosta.mbtisland.repository.RecommendRepository;

@Service
public class MbtwhyServiceImpl implements MbtwhyService {
	
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
//	private final MbtwhyRepository mbtwhyRepository;
	
	@Autowired
	private MbtwhyDslRepository mbtwhyDslRepository;
	
	@Autowired
	private MbtwhyCommentRepository mbtwhyCommentRepository;
	
	@Autowired
	private RecommendRepository recommendRepository;
	
	@Autowired
	private BookmarkRepository bookmarkRepository;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	// 댓글수 포함
	@Override
	public List<MbtwhyDto> selectMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbti, PageInfo pageInfo, String search, String sort) throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5);
		List<Mbtwhy> mbtwhyList = mbtwhyDslRepository.findMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageRequest, search, sort);

		if(mbtwhyList.size()!=0) {
			List<MbtwhyDto> dtoList = new ArrayList<>();
			// Entity => Dto
			// 각 Mbtwhy 객체 변수마다 댓글 개수 추가
			for(Mbtwhy mbtwhy : mbtwhyList) {
				Integer commentCnt = selectMbtwhyCommentCountByMbtwhyNo(mbtwhy.getNo());
				
				MbtwhyDto dto = MbtwhyDto.builder().no(mbtwhy.getNo()).content(mbtwhy.getContent()).mbtiCategory(mbtwhy.getMbtiCategory())
						.viewCnt(mbtwhy.getViewCnt()).recommendCnt(mbtwhy.getRecommendCnt()).writeDate(mbtwhy.getWriteDate())
						.isBlocked(mbtwhy.getIsBlocked()).writerId(mbtwhy.getWriterId()).writerNickname(mbtwhy.getWriterNickname())
						.writerMbti(mbtwhy.getWriterMbti()).writerMbtiColor(mbtwhy.getWriterMbtiColor()).commentCnt(commentCnt).build();
				
				dtoList.add(dto);
			}
			
			// 페이징 계산
			// MbtwhyController에서 넘겨준 pageInfo를 참조하기에, 반환하지 않아도 됨
			Long allCount = mbtwhyDslRepository.findMbtwhyCountByMbtiAndSearch(mbti, search);
			Integer allPage = allCount.intValue() / pageRequest.getPageSize();
			if(allCount % pageRequest.getPageSize()!=0) allPage += 1;
			Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
			Integer endPage = Math.min(startPage + 10 - 1, allPage);
			
			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);
			
			return dtoList;
		}
		
		return null;		
	}
	
	// 게시글 개수 조회 (MBTI 타입, 검색 값, 정렬 옵션)
	@Override
	public Long selectMbtwhyCountByMbtiAndSearch(String mbti, String search) throws Exception {
		return mbtwhyDslRepository.findMbtwhyCountByMbtiAndSearch(mbti, search);
	}
	
	// DTO 게시글 조회 (게시글 번호)
	@Override
	public MbtwhyDto selectMbtwhyDtoByNo(Integer no) throws Exception {
		Mbtwhy mbtwhy = mbtwhyRepository.findById(no).get();
		
		if(mbtwhy!=null) {
			Integer commentCnt = selectMbtwhyCommentCountByMbtwhyNo(mbtwhy.getNo());
			MbtwhyDto dto = MbtwhyDto.builder().no(mbtwhy.getNo()).content(mbtwhy.getContent()).mbtiCategory(mbtwhy.getMbtiCategory())
					.viewCnt(mbtwhy.getViewCnt()).recommendCnt(mbtwhy.getRecommendCnt()).writeDate(mbtwhy.getWriteDate())
					.isBlocked(mbtwhy.getIsBlocked()).writerId(mbtwhy.getWriterId()).writerNickname(mbtwhy.getWriterNickname())
					.writerMbti(mbtwhy.getWriterMbti()).writerMbtiColor(mbtwhy.getWriterMbtiColor()).commentCnt(commentCnt).build();
			return dto;
		}
		return null;
	}
	
	// Entity 게시글 조회 (게시글 번호)
	@Override
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception {
		return mbtwhyRepository.findById(no).get();
	}
	
	// 일간 인기 게시글 조회 (MBTI)
	// 댓글수 포함
	@Override
	public MbtwhyDto selectDailyHotMbtwhy(String mbti) throws Exception {
		Mbtwhy mbtwhy = mbtwhyDslRepository.findDailyHotMbtwhy(mbti);
		
		if(mbtwhy!=null) {
			Integer commentCnt = selectMbtwhyCommentCountByMbtwhyNo(mbtwhy.getNo());
			MbtwhyDto dto = MbtwhyDto.builder().no(mbtwhy.getNo()).content(mbtwhy.getContent()).mbtiCategory(mbtwhy.getMbtiCategory())
					.viewCnt(mbtwhy.getViewCnt()).recommendCnt(mbtwhy.getRecommendCnt()).writeDate(mbtwhy.getWriteDate())
					.isBlocked(mbtwhy.getIsBlocked()).writerId(mbtwhy.getWriterId()).writerNickname(mbtwhy.getWriterNickname())
					.writerMbti(mbtwhy.getWriterMbti()).writerMbtiColor(mbtwhy.getWriterMbtiColor()).commentCnt(commentCnt).build();
			return dto;
		}
		return null;
	}
	
	// 게시글 작성
	@Override
	public Integer insertMbtwhy(Mbtwhy mbtwhy) throws Exception {
		return mbtwhyRepository.save(mbtwhy).getNo();
	}
	
	// 게시글 삭제
	@Override
	public void deleteMbtwhy(Integer no) throws Exception {
		Mbtwhy mbtwhy = mbtwhyRepository.findById(no).get();
		if(mbtwhy==null) throw new Exception("게시글이 존재하지 않습니다.");
		mbtwhyRepository.deleteById(no);
	}
	
	// 댓글 목록 조회 (게시글 번호, 페이지 정보)
	@Override
	public List<MbtwhyComment> selectMbtwhyCommentListByMbtwhyNoAndPage(Integer no, PageInfo pageInfo)
			throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<MbtwhyComment> mbtwhyCommentList = mbtwhyDslRepository.findMbtwhyCommentListByMbtwhyNoAndPage(no, pageRequest);
					
		// 페이징 계산
		// MbtwhyCommentController에서 넘겨준 pageInfo를 참조하기에, 반환하지 않아도 됨
		Integer allCount = mbtwhyCommentRepository.countByMbtwhyNo(no);
		Integer allPage = allCount.intValue() / pageRequest.getPageSize();
		if(allCount % pageRequest.getPageSize()!=0) allPage += 1;
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
							
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		// 게시글 삭제 시 예외처리?
//		if(pageInfo.getCurPage() > allPage) pageInfo.setCurPage(allPage);
									
		return mbtwhyCommentList;
	}

	// 댓글 개수 조회 (게시글 번호)
	@Override
	public Integer selectMbtwhyCommentCountByMbtwhyNo(Integer no) throws Exception {
		//return mbtwhyDslRepository.findMbtwhyCommentCountByMbtwhyNo(no).intValue();
		return mbtwhyCommentRepository.countByMbtwhyNo(no);
	}

	// 댓글 작성
	@Override
	public void insertMbtwhyComment(MbtwhyComment mbtwhyComment) throws Exception {
		mbtwhyCommentRepository.save(mbtwhyComment);
	}
	
	// 댓글 삭제
	@Override
	public void deleteMbtwhyComment(Integer commentNo) throws Exception {
		Optional<MbtwhyComment> targetComment = mbtwhyCommentRepository.findById(commentNo);
		if(targetComment.isEmpty()) throw new Exception("댓글이 존재하지 않습니다");
		targetComment.get().setIsRemoved("Y");
		mbtwhyCommentRepository.save(targetComment.get());
	}
	
	// 게시글 추천 데이터 조회
	@Override
	public Recommend selectRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
		return recommendRepository.findByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}
	
	// 게시글 추천 여부 조회
	@Override
	public Boolean selectIsRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
		return recommendRepository.existsByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}
	
	// 게시글 추천 개수 조회
	@Override
	public Integer selectRecommendCountByPostNoAndBoardType(Integer postNo, String boardType) throws Exception {
		return recommendRepository.countByPostNoAndBoardType(postNo, boardType);
	}
	
	// 게시글 추천
	@Override
	public void insertRecommend(Recommend recommend) throws Exception {
		recommendRepository.save(recommend);
	}

	// 게시글 추천 취소
	@Override
	public void deleteRecommend(Integer no) throws Exception {
		recommendRepository.deleteById(no);
	}
	
	// 게시글 북마크 데이터 조회
	@Override
	public Bookmark selectBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
		return bookmarkRepository.findByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}
	
	// 게시글 북마크 여부 조회
	@Override
	public Boolean selectIsBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
		return bookmarkRepository.existsByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}
	
	// 게시글 북마크
	@Override
	public void insertBookmark(Bookmark bookmark) throws Exception {
		bookmarkRepository.save(bookmark);
	}
	
	// 게시글 북마크 취소
	@Override
	public void deleteBookmark(Integer no) throws Exception {
		bookmarkRepository.deleteById(no);
	}
}
