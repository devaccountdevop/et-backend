package com.es.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.SprintListPage;

@Repository
public interface SprintListPageRepository extends JpaRepository<SprintListPage, Integer> {

}