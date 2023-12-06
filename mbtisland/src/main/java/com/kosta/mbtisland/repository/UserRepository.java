package com.kosta.mbtisland.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	UserEntity findByUsername(String username);
	Optional<UserEntity> findByProviderAndProviderId(String provider, String providerId);	
}
