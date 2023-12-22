package com.kosta.mbtisland.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Mbtmi;

public interface MbtmiRepository extends JpaRepository<Mbtmi, Integer> {
	Integer countByWriterId(String username);
	// 주의: 항상 where절에 mbtmi.isBlocked.eq("N")를 포함해야하기 때문에 MbtmiRepository의 countBy명명규칙을 통해 자동생성되는 쿼리를 이용하여 게시글수를 조회하지 않도록 한다
	
	
//	Long countByCategory(String category);
//	Long countByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String searchTerm1, String searchTerm2); // 매개변수가 두개가 아니라면 선언규칙 위배로 디펜던시 오류 발생
	
}
