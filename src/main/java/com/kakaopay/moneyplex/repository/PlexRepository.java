package com.kakaopay.moneyplex.repository;

import com.kakaopay.moneyplex.entity.Plex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PlexRepository extends JpaRepository<Plex, Long> {
    Optional<Plex> findByTokenAndRoomIdAndCreatedTimeAfter(String token, String roomId, LocalDateTime limitTime);
    Optional<Plex> findByTokenAndRoomIdAndOwnerIdAndCreatedTimeAfter(String token, String roomId, Long ownerId, LocalDateTime limitTime);
    int countByTokenAndOwnerIdAndRoomIdAndCreatedTimeAfter(String token, Long ownerId, String roomId, LocalDateTime limitTime);
}
