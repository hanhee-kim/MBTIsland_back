package com.kosta.mbtisland.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.AlarmDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.MbattleComment;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.entity.Note;
import com.kosta.mbtisland.repository.AlarmDslRepository;
import com.kosta.mbtisland.repository.AlarmRepository;
import com.kosta.mbtisland.repository.MbattleCommentRepository;
import com.kosta.mbtisland.repository.MbtmiCommentRepository;
import com.kosta.mbtisland.repository.MbtwhyCommentRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;
import com.kosta.mbtisland.repository.NoteRepository;
import com.kosta.mbtisland.repository.UserRepository;
@Service
public class AlarmServiceImpl implements AlarmService{
	
	@Autowired
	private AlarmRepository alarmRepository;
	@Autowired
	private AlarmDslRepository alarmDslRepository;
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
	@Autowired
	private MbtwhyCommentRepository mbtwhyCommentRepository;
	@Autowired
	private MbtmiCommentRepository mbtmiCommentRepository;
	@Autowired
	private MbattleCommentRepository mbattleCommentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NoteRepository noteRepository;
	
	public List<AlarmDto> alarmToAlarmDto(List<Alarm> alarmList) throws Exception{

		List<AlarmDto> alarmDtoList = new ArrayList<AlarmDto>();
		for(Alarm alarm : alarmList) {
			String myContent = null;
			String type = null;
			Integer no = null;
			String mbti = null;
			Timestamp date = null;
			Integer warnCnt = null;
			//myContent 정할때
			if(alarm.getAlarmTargetFrom().toUpperCase().contains("COMMENT")) {
				myContent = "내 댓글";
				
			} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("NOTE")) {
				myContent = "내 쪽지";
			} else {
				myContent = "내 게시글";
			}
			//detailType정하고 경고와 벤에 따른 데이터 삽입
			if(alarm.getAlarmTargetFrom().toUpperCase().contains("MBTMI")) {
				type = "MBTMI";
			} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("MBTWHY")) {
				type = "MBTWHY";
			} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("MBATTLE")) {
				type = "MBATTLE";
			} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("QUESTION")) {
				type ="QUESTION";
			} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("NOTE")) {
				type = "NOTE";
			} else {
				if(alarm.getAlarmType().equals("경고")) {
					type = "WARN";
					warnCnt = userRepository.findByUsername(alarm.getUsername()).getUserWarnCnt();
				} else {
					type = "BAN";
					date = userRepository.findByUsername(alarm.getUsername()).getBanDate();
				}
			}
			
			//detailNo정할떄		
			if(alarm.getAlarmTargetFrom().toUpperCase().contains("COMMENT")) {
				if(alarm.getAlarmTargetFrom().toUpperCase().contains("MBTMI")) {
					Optional<MbtmiComment> tmiComm = mbtmiCommentRepository.findById(alarm.getAlarmTargetNo());
					if(tmiComm.isPresent()) {
						no = tmiComm.get().getMbtmiNo();
					} else {
						throw new Exception("알림에 대한 해당 댓글이 존재하지 않음");
					}
				} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("MBTWHY")) {
					Optional<MbtwhyComment> whyComm = mbtwhyCommentRepository.findById(alarm.getAlarmTargetNo());
					if(whyComm.isPresent()) {
						no = whyComm.get().getMbtwhyNo();
					} else {
						throw new Exception("알림에 대한 해당 댓글이 존재하지 않음");
					}
				} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("MBATTLE")) {
					Optional<MbattleComment> battleComm = mbattleCommentRepository.findById(alarm.getAlarmTargetNo());
					if(battleComm.isPresent()) {
						no = battleComm.get().getMbattleNo();
					} else {
						throw new Exception("알림에 대한 해당 댓글이 존재하지 않음");
					}
				}
			} else {
				no = alarm.getAlarmTargetNo();
			}
			if(type.equals("MBTWHY")) {
				Optional<Mbtwhy> optionalWhy = mbtwhyRepository.findById(no);
				mbti = optionalWhy.get().getMbtiCategory();
			}else {
				mbti = "";
			}
			
		alarmDtoList.add(AlarmDto.builder()
				.alarmNo(alarm.getAlarmNo())
				.username(alarm.getUsername())
				.alarmType(alarm.getAlarmType())
				.detailType(type)
				.detailNo(no)
				.detailMbti(mbti)
				.alarmContent("["+myContent+"] 에 "+alarm.getAlarmType()+"(이)/가 도착했습니다. ( "+alarm.getAlarmCnt()+" )")
				.alarmTargetNo(alarm.getAlarmTargetNo())
				.alarmTargetFrom(alarm.getAlarmTargetFrom())
				.alarmIsRead(alarm.getAlarmIsRead())
				.alarmReadDate(alarm.getAlarmReadDate())
				.alarmUpdateDate(alarm.getAlarmUpdateDate())
				.alarmCnt(alarm.getAlarmCnt())
				.warnCnt(warnCnt)
				.banDate(date)
				.build()
				);
		
		}
		return alarmDtoList;
	}


	@Override
	public List<AlarmDto> getAlarmListByUserAndTypeAndPaging(String username, String type, PageInfo pageInfo)
			throws Exception {
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		List<Alarm> alarmList = new ArrayList<Alarm>();
		String sendType;
		if(type == null || type == "" || type == "null") {
			sendType = null;
		}else {
			sendType = type;
		}
		Long allCnt = alarmDslRepository.findAlarmCntByUserAndType(username, type);
		Long allCount = allCnt;
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		//필터로 적용한 페이지의 내용이 현재 페이지보다 낮을떄 현재 페이지를 1페이지로 세팅해줌
		if(allPage < pageInfo.getCurPage()) {
			pageInfo.setCurPage(1);
		}
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		System.out.println(allCnt);
		
		alarmList = alarmDslRepository.findAlarmListByUserAndTypeAndPaging(username, sendType, pageRequest);
		List<AlarmDto> alarmDtoList =  alarmToAlarmDto(alarmList);
		if(alarmDtoList.isEmpty()) {
			throw new Exception("해당 알림없음.");
		}else {
			return alarmDtoList;
		}
	}
	


	@Override
	public void updateAlarmRead(List<Integer> noList) throws Exception {
		for(Integer no : noList) {
			Optional<Alarm> optionalAlarm = alarmRepository.findById(no);
			if(optionalAlarm.isPresent()) {
				Alarm alarm = optionalAlarm.get();
				alarm.setAlarmIsRead("Y");
				alarm.setAlarmReadDate(new Timestamp(new Date().getTime()));
				if(alarm.getAlarmType().equals("댓글")) {
					alarm.setAlarmCnt(0);
				}
				alarmRepository.save(alarm);
			}else {
				throw new Exception("해당 번호 알람 없음");
			}
		}		
	}


	@Override
	public void updateAlarmReadAll(String username) throws Exception {
		List<Alarm> alarmList = alarmRepository.findByUsername(username);
		if(alarmList.isEmpty()) {
			throw new Exception("해당 유저에 대한 알람없음.");
		} else {
			for(Alarm alarm : alarmList) {
				alarm.setAlarmIsRead("Y");
				alarm.setAlarmReadDate(new Timestamp(new Date().getTime()));
				if(alarm.getAlarmType().equals("댓글")) {
					alarm.setAlarmCnt(0);
				}
				alarmRepository.save(alarm);
			}
		}
	}

	
	// 댓글작성시, 문의답글시, 회원제재시 알림 데이터 삽입
	@Override
	public void addAlarm(Alarm alarm) throws Exception {
		alarmRepository.save(alarm);
	}

	// 기존 알림데이터 조회
	@Override
	public Alarm selectAlarmByAlarmTargetNoAndAlarmTargetFrom(Integer alarmTargetNo, String alarmTargetFrom) throws Exception {
		Optional<Alarm> oalarm = alarmRepository.findByAlarmTargetNoAndAlarmTargetFrom(alarmTargetNo, alarmTargetFrom);
		if(oalarm.isEmpty()) return null; // 기존 알림 데이터가 없으면 null반환
		return oalarm.get();
	}


	@Override
	public List<AlarmDto> getAlarmListByAlarmIsNotReadBy5(String username) throws Exception {
		List<Alarm> alarmList = alarmDslRepository.findAlarmListNotReadByUsername(username);
		return alarmToAlarmDto(alarmList);
	}


	@Override
	public Long getCntNotReadAlarmList(String username) throws Exception {
		Long cnt = alarmDslRepository.findCntAlarmNotReadByUsername(username);
		return cnt;
	}

	//알람 읽음여부와 시간
	@Override
	public void updateAlarmRead(Integer no) throws Exception {
		Optional<Alarm> optionalAlarm = alarmRepository.findById(no);
		if(optionalAlarm.isEmpty()) {
			throw new Exception("해당 알림 없음");
		} else { 
			optionalAlarm.get().setAlarmIsRead("Y");
			optionalAlarm.get().setAlarmReadDate(new Timestamp(new Date().getTime()));
			if(optionalAlarm.get().getAlarmType().equals("댓글")) {
				optionalAlarm.get().setAlarmCnt(0);
			}
			if(optionalAlarm.get().getAlarmTargetFrom().toUpperCase().equals("NOTE")) {
				Optional<Note> note = noteRepository.findById(optionalAlarm.get().getAlarmTargetNo());
				note.get().setNoteIsRead("Y");
				noteRepository.save(note.get());
			}
			alarmRepository.save(optionalAlarm.get());
		}
	}
	
	
	
	

}
