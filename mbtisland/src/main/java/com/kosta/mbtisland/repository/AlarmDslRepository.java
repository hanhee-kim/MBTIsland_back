package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.QAlarm;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class AlarmDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryfactory;
	
	public List<Alarm> findAlarmListByUserAndTypeAndPaging(String username,String type,PageRequest pageRequest){
		System.out.println("알람 DSL 진입");
		QAlarm alarm = QAlarm.alarm;
		List<Alarm> alarmList = jpaQueryfactory
				.selectFrom(alarm)
				.where(
						alarm.username.eq(username).and(
						type!=null?alarm.alarmType.eq(type):null)
						)
				.orderBy(alarm.alarmIsRead.asc())
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();
		return alarmList;
	}
	
	
	public Long findAlarmCntByUserAndType(String username, String type) {
		QAlarm alarm = QAlarm.alarm;
		Long cnt = jpaQueryfactory
				.select(alarm.count())
				.from(alarm)
				.where(
						alarm.username.eq(username).and(
						type!=null?alarm.alarmType.eq(type):null)
						)
				.fetchOne();
		return cnt;
	}
	
	//5개씩만 가져옴
	public List<Alarm> findAlarmListNotReadByUsername(String username){
		QAlarm alarm = QAlarm.alarm;
		List<Alarm> alarmList = jpaQueryfactory
				.selectFrom(alarm)
				.where(alarm.username.eq(username).and(alarm.alarmIsRead.eq("N")))
				.orderBy(alarm.alarmUpdateDate.desc())
				.limit(5)
				.fetch();
		return alarmList;
	}
	
	public Long findCntAlarmNotReadByUsername(String username) {
		QAlarm alarm = QAlarm.alarm;
		Long cnt = jpaQueryfactory
				.select(alarm.count())
				.from(alarm)
				.where(alarm.username.eq(username).and(alarm.alarmIsRead.eq("N")))
				.fetchOne();
		return cnt;
	}
}
