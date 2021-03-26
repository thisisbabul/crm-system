package com.sohanf.crmsystem.repository;

import com.sohanf.crmsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
