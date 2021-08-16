package com.example.shirodemo.dao;

import com.example.shirodemo.entity.Permit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository extends JpaRepository<Permit, Integer> {
}