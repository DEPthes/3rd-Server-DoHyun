package org.example.service;

import org.example.domain.DTO.MemberDto;
import org.example.domain.entity.Member;

public interface MemberService {
    Member saveEntity(Member member);

    Member saveDTO(MemberDto memberDto);

    Member findByUsername(String username);
}
