package com.outing.friendship.services;

import com.outing.friendship.api.dto.DummyUserDto;
import com.outing.friendship.api.dto.FriendshipDto;

import java.io.Serializable;
import java.util.List;

public interface FriendshipCheckService extends Serializable {
    List<FriendshipDto> getAllFriendsByUserId(String userId);
    DummyUserDto getDummyUserById(String id);
}
