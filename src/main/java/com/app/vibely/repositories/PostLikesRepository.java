package com.app.vibely.repositories;

import com.app.vibely.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesRepository extends JpaRepository<Like , Integer> {
}
