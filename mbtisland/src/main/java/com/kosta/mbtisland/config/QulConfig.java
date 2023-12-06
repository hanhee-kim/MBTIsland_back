package com.kosta.mbtisland.config;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

//처음뜰때 실행
@Configuration
public class QulConfig {
	//JPA가 가지고 있는 EntityManager를 사용
		@Autowired
		EntityManager entityManager;
		
		//따로 호출하지않아도 
		//빈객체를 생성해서 같이 쓰기위해
		@Bean
		public JPAQueryFactory jpaQueryFactory() {
			return new JPAQueryFactory(entityManager);
		}

}
