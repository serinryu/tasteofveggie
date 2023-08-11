package com.serinryu.springproject.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {
    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminPage(Authentication authentication){
        System.out.println(authentication.getAuthorities());
        return ResponseEntity.ok("ADMIN PAGE");
    }

    /*
    @GetMapping("/api/admin/user")
    public ResponseEntity<UserResponseDTO> getUserInfo(){
    }
    */

}
