package com.example.Assistant.repository;

import com.example.Assistant.entity.DemoRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository<Entity, IdType> extend karte hi save(), findById(),
 * findAll(), deleteById() jaise methods free me mil jaate hain -
 * inka implementation Spring runtime pe generate karta hai.
 */
public interface DemoRecordRepository extends JpaRepository<DemoRecord, Long> {
}