package com.es.repository;

import org.springframework.stereotype.Repository;

import com.es.entity.ClientCredentials;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClientCredentialsRepository extends JpaRepository<ClientCredentials, Integer> {

	public List<ClientCredentials> findByUserId(Integer userId);

}
