package taxi.share.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taxi.share.back.model.User;
import taxi.share.back.repository.UserRepository;
import taxi.share.back.util.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }

    public String login(String userId, String password) throws Exception {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("User not found"));
        if (passwordEncoder.matches(password, user.getUserPassword())) {
            return jwtUtil.generateToken(userId);
        } else {
            throw new Exception("Invalid credentials");
        }
    }
}