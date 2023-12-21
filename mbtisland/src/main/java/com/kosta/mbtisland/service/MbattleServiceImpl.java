package com.kosta.mbtisland.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.MbtwhyDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleComment;
import com.kosta.mbtisland.entity.MbattleVoter;
import com.kosta.mbtisland.repository.MbattleCommentRepository;
import com.kosta.mbtisland.repository.MbattleDslRepository;
import com.kosta.mbtisland.repository.MbattleRepository;
import com.kosta.mbtisland.repository.MbattleVoterRepository;

@Service
public class MbattleServiceImpl implements MbattleService {
	@Autowired
	private MbattleRepository mbattleRepository;
	
	@Autowired
	private MbattleDslRepository mbattleDslRepository;
	
	@Autowired
	private MbattleCommentRepository mbattleCommentRepository;
	
	@Autowired
	private MbattleVoterRepository mbattleVoterRepository;
	
	// 게시글 목록 조회
	@Override
	public List<Mbattle> selectMbattleListByPageAndSearchAndSort(PageInfo pageInfo, String search, String sort)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 일간 인기 게시글 조회
	@Override
	public List<Mbattle> selectDailyHotMbattle() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Entity 게시글 조회
	@Override
	public Mbattle selectMbattleByNo(Integer no) throws Exception {
		Optional<Mbattle> ombattle = mbattleRepository.findById(no);
		if(ombattle.isEmpty()) throw new Exception(no + "번 게시글이 존재하지 않습니다.");
		Mbattle mbattle = ombattle.get();
		return mbattle;
	}
	
	// 조회수 증가
	@Override
	public void increaseViewCount(Integer no) throws Exception {
		Mbattle mbattle = selectMbattleByNo(no);
		mbattle.setViewCnt(mbattle.getViewCnt()+1);
		mbattleRepository.save(mbattle);
	}
	
	// 게시글 작성
	@Override
	public Integer insertMbattle(Mbattle mbattle) throws Exception {
		return mbattleRepository.save(mbattle).getNo();
	}
	
	// 게시글 삭제
	@Override
	public void deleteMbattle(Integer no) throws Exception {
		Mbattle mbattle = mbattleRepository.findById(no).get();
		if(mbattle==null) throw new Exception("게시글이 존재하지 않습니다.");
		mbattleRepository.deleteById(no);
	}
	
	// 댓글 목록 조회
	@Override
	public List<MbattleComment> selectMbattleCommentListByMbattleNoAndPage(Integer no, PageInfo pageInfo)
			throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<MbattleComment> mbattleCommentList = mbattleDslRepository.findMbattleCommentListByMbattleNoAndPage(no, pageRequest);
					
		// 페이징 계산
		// MbattleCommentController에서 넘겨준 pageInfo를 참조하기에, 반환하지 않아도 됨
		Integer allCount = mbattleCommentRepository.countByMbattleNo(no);
		Integer allPage = allCount.intValue() / pageRequest.getPageSize();
		if(allCount % pageRequest.getPageSize()!=0) allPage += 1;
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
							
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		// 게시글 삭제 시 예외처리?
//		if(pageInfo.getCurPage() > allPage) pageInfo.setCurPage(allPage);
									
		return mbattleCommentList;
	}
	
	// 댓글 개수 조회
	@Override
	public Integer selectMbattleCommentCountByMbattleNo(Integer no) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 댓글 작성
	@Override
	public void insertMbattleComment(MbattleComment mbattleComment) throws Exception {
		mbattleCommentRepository.save(mbattleComment);
	}
	
	// 댓글 삭제
	@Override
	public void deleteMbattleComment(Integer commentNo) throws Exception {
		Optional<MbattleComment> targetComment = mbattleCommentRepository.findById(commentNo);
		if(targetComment.isEmpty()) throw new Exception("댓글이 존재하지 않습니다");
		targetComment.get().setIsRemoved("Y");
		mbattleCommentRepository.save(targetComment.get());
	}

	// 투표 데이터 조회
	@Override
	public MbattleVoter selectIsVoteByUsernameAndPostNo(String voterId, Integer no) throws Exception {
		return mbattleVoterRepository.findByVoterIdAndMbattleNo(voterId, no);
	}
}
