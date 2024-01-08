package com.outing.friendship.services.impl;

import com.outing.auth.api.controller.UserController;
import com.outing.auth.api.dto.UserDto;
import com.outing.commons.api.exception.OutingException;
import com.outing.friendship.api.dto.DummyUserDto;
import com.outing.friendship.api.dto.FriendshipDto;
import com.outing.friendship.model.DummyUserModel;
import com.outing.friendship.model.FriendshipModel;
import com.outing.friendship.repository.DummyUserRepository;
import com.outing.friendship.repository.FriendshipRepository;
import com.outing.friendship.services.FriendshipCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipCheckImpl implements FriendshipCheckService {

    private final FriendshipRepository friendshipRepository;
    private final DummyUserRepository dummyUserRepository;



    @Autowired
    private UserController userController;

    @Autowired
    public FriendshipCheckImpl(FriendshipRepository friendshipRepository, DummyUserRepository dummyUserRepository) {
        this.friendshipRepository = friendshipRepository;
        this.dummyUserRepository = dummyUserRepository;

    }
    @Override
    public List<FriendshipDto> getAllFriendsByUserId(String userId) {
        List<FriendshipModel> friendships= friendshipRepository.findByInviterUserIdAndStatus(userId, "accepted");
        List<FriendshipModel> friendships2 = friendshipRepository.findByInviterUserIdAndStatus(userId,  null);
        List<FriendshipModel> friendships1 = friendshipRepository.findByInviteeUserIdAndStatus(userId, "accepted");
        friendships.addAll(friendships1);
        friendships.addAll(friendships2);
        System.out.println(friendships+"friendships");

        List<FriendshipDto> friendshipDtos = new ArrayList<>();

        for(FriendshipModel friendship : friendships){


            FriendshipDto friendshipDTO = convertToDTO(friendship);
            friendshipDtos.add(friendshipDTO);
        }

        return friendshipDtos;
    }

    @Override
    public DummyUserDto getDummyUserById(String id) {
        if(id==null){
            throw new OutingException("Dummy user id cannot be null", HttpStatus.BAD_REQUEST);
        }
        Optional<DummyUserModel> dummyUserModel = this.dummyUserRepository.findById(id);
        List<DummyUserModel> listOfDummyUser = new ArrayList<>();
        dummyUserModel.ifPresent(listOfDummyUser::add);
        DummyUserModel dummyUserModel1 = listOfDummyUser.get(0);
        DummyUserDto dummyUserDto = new DummyUserDto(dummyUserModel1.getId(),dummyUserModel1.getName());
        return dummyUserDto;
    }

    private FriendshipDto convertToDTO(FriendshipModel friendship) {

        UserDto inviterUser = userController.getUserByUserId(friendship.getInviterUserId()).getBody();
        System.out.println(inviterUser+"inviterUser");
        UserDto inviteeUser = friendship.getInviteeUserId() != null ? userController.getUserByUserId(friendship.getInviteeUserId()).getBody() : null;
        DummyUserModel dummyUserModel = friendship.getDummyUserId() != null ? dummyUserRepository.findById(friendship.getDummyUserId()).orElse(null) : null;
        DummyUserDto dummyUserDto = null;
        if(dummyUserModel != null) {
            dummyUserDto = new DummyUserDto(dummyUserModel.getId(), dummyUserModel.getName());
        }

        return new FriendshipDto(friendship.getId(), inviterUser, inviteeUser, dummyUserDto, friendship.getStatus());
    }
}
