package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public List<UserShort> getUsersByEmail(@PathVariable("email") String email) {
        return userService.getUsersByEmail(email);
    }

    @GetMapping("/ip/{email}")
    public List<UserShortWithIP> getUsersByEmailWithIP(@PathVariable("email") String email) {
        return userService.getUsersByEmailWithIp(email);
    }

    @PostMapping
    public UserDto saveNewUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }
}