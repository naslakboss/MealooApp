package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class MealooUserProvider {

    MealooUserRepository userRepository;

    public MealooUserProvider(MealooUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<MealooUser> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public MealooUser getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User " + username + " does not exist in database"));
    }

    public MealooUser createUser(MealooUser user){
        return userRepository.save(user);
    }

    public MealooUser updateUser(MealooUser user){
        return userRepository.save(user);
    }

    public void deleteUserByUsername(String username){
        userRepository.deleteByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
}
