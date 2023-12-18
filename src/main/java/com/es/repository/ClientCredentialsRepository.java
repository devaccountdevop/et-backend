package com.es.repository;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.es.entity.ClientCredentials;
import com.es.entity.ImportTask;

@Repository
public interface ClientCredentialsRepository extends JpaRepository<ClientCredentials, Integer>{

	//List<ClientCredentials> findAllByIdIn(List<Integer> ids);
	List<ClientCredentials> findAllByIdIn(Set<Integer> clientUserIds);
//	@Query(value = "select j from JiraCredentials j where j.userId = :userId")
	public List<ClientCredentials> findByUserId(int userId);
	ClientCredentials findByJiraUserName(String jiraUserName);
	List<ClientCredentials> findByJiraUserNameIn(Set<String> jiraUserNames);
//	List<ClientCredentials> findByClientInAndUserIdIn( List<Integer> userIds,List<String> tokens);
//	userJiraToken
//	findByClientInAndUserIdIn
//	List<ClientCredentials> findByClientInAndUserIdIn(Set<Integer> userIds, Set<String> userJiraToken);
	
	@Query("SELECT c FROM ClientCredentials c WHERE c.token IN :tokens AND c.userId IN :userIds")
    List<ClientCredentials> findByClientInAndUserIdIn(@Param("tokens") Set<String> tokens, @Param("userIds") Set<Integer> userIds);
}
