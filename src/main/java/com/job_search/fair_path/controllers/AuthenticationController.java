package com.job_search.fair_path.controllers;

import com.job_search.fair_path.dataTransferObject.AuthUserDTO;
import com.job_search.fair_path.dataTransferObject.LoginResponseDTO;
import com.job_search.fair_path.dataTransferObject.RegisterUserDTO;
import com.job_search.fair_path.dataTransferObject.VerifyUserDTO;
import com.job_search.fair_path.dataTransferObject.ForgotPasswordDTO;
import com.job_search.fair_path.dataTransferObject.UpdatePasswordDTO;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.services.AuthenticationService;
import com.job_search.fair_path.services.JwtService;
import com.job_search.fair_path.services.UserService;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*

    creation of route /auth/signup and /auth/login for user authentication
    For both user authentication and user registration respectively
 */
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
            UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    // Expose mapping for signing up to create accounts

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        try {
            authenticationService.signUp(registerUserDTO);
            System.out.println("Generated JWT Token");
            return ResponseEntity.ok("Success");
        } catch (DuplicateKeyException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // or HttpStatus.CONFLICT
                    .body(Map.of(
                            "code", "DUPLICATE_USER",
                            "message", ex.getMessage()));
        }
    }

    // Post mapping for login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody AuthUserDTO authUserDTO) {
        User authenticateUser = authenticationService.authenticate(authUserDTO);
        String jwtToken = jwtService.generateToken(authenticateUser);
        LoginResponseDTO loginResponse = new LoginResponseDTO(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    // Verify User by entering verification code
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO) {
        try {
            authenticationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    // Resend email, resending verification code
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody AuthUserDTO input) {
        try {
            authenticationService.resendVerificationCode(input);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendUpdatePasswordEmail(@RequestBody ForgotPasswordDTO input) {
        try {
            authenticationService.sendForgotPasswordEmail(input);
            return ResponseEntity.ok("Reset password email sent, please check your mailbox.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO input) {
        try {
            authenticationService.updatePassword(input);
            return ResponseEntity.ok("Successfully updated password");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
