package mainservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.exceptions.ValidationException;
import mainservice.user.dto.UserDto;
import mainservice.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@Slf4j
@Validated
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids) {
        List<UserDto> dtos = userService.getUsers(ids);
        log.info("getUsers: " + ids);
        return dtos;
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException(String.format("%s - некорректное имя пользователя.", userDto.getName()));
        }
        UserDto dto = userService.saveUser(userDto);
        log.info("save User: " + userDto);
        return dto;
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("delete User id: " + userId);

    }
}
