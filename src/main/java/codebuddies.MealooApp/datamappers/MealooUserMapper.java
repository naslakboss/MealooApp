package codebuddies.MealooApp.datamappers;

import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.MealooUserRole;
import codebuddies.MealooApp.entities.user.Role;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.MealooUserRepository;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class MealooUserMapper {

    private ModelMapper modelMapper = new ModelMapper();

    MealooUserRepository userRepository;

    public MealooUserMapper(MealooUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<MealooUserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> modelMapper.map(user, MealooUserDTO.class));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public MealooUserDTO getUserByUsername(String username) {
        MealooUser user = userRepository.findByUsername(username).orElseThrow(() ->
                 new ResourceNotFoundException(username));

        return modelMapper.map(user, MealooUserDTO.class);
    }

    public MealooUserDTO getUserById(long id){
        MealooUser user = userRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("" + id));
        return modelMapper.map(user, MealooUserDTO.class);
    }

    public MealooUserDTO createUser(MealooUserDTO user) {
        MealooUser newUser = modelMapper.map(user, MealooUser.class);
        newUser.setRoles(Collections.singleton(new Role(MealooUserRole.ROLE_USER)));
        userRepository.save(newUser);

        return modelMapper.map(newUser, MealooUserDTO.class);
    }

    public MealooUserDTO updateUser(MealooUserDTO user) {
        MealooUser updatedUser = modelMapper.map(user, MealooUser.class);
        userRepository.save(updatedUser);

        return user;
    }

    public void deleteUserByUsername(String username) {
        if(!existsByUsername(username)){
            throw new ResourceNotFoundException(username);
        }
        userRepository.deleteByUsername(username);
    }

}
