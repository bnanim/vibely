package com.app.vibely.repositories;

import com.app.vibely.entities.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLikeRepository extends JpaRepository<EventLike, Integer> {
}