package edu.byui.apj.storefront.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Optional<UserDetails> maybeUserDetails) {
        return maybeUserDetails.map(userDetails ->
            ResponseEntity.ok(userDetails.getUsername())
        ).orElse(
            ResponseEntity.noContent().build()
        );
    }

}
