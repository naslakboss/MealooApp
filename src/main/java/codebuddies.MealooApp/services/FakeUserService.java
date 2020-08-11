package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.repositories.FakeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FakeUserService {

    @Autowired
    FakeUserRepository fakeUserRepository;

    public List<FakeUser> findAll() {
        return fakeUserRepository.findAll();
    }

    public FakeUser save(FakeUser user1) {
        return fakeUserRepository.save(user1);
    }

    public FakeUser findByUsername(String username) {
        return fakeUserRepository.findByUsername(username);
    }
}
