package com.famillink.controller;

import com.famillink.annotation.ValidationGroups;
import com.famillink.model.domain.user.Account;
import com.famillink.model.domain.user.Member;
import com.famillink.model.service.MemberService;
import com.famillink.model.service.MemberServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api("User Controller")
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberServiceImpl memberservice;

    //Account account, Member member,String photo
    @ApiOperation(value = "회원가입", notes = "req_data : [id, pw, email, name, nickname]")
    @PostMapping("/signup/{photo}")
    public ResponseEntity<?> signup(@RequestBody Member member,@PathVariable String  photo) throws Exception {

        Member savedUser = memberservice.signup(member,photo);

        return new ResponseEntity<Object>(new HashMap<String, Object>() {{
            put("result", true);
            put("msg", "회원가입을 성공하였습니다.\n이메일을 확인해주세요.\n30분 이내 인증을 완료하셔야합니다.");
        }}, HttpStatus.OK);
    }



    @ApiOperation(value = "로그인", notes = "req_data : [id, pw]")
    @PostMapping("/login/{photo}")

    public ResponseEntity<?> login(@RequestBody Member member,@PathVariable  String photo) throws Exception {

        Map<String, Object> token = memberservice.login(member,photo);

        return new ResponseEntity<Object>(new HashMap<String, Object>() {{
            put("result", true);
            put("msg", "로그인을 성공하였습니다.");
            put("access-token", token.get("access-token"));
            put("refresh-token", token.get("refresh-token"));
            put("uid", token.get("uid"));
            put("name", token.get("name"));

        }}, HttpStatus.OK);
    }


    @ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Long uid, HttpServletRequest request) throws Exception {
        HttpStatus status = HttpStatus.ACCEPTED;
        String token = request.getHeader("refresh-token");
        String result = memberservice.refreshToken(uid, token);
        if (result != null && !result.equals("")) {
            // 발급 성공
            return new ResponseEntity<Object>(new HashMap<String, Object>() {{
                put("result", true);
                put("msg", "토큰이 발급되었습니다.");
                put("access-token", result);
            }}, status);
        } else {
            // 발급 실패
            throw new RuntimeException("리프레시 토큰 발급에 실패하였습니다.");
        }
    }




    @ApiOperation(value = "회원 확인", notes = "회원정보를 반환합니다.")
    @GetMapping("/auth")
    public ResponseEntity<?> authUser(final Authentication authentication) {
        Member auth = (Member) authentication.getPrincipal();
        return new ResponseEntity<Object>(new HashMap<String, Object>() {{
            put("result", true);
            put("data", auth);
        }}, HttpStatus.OK);
    }




}
