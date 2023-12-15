package com.kosta.mbtisland.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

	@Override
	public List<Alarm> getAlarmListByUserAndTypeAndPaging(String username, String type, PageInfo pageInfo)
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
		if(alarmList.isEmpty()) {
			throw new Exception("해당 알림없음.");
		}else {
			return alarmList;
		}
	}

}
