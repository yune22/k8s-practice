package com.beyond.ordersystem.member.controller;

import com.beyond.ordersystem.member.dtos.*;
import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.service.MemberService;
import com.beyond.ordersystem.common.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MemberCreateDto dto){
        Long id = memberService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/list")
    public List<MemberResDto> findAll(){
        List<MemberResDto> dtoList = memberService.findAll();
        return dtoList;
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        MemberResDto dto = memberService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/myinfo")
//    X-로 시작하는 헤더명은 개발자가 인위적으로 만든 Header인 경우에 관례적으로 사용
    public ResponseEntity<?> myinfo(@RequestHeader("X-User-Email")String email){
        MemberResDto dto = memberService.myinfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @PostMapping("/doLogin")
    public ResponseEntity<?> login(@RequestBody MemberLoginReqDto dto){
        Member member = memberService.login(dto);
        String accessToken = jwtTokenProvider.createToken(member);
//        refresh생성 및 저장
        String refreshToken = jwtTokenProvider.createRtToken(member);
        MemberLoginResDto memberLoginResDto = MemberLoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(memberLoginResDto);
    }

    @PostMapping("/refresh-at")
    public ResponseEntity<?> refreshAt(@RequestBody RefreshTokenDto dto){
//        rt검증(1.토큰자체검증 2.redis조회검증)
        Member member = jwtTokenProvider.validateRt(dto.getRefreshToken());

//        at신규생성
        String accessToken = jwtTokenProvider.createToken(member);
//        refresh생성 및 저장
        String refreshToken = jwtTokenProvider.createRtToken(member);
        MemberLoginResDto memberLoginResDto = MemberLoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(memberLoginResDto);
    }
}