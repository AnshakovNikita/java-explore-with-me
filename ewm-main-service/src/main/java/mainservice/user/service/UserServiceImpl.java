package mainservice.user.service;

import mainservice.exceptions.ConflictException;
import mainservice.exceptions.ValidationException;
import mainservice.user.dto.UserDto;
import mainservice.user.mapper.UserMapper;
import mainservice.user.model.User;
import mainservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static mainservice.user.mapper.UserMapper.toUser;
import static mainservice.user.mapper.UserMapper.toUserDto;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException(String.format("%s - некорректное имя пользователя.", userDto.getName()));
        }

        try {
            return toUserDto(userRepository.save(toUser(userDto)));
        } catch (RuntimeException e) {
            throw new ConflictException(String.format("%s - такое имя уже существует.", userDto.getName()));
        }
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("%d - Пользователь  не найден", userId)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids) {
        return userRepository.findAllById(ids).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /*public void userValid(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ConflictException(String.format("%s - такое имя уже существует.", userId)));
    } */
}
