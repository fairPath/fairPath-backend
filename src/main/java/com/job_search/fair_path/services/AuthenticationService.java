package com.job_search.fair_path.services;

import com.job_search.fair_path.dataTransferObject.AuthUserDTO;
import com.job_search.fair_path.dataTransferObject.RegisterUserDTO;
import com.job_search.fair_path.dataTransferObject.VerifyUserDTO;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Value("${forgot.password.url}")
    private String forgotPasswordUrl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signUp(RegisterUserDTO input) {
        User user = new User(input.getFirstName(), input.getLastName(), input.getUsername(), input.getEmail(),
                passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    // Login Auth logic
    public User authenticate(AuthUserDTO input) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            // thrown if they try to login and they are not verified
            throw new RuntimeException("Account not verified. Please verify your account.");
        }

        return user;
    }

    public void verifyUser(VerifyUserDTO input) {
        Optional<User> userOptional = userRepository.findByEmail(input.getEmail());
        // checking to see if user is present in our User Table
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            // if the verification code user input is valid then we enable them
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                // saving user to our repo
                userRepository.save(user);
            } else {
                // if incorrect verification code is entered
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            // if user not present throw error
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(AuthUserDTO input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        System.out.println("Resend verification code requested for email: " + input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified.");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        // Message itself will be in html format
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public void sendForgotPasswordEmail(AuthUserDTO input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            String subject = "Reset Password";
            // Message itself will be in html format
            String htmlMessage = "<html>"
                    + "<body style=\"font-family: Arial, sans-serif;\">"
                    + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                    + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                    + "<p style=\"font-size: 16px;\">Please click on the link below to update your password:</p>"
                    + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                    + "<h3 style=\"color: #333;\">Forgot Password:</h3>"
                    + "<a style=\"font-size: 18px; font-weight: bold; color: #007bff;\" href='" + forgotPasswordUrl + "'>Click here to reset password" + "</a>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";
            try {
                emailService.sendVerificationEmail(input.getEmail(), subject, htmlMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("User not found");
        }

    }

    @Transactional
    public void updatePassword(AuthUserDTO input) {
        Optional<User> userOptional = userRepository.findByEmail(input.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(input.getPassword()));
            userRepository.save(user);
        }
    }

}
