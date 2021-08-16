package com.example.shirodemo;

import com.example.shirodemo.dao.RoleRepository;
import com.example.shirodemo.dao.UserRepository;
import com.example.shirodemo.entity.Role;
import com.example.shirodemo.entity.User;
import com.example.shirodemo.util.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ShiroDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            User adminUser = new User();
            adminUser.setUsername("root");
            adminUser.setSalt(SaltUtil.getSalt());
            adminUser.setPassword(new Md5Hash("root", adminUser.getSalt(), 1024).toHex());

            Role adminRole = new Role();
            adminRole.setName("admin");
            Role userRole = new Role();
            userRole.setName("user");
            adminRole.getUsers().add(adminUser);
            userRole.getUsers().add(adminUser);

            List<Role> roles = adminUser.getRoles();
            roles.add(adminRole);
            roles.add(userRole);

            userRepository.save(adminUser);
        };
    }

}
