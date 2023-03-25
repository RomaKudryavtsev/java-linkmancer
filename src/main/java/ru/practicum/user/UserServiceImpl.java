package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<UserShort> getUsersByEmail(String email) {
        return repository.findAllByEmailContainingIgnoreCase(email);
    }

    @Override
    public List<UserShortWithIP> getUsersByEmailWithIp(String email) {
        return repository.findAllByEmailContainingIgnoreCaseWithIP(email);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = repository.save(UserMapper.mapToNewUser(userDto));
        return UserMapper.mapToUserDto(user);
    }
}