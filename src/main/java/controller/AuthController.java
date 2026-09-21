package controller;

import dto.ApiResponse;
import dto.RegisterRequest;
import entity.User;
import service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterRequest request) {
        User registeredUser = userService.registerCustomer(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Đăng ký thành công!", registeredUser));
    }
}