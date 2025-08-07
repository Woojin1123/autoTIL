package com.woojin.autotil.til.repository;

import com.woojin.autotil.til.entity.Til;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TilRepository extends JpaRepository<Til,Long> {
}
