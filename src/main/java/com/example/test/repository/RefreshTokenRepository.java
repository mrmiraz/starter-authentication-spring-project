package com.example.test.repository;

import com.example.test.domain.entity.RefreshToken;
import com.example.test.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByClientInstanceId(String clientInstanceId);
    Optional<RefreshToken> findByTokenAndClientInstanceId(String token, String clientInstanceId);
    Optional<RefreshToken> findByUser(User user);
    void deleteByToken(String token);
    void deleteRefreshTokenByTokenAndClientInstanceId(String token, String clientInstanceId);
}
