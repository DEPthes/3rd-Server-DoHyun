package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save (Member member); //회원이 저장소에 저장됨.
    Optional<Member> findById(Long id); //아이디를 가지고 저장소에서 정보를 찾아옴.
    Optional<Member> findByName(String name); //이름을 가지고 저장소에서 정보를 찾아옴.
    List<Member> findAll(); //지금까지 저장된 모든 회원 리스트의 반환.
}
