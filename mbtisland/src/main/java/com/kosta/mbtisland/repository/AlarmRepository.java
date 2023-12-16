package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
	List<Alarm> findByUsername(String username) throws Exception;
}
