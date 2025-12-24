package com.job_search.fair_path.controllers;

import com.job_search.fair_path.dataTransferObject.LoginUserDTO;
import com.job_search.fair_path.dataTransferObject.RegisterUserDTO;
import com.job_search.fair_path.dataTransferObject.VerifyUserDTO;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.responses.LoginResponse;
import com.job_search.fair_path.services.AuthenticationService;
import com.job_search.fair_path.services.JwtService;
import com.job_search.fair_path.services.UserService;
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

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService){
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    // Expose mapping for signing up to create accounts

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDTO){
        User registeredUser = authenticationService.signUp(registerUserDTO);
        return ResponseEntity.ok(registeredUser);
    }

    // Post mapping for login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        User authenticateUser = authenticationService.authenticate(loginUserDTO);
        String jwtToken = jwtService.generateToken(authenticateUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    // Verify User by entering verification code
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO){
        try{
            authenticationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("Account verified successfully");
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    // Resend email, resending verification code
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody String email){
        try{
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }


}
