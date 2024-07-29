package com.example.demo.services;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateUser(User user) {
        boolean duplicateExists = false;
        User updateUser = repository.getById(user.getId());
        String email = user.getEmail();
        if (repository.getByEmail(email) != null) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        repository.update(user);
    }

    @Override
    public UserDetails getUserById(int id) {
        return new CustomUserDetails(repository.getById(id));
    }

    @Override
    public UserDetails getUserByEmail(String email) {
        return new CustomUserDetails(repository.getByEmail(email));
    }

//    @Override
//    public List<User> getUserByKeyword(String keyword) {
//        return repository.search(keyword);
//    }

    @Override
    public void changePassword(int id, String newPassword) {
        User user = repository.getById(id);
        user.setPassword(newPassword);
        repository.update(user);
    }

    @Override
    public void blockUser(int id) {
        User user = repository.getById(id);
        user.setBlocked(true);
        repository.update(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email);
        return new CustomUserDetails(user);
    }

}
