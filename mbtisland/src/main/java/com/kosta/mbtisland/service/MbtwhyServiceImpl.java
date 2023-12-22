package com.kosta.mbtisland.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.MbtwhyDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.repository.MbtwhyCommentRepository;
import com.kosta.mbtisland.repository.MbtwhyDslRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;

@Service
public class MbtwhyServiceImpl implements MbtwhyService {
	
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
//	private final MbtwhyRepository mbtwhyRepository;
	
	@Autowired
	private MbtwhyDslRepository mbtwhyDslRepository;
	
	@Autowired
	private MbtwhyCommentRepository mbtwhyCommentRepository;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	// 댓글수 포함
	@Override
	public List<MbtwhyDto> selectMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbti, PageInfo pageInfo, String search, String sort) throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5);
		List<Mbtwhy> mbtwhyList = mbtwhyDslRepository.findMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageRequest, search, sort);

		List<MbtwhyDto> dtoList = new ArrayList<>();
		if(mbtwhyList.size()!=0) {
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

		return dtoList;
	}
	
	// 게시글 개수 조회 (MBTI 타입, 검색 값, 정렬 옵션)
	@Override
	public Long selectMbtwhyCountByMbtiAndSearch(String mbti, String search) throws Exception {
		return mbtwhyDslRepository.findMbtwhyCountByMbtiAndSearch(mbti, search);
	}
	
//	// DTO 게시글 조회 (게시글 번호)
//	@Override
//	public MbtwhyDto selectMbtwhyDtoByNo(Integer no) throws Exception {
//		Mbtwhy mbtwhy = mbtwhyRepository.findById(no).get();
//		
//		if(mbtwhy!=null) {
//			Integer commentCnt = selectMbtwhyCommentCountByMbtwhyNo(mbtwhy.getNo());
//			MbtwhyDto dto = MbtwhyDto.builder().no(mbtwhy.getNo()).content(mbtwhy.getContent()).mbtiCategory(mbtwhy.getMbtiCategory())
//					.viewCnt(mbtwhy.getViewCnt()).recommendCnt(mbtwhy.getRecommendCnt()).writeDate(mbtwhy.getWriteDate())
//					.isBlocked(mbtwhy.getIsBlocked()).writerId(mbtwhy.getWriterId()).writerNickname(mbtwhy.getWriterNickname())
//					.writerMbti(mbtwhy.getWriterMbti()).writerMbtiColor(mbtwhy.getWriterMbtiColor()).commentCnt(commentCnt).build();
//			return dto;
//		}
//		return null;
//	}
	
	// Entity 게시글 조회 (게시글 번호)
	@Override
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception {
		Optional<Mbtwhy> ombtwhy = mbtwhyRepository.findById(no);
		if(ombtwhy.isEmpty()) throw new Exception(no + "번 게시글이 존재하지 않습니다.");
		Mbtwhy mbtwhy = ombtwhy.get();
		return mbtwhy;
	}
	
	// 조회수 증가
	@Override
	public void increaseViewCount(Integer no) throws Exception {
		Mbtwhy mbtwhy = selectMbtwhyByNo(no);
		mbtwhy.setViewCnt(mbtwhy.getViewCnt()+1);
		mbtwhyRepository.save(mbtwhy);
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
	
	// 댓글의 대댓글 수 조회
	@Override
	public Integer selectMbtwhyChildCommentCount(Integer commentNo) throws Exception {
		Long mbtwhyChildCommentCount = mbtwhyDslRepository.countCommentByParentcommentNo(commentNo);
		return mbtwhyChildCommentCount.intValue();
	}
	
	// 게시글 추천 데이터 조회
//	@Override
//	public Recommend selectRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
//		return recommendRepository.findByUsernameAndPostNoAndBoardType(username, postNo, boardType);
//	}
	
	// 게시글 추천 여부 조회
//	@Override
//	public Boolean selectIsRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
//		return recommendRepository.existsByUsernameAndPostNoAndBoardType(username, postNo, boardType);
//	}
	
	// 게시글 추천 개수 조회
//	@Override
//	public Integer selectRecommendCountByPostNoAndBoardType(Integer postNo, String boardType) throws Exception {
//		return mbtwhyRepository.countByPostNoAndBoardType(postNo, boardType);
//	}
	
	// 추천수 증가
	@Override
	public void increaseRecommendCnt(Integer no) throws Exception {
		Mbtwhy mbtwhy = selectMbtwhyByNo(no);
		mbtwhy.setRecommendCnt(mbtwhy.getRecommendCnt()+1);
		mbtwhyRepository.save(mbtwhy);
	}
	
	// 추천수 감소
	@Override
	public void decreaseRecommendCnt(Integer no) throws Exception {
		Mbtwhy mbtwhy = selectMbtwhyByNo(no);
		mbtwhy.setRecommendCnt(mbtwhy.getRecommendCnt()-1);
		mbtwhyRepository.save(mbtwhy);
	}
	
	// 게시글 추천
//	@Override
//	public void insertRecommend(Recommend recommend) throws Exception {
//		recommendRepository.save(recommend);
//	}
//
	// 게시글 추천 취소
//	@Override
//	public void deleteRecommend(Integer no) throws Exception {
//		recommendRepository.deleteById(no);
//	}
//	
	// 게시글 북마크 데이터 조회
//	@Override
//	public Bookmark selectBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
//		return bookmarkRepository.findByUsernameAndPostNoAndBoardType(username, postNo, boardType);
//	}
	
	// 게시글 북마크 여부 조회
//	@Override
//	public Boolean selectIsBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
//		return bookmarkRepository.existsByUsernameAndPostNoAndBoardType(username, postNo, boardType);
//	}
	
	// 게시글 북마크
//	@Override
//	public void insertBookmark(Bookmark bookmark) throws Exception {
//		bookmarkRepository.save(bookmark);
//	}
	
	// 게시글 북마크 취소
//	@Override
//	public void deleteBookmark(Integer no) throws Exception {
//		bookmarkRepository.deleteById(no);
//	}
	
	//마이페이지에서 내가 작성한 mbtwhyList불러오기 ( 회원ID,page로 mbtwhyList )
	@Override
	public List<Mbtwhy> getMyMbtwhyListByPage(String username, PageInfo pageInfo) throws Exception {
		System.out.println(pageInfo.getCurPage());
		List<Mbtwhy> myMbtwhyAllList = mbtwhyRepository.findByWriterId(username);
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		if (myMbtwhyAllList.isEmpty()) {
			throw new Exception("myMbtwhyList 사이즈 0");
		} else {
//				 mbtwhyRepository.count(null)
			List<Mbtwhy> myMbtwhyList = mbtwhyDslRepository.findMbtwhyListUserAndPaging(username, pageRequest);

			Integer allCount = myMbtwhyAllList.size();
			Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
			Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
			Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
			if (endPage > allPage)
				endPage = allPage;
			pageInfo.setAllPage(allPage);
			pageInfo.setStartPage(startPage);
			pageInfo.setEndPage(endPage);

			return myMbtwhyList;
		}
	}

	// noList 삭제
	@Override
	public void deleteMbtwhyList(List<Integer> noList) throws Exception {
		for (Integer no : noList) {
			Optional<Mbtwhy> optionalMbtwhy = mbtwhyRepository.findById(no);
			if (optionalMbtwhy.isPresent()) {
//					optionalMbtwhy.get().setIsRemoved("Y");
//					mbtwhyRepository.save(optionalMbtwhy.get());
				mbtwhyRepository.deleteById(no);
			} else {
				throw new Exception("해당 번호 Mbtwhy게시글 없음");
			}
		}
	}
}
