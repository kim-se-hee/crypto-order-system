package ksh.example.mybit.persistence.mysql.jpa.repository;

import ksh.example.mybit.persistence.mysql.jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
