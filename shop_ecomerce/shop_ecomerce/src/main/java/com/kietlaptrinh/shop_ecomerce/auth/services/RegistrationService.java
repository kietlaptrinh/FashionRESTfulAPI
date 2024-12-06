package com.kietlaptrinh.shop_ecomerce.auth.services;
import com.kietlaptrinh.shop_ecomerce.auth.dto.RegistrationRequest;
import com.kietlaptrinh.shop_ecomerce.auth.dto.RegistrationResponse;
import com.kietlaptrinh.shop_ecomerce.auth.entities.User;
import com.kietlaptrinh.shop_ecomerce.auth.help.VerificationCodeGenerator;
import com.kietlaptrinh.shop_ecomerce.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;
@Service
public class RegistrationService {
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public RegistrationResponse createUser(RegistrationRequest request) {
        // Check if a user with the same email already exists
        User existing = userDetailRepository.findByEmail(request.getEmail());

        if(null != existing){
            return  RegistrationResponse.builder()
                    .code(400)
                    .message("Email already exist!")
                    .build();
        }
        try{
            // Create a new user entity
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setProvider("manual");

            String code = VerificationCodeGenerator.generateCode();
            user.setVerificationCode(code);
            user.setAuthorities(authorityService.getUserAuthority());
            userDetailRepository.save(user);
            // Send a verification email to the user
            emailService.sendMail(user);

            return RegistrationResponse.builder()
                    .code(200)
                    .message("User created!")
                    .build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServerErrorException(e.getMessage(),e.getCause());
        }
    }
    public void verifyUser(String userName) {
        User user= userDetailRepository.findByEmail(userName);
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
