package hello.capstone.service;

import org.springframework.stereotype.Service;

import hello.capstone.dto.Member;
import hello.capstone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	
	public int getMeberIdx(Member member) {
		int idx = memberRepository.getMeberIdx(member);
		return idx;
	}
}