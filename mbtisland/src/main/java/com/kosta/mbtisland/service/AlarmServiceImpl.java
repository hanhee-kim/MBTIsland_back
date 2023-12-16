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
import com.kosta.mbtisland.repository.AlarmDslRepository;
import com.kosta.mbtisland.repository.AlarmRepository;
@Service
public class AlarmServiceImpl implements AlarmService{
	
	@Autowired
	private AlarmRepository alarmRepository;
	@Autowired
	private AlarmDslRepository alarmDslRepository;
	
	public List<AlarmDto> alarmToAlarmDto(List<Alarm> alarmList){
//		private Integer alarmNo;
//		private String username;
//		private String alarmType;
//		private String alarmContent;
//		private Integer alarmTargetNo;
//		private String alarmTargetFrom;
//		private String alarmIsRead;
//		private Timestamp alarmReadDate;
//		private Timestamp alarmUpdateDate;
		List<AlarmDto> alarmDtoList = new ArrayList<AlarmDto>();
		for(Alarm alarm : alarmList) {
			String myContent = null;
			if(alarm.getAlarmTargetFrom().toUpperCase().contains("COMMENT")) {
				myContent = "내 댓글";
			} else if(alarm.getAlarmTargetFrom().toUpperCase().contains("NOTE")) {
				myContent = "내 쪽지";
			} else {
				myContent = "내 게시글";
			}
		alarmDtoList.add(AlarmDto.builder()
				.alarmNo(alarm.getAlarmNo())
				.username(alarm.getUsername())
				.alarmType(alarm.getAlarmType())
				.alarmContent("["+myContent+"] 에 "+alarm.getAlarmType()+"(이)/가 도착했습니다.")
				.alarmTargetNo(alarm.getAlarmTargetNo())
				.alarmTargetFrom(alarm.getAlarmTargetFrom())
				.alarmIsRead(alarm.getAlarmIsRead())
				.alarmReadDate(alarm.getAlarmReadDate())
				.alarmUpdateDate(alarm.getAlarmUpdateDate())
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
				alarmRepository.save(alarm);
			}
		}
	}
	
	
	
	

}
