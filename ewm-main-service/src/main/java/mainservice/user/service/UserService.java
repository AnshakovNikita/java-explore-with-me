package mainservice.user.service;

import mainservice.user.dto.UserDto;
import mainservice.user.model.User;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    User getUser(Long userId);

    List<UserDto> getUsers(List<Long> ids);

    void deleteUser(Long userId);
}