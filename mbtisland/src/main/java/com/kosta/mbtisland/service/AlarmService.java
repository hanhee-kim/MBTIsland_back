package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.AlarmDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;

public interface AlarmService {

	List<AlarmDto> getAlarmListByUserAndTypeAndPaging(String username,String type,PageInfo pageInfo) throws Exception;
	void updateAlarmRead(List<Integer> noList) throws Exception;
	void updateAlarmReadAll(String username) throws Exception;
}
