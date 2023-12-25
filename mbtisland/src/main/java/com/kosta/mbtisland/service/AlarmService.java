package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.AlarmDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;

public interface AlarmService {

	List<AlarmDto> getAlarmListByUserAndTypeAndPaging(String username,String type,PageInfo pageInfo) throws Exception;
	void updateAlarmRead(List<Integer> noList) throws Exception;
	void updateAlarmReadAll(String username) throws Exception;
	
	// 댓글작성시, 문의답글시, 회원제재시 알림 데이터 삽입
	void addAlarm(Alarm alarm) throws Exception;
	// 기존 알림데이터 조회
	Alarm selectAlarmByAlarmTargetNoAndAlarmTargetFrom(Integer alarmTargetNo, String alarmTargetFrom) throws Exception;
	List<AlarmDto> getAlarmListByAlarmIsNotReadBy5(String username) throws Exception;
	Long getCntNotReadAlarmList(String username) throws Exception;
	
}
