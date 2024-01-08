package com.outing.friendship.services;

import com.outing.friendship.api.dto.FriendshipDto;
import com.outing.friendship.model.FriendshipModel;
import com.outing.auth.api.dto.UserDto;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public interface FriendshipService extends Serializable {
    public FriendshipModel addTestData(FriendshipModel friendship);

    public List<FriendshipDto> getAllFriendsByUserId();

    public List<FriendshipDto> sentRequests();

    public List<FriendshipDto> receivedRequests();

    public UserDto getUserByUsername(String input);

    public FriendshipModel acceptFriendInvitation(String friendshipId);

    public void cancelInvitation(String friendshipId);

    public void rejectInvitation(String friendshipId);

    public void revokeFriendship(String friendshipId);

    public FriendshipDto sendInvite(String name, String email);

    public FriendshipDto getFriendshipById(String friendship_id);

    public void updateFriendDetails(String email, String friendshipId);

}
