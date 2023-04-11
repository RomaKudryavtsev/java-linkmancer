package ru.practicum.user;

import org.springframework.context.annotation.Lazy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepositoryCustom {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserShortWithIP> findAllByEmailContainingIgnoreCaseWithIP(String emailSearch) {
        return userRepository.findAllByEmailContainingIgnoreCase(emailSearch).stream()
                .map(userShort -> new UserShortWithIP(userShort, getServerIP(getEmailServer(userShort.getEmail()))))
                .collect(Collectors.toList());
    }

    private String getServerIP(String emailServer) {
        try {
            return InetAddress.getByName(emailServer).toString();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    private String getEmailServer(String email) {
        String[] emailParts = email.split("\\@");//никогда так не делайте :-)
        if (emailParts.length != 2 || emailParts[1].isEmpty()) {
            throw new IllegalArgumentException("Incorrect domain");
        }
        return emailParts[1];
    }
}
