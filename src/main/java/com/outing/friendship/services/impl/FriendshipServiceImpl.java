package com.outing.friendship.services.impl;

import com.outing.commons.api.response.OutingResponse;
import com.outing.friendship.api.dto.DummyUserDto;
import com.outing.friendship.api.dto.FriendshipDto;
import com.outing.friendship.model.DummyUserModel;
import com.outing.friendship.model.FriendshipModel;
import com.outing.friendship.repository.DummyUserRepository;
//import com.outing.friendship.api.exception.FriendshipException;
import com.outing.commons.api.exception.OutingException;
import com.outing.friendship.repository.FriendshipRepository;
import com.outing.friendship.services.FriendshipService;
import com.outing.auth.api.controller.UserController;
import com.outing.auth.api.dto.UserDto;
//import com.outing.auth.api.Response;
//import com.outing.commons.api.response.Response;
import com.outing.auth.security.util.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final DummyUserRepository dummyUserRepository;



    @Autowired
    private UserController userController;
    @Autowired
    private PrincipalDetails principalDetails;

    @Autowired
    public FriendshipServiceImpl(FriendshipRepository friendshipRepository, DummyUserRepository dummyUserRepository) {
        this.friendshipRepository = friendshipRepository;
        this.dummyUserRepository = dummyUserRepository;

    }

    private FriendshipDto convertToDTO(FriendshipModel friendship) {
//        userRestController.getUserByUserId(friendship.getInviterUserId()).getBody()

        UserDto inviterUser = userController.getUserByUserId(friendship.getInviterUserId()).getBody();
        UserDto inviteeUser = friendship.getInviteeUserId() != null ? userController.getUserByUserId(friendship.getInviteeUserId()).getBody() : null;
        DummyUserModel dummyUserModel = friendship.getDummyUserId() != null ? dummyUserRepository.findById(friendship.getDummyUserId()).orElse(null) : null;
        DummyUserDto dummyUserDto = null;
        if(dummyUserModel != null) {
             dummyUserDto = new DummyUserDto(dummyUserModel.getId(), dummyUserModel.getName());
        }

        if (inviterUser == null) {
            throw new IllegalArgumentException("Invalid inviterUser ID found in friendship.");
        }

        return new FriendshipDto(friendship.getId(), inviterUser, inviteeUser, dummyUserDto, friendship.getStatus());
    }

    public FriendshipModel addTestData(FriendshipModel friendship) {
        return friendshipRepository.save(friendship);
    }

    public List<FriendshipDto> getAllFriendsByUserId() {

        String userId = this.principalDetails.getPrincipalDetails().getId();

//        System.out.println("inside friendlist " + userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        List<FriendshipModel> friendships= friendshipRepository.findByInviterUserIdAndStatus(userId, "accepted");
        List<FriendshipModel> friendships2 = friendshipRepository.findByInviterUserIdAndStatus(userId,  null);
//            System.out.println("====== > > > " + friendships2);
        List<FriendshipModel> friendships1 = friendshipRepository.findByInviteeUserIdAndStatus(userId, "accepted");
        friendships.addAll(friendships1);
        friendships.addAll(friendships2);


        List<FriendshipDto> friendshipDtos = new ArrayList<>();

         for(FriendshipModel friendship : friendships){


             FriendshipDto friendshipDTO = convertToDTO(friendship);
             friendshipDtos.add(friendshipDTO);
         }

         return friendshipDtos;
    }

    public List<FriendshipDto> sentRequests() {

        List<FriendshipModel> friendships= friendshipRepository.findByInviterUserIdAndStatus(this.principalDetails.getPrincipalDetails().getId(), "invited");
        List<FriendshipModel> friendships1 = friendshipRepository.findByInviterUserIdAndStatus(this.principalDetails.getPrincipalDetails().getId(), "rejected");
        List<FriendshipDto> friendshipDtos = new ArrayList<>();
          friendships.addAll(friendships1);
        for(FriendshipModel friendship : friendships){

            FriendshipDto friendshipDTO = convertToDTO(friendship);
            friendshipDtos.add(friendshipDTO);
        }

        return friendshipDtos;
    }

    public List<FriendshipDto> receivedRequests() {
        List<FriendshipModel> friendships= friendshipRepository.findByInviteeUserIdAndStatus(this.principalDetails.getPrincipalDetails().getId(), "invited");

        List<FriendshipDto> friendshipDtos = new ArrayList<>();

        for(FriendshipModel friendship : friendships){

            FriendshipDto friendshipDTO = convertToDTO(friendship);
            friendshipDtos.add(friendshipDTO);
        }

        return friendshipDtos;
    }

    public UserDto getUserByUsername(String input) {
        return userController.getUserByUserId(this.principalDetails.getPrincipalDetails().getId()).getBody();
    }

    public FriendshipModel acceptFriendInvitation(String friendshipId){

        try {
            System.out.println(friendshipId+ " "+this.principalDetails.getPrincipalDetails().getId()+" ");
            FriendshipModel friendship = friendshipRepository.findByIdAndInviteeUserIdAndStatus(friendshipId, this.principalDetails.getPrincipalDetails().getId(), "invited");
            if (friendship == null) {
                throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not in invited status.");
            }
            friendship.setStatus("accepted");
            return friendshipRepository.save(friendship);
        } catch (Exception e) {
            System.out.println("Inside accept");
            List<String> errors = new ArrayList<>();
            errors.add("Friendship already accepted");
            throw new OutingException(errors , HttpStatus.BAD_REQUEST);
//            throw new RuntimeException("Error accepting friend invitation: " + e.getMessage(), e);
        }
    }

    public void cancelInvitation(String friendshipId) {
        FriendshipModel friendship = friendshipRepository.findByIdAndInviterUserIdAndStatus(friendshipId,this.principalDetails.getPrincipalDetails().getId(), "invited");
        if (friendship != null) {
            friendshipRepository.delete(friendship);
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Invitation is already cancelled");
            throw new OutingException(errors , HttpStatus.BAD_REQUEST);
        }
    }

    public void rejectInvitation(String friendshipId) {
        FriendshipModel friendship = friendshipRepository.findByIdAndInviteeUserIdAndStatus(friendshipId, this.principalDetails.getPrincipalDetails().getId(), "invited");
        if (friendship != null) {
            friendship.setStatus("rejected");
            friendshipRepository.save(friendship);
        } else {
            throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not in invited status.");
        }
    }

    public void revokeFriendship(String friendshipId) {
        Optional<FriendshipModel> friendshipOptional = friendshipRepository.findById(friendshipId);
// TODO : NEED TO CHECK WHETHER FRIENDSHIP IS ACCEPTED THEN ONLY DO AHEAD
        if (friendshipOptional.isPresent()) {
            FriendshipModel friendship = friendshipOptional.get(); // Get the FriendshipModel from Optional
            if (this.principalDetails.getPrincipalDetails().getId().equals(friendship.getInviteeUserId())
                    || this.principalDetails.getPrincipalDetails().getId().equals(friendship.getInviterUserId())) {
                friendshipRepository.delete(friendship);
            } else {
                throw new RuntimeException("You are not authorized to revoke this friendship.");
            }
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Friendship already revoked");
            throw new OutingException(errors , HttpStatus.BAD_REQUEST);
        }
    }

    public FriendshipDto sendInvite(String name, String email){
        String currentUserID = this.principalDetails.getPrincipalDetails().getId();
        UserDto user = null;
        System.out.println(email + "email ");

        if(StringUtils.hasText(email)){
            System.out.println("hey we are inside this email");
            user = userController.getUserByEmail(email).getBody();
            if(user==null){
                throw new OutingException("Email Not Found",HttpStatus.NOT_FOUND);
            }
            if(StringUtils.hasText(name)){
                if(!user.getName().equals(name)){
                    List<String> errors = new ArrayList<>();
                    errors.add("Invalid name provided");
                    throw new OutingException(errors , HttpStatus.BAD_REQUEST);
                }
            }
        }



        if(user != null){
            System.out.println(user.getEmail()+" user email");
            if(user.getId().equals(currentUserID)){
                // user trying sending invitation to himself
                List<String> errors = new ArrayList<>();
                errors.add("You can't sent invitation to yourself");
                throw new OutingException(errors , HttpStatus.BAD_REQUEST);
            }

            FriendshipModel existingFriendship = friendshipRepository.findByInviteeUserIdAndInviterUserIdOrInviteeUserIdAndInviterUserId(
                    currentUserID, user.getId(), user.getId(), currentUserID
            );

            if (existingFriendship != null) {

                List<String> errors = new ArrayList<>();

                String status = existingFriendship.getStatus();
                if(status.equals("rejected")){
                    System.out.println("rejected friendship");
                    existingFriendship.setStatus("invited");
                    friendshipRepository.save(existingFriendship);
                    FriendshipDto result = convertToDTO(existingFriendship);
                    return result;

                }else if(status.equals("invited")){
                    System.out.println("You are already got invitation or sent invitation");
                    errors.add("You have already got invitation or sent invitation to this user");
                    throw new OutingException(errors , HttpStatus.BAD_REQUEST);
                }
                errors.add("Existing Friendship");
                throw new OutingException(errors , HttpStatus.BAD_REQUEST);
            }

            // User with given email is registered, send invite
            FriendshipModel friendshipModel = new FriendshipModel(currentUserID,user.getId(),"invited");
            friendshipRepository.save(friendshipModel);
            FriendshipDto result = convertToDTO(friendshipModel);

             return result;
        }else{

            String var = name.equals("")?email:name;
            DummyUserModel dummyUserModel = new DummyUserModel(var);
            dummyUserRepository.save(dummyUserModel);

            FriendshipModel friendshipModel = new FriendshipModel(currentUserID, dummyUserModel.getId());
            friendshipRepository.save(friendshipModel);
            FriendshipDto result = convertToDTO(friendshipModel);

            return result;
        }

    }


    public FriendshipDto getFriendshipById(String friendship_id) {
        String currentUserId = this.principalDetails.getPrincipalDetails().getId();
        List<String> errors = new ArrayList<>();

        Optional<FriendshipModel> friendshipOptional = friendshipRepository.findById(friendship_id);

        if (friendshipOptional.isPresent()) {
            FriendshipModel friendship = friendshipOptional.get();
            System.out.println("++++" + currentUserId);
            // Check if the current user is a part of this friendship
            if (currentUserId.equals(friendship.getInviterUserId()) || currentUserId.equals(friendship.getInviteeUserId())) {
                FriendshipDto friendshipDTO = convertToDTO(friendship);
                return friendshipDTO;
            } else {
//                throw new RuntimeException("You are not authorized to access this friendship.");
                errors.add("You are not authorized to access this friendship.");
                throw new OutingException(errors , HttpStatus.UNAUTHORIZED);
            }
        } else {
            errors.add("Friendship not found");
            throw new OutingException(errors , HttpStatus.BAD_REQUEST);
//            throw new NoSuchElementException("Friendship not found with ID: " + friendship_id);
        }
    }


    public void updateFriendDetails(String email, String friendshipId){
        List<String> errors = new ArrayList<>();
        if(StringUtils.hasText(email)){

             ResponseEntity<UserDto> user = userController.getUserByEmail(email);

             if(user == null){
                 errors.add("Invalid Email");
                 throw new OutingException(errors , HttpStatus.BAD_REQUEST);
             }

             UserDto user1 = user.getBody();

            if(user1 == null){
                errors.add("Invalid Email");
                throw new OutingException(errors , HttpStatus.BAD_REQUEST);
            }

              FriendshipModel checkfriendship = friendshipRepository.findByInviteeUserIdAndInviterUserIdOrInviteeUserIdAndInviterUserId(
                      this.principalDetails.getPrincipalDetails().getId(), user1.getId(), user1.getId(), this.principalDetails.getPrincipalDetails().getId()
              );

              if(checkfriendship != null){
                  if(checkfriendship.getStatus().equals("invited") && checkfriendship.getInviterUserId().equals(user1.getId())){
                      errors.add("You have already got invitation from user");
                      throw new OutingException(errors, HttpStatus.BAD_REQUEST);
                  }

                  if(checkfriendship.getStatus().equals("invited")){
                      errors.add("User is already invited");
                      throw new OutingException(errors, HttpStatus.BAD_REQUEST);
                  }
                  if(checkfriendship.getStatus().equals("accepted") ){
                      errors.add("User is already a friend");
                      throw new OutingException(errors, HttpStatus.BAD_REQUEST);

                  }

              }
            System.out.println(user1);
             FriendshipModel friendship = friendshipRepository.findByIdAndInviterUserId(friendshipId, this.principalDetails.getPrincipalDetails().getId());
             if(friendship == null){
                 errors.add("Friendship not found");
                 throw new OutingException(errors, HttpStatus.BAD_REQUEST);
             }

             if(friendship.getInviteeUserId() != null){
                 errors.add("Already updated");
                 throw new OutingException(errors, HttpStatus.BAD_REQUEST);
             }
             friendship.setInviterUserId(this.principalDetails.getPrincipalDetails().getId());
             friendship.setInviteeUserId(user1.getId());
             friendship.setStatus("invited");
             friendship.setDummyUserId(null);
             friendshipRepository.save(friendship);
        }else{
            errors.add("Email is required");
            throw new OutingException(errors, HttpStatus.BAD_REQUEST);
        }
    }




}
