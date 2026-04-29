package com.myfavor.controller;

import com.myfavor.dto.ApiResponse;
import com.myfavor.dto.JwtResponse;
import com.myfavor.dto.LoginRequest;
import com.myfavor.dto.RegisterRequest;
import com.myfavor.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(ApiResponse.success(jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("登录失败: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            return ResponseEntity.ok(ApiResponse.success("注册成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("注册失败: " + e.getMessage()));
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean exists = userService.findByUsername(username) != null;
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
