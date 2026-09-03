package com.heydrian.stories_live.repository.users_repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.heydrian.stories_live.models.users_models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    public Users findByUserEmail(String userEmail);
}
