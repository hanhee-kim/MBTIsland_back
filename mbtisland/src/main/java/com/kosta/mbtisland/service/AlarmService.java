package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;

public interface AlarmService {

	List<Alarm> getAlarmListByUserAndTypeAndPaging(String username,String type,PageInfo pageInfo) throws Exception;
}
