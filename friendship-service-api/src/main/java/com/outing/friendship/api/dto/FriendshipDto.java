package com.outing.friendship.api.dto;

import com.outing.auth.api.dto.UserDto;

import java.io.Serializable;

public class FriendshipDto implements Serializable {

    private String id;
    private UserDto inviterUser;
    private UserDto inviteeUser;
    private DummyUserDto dummyUserDto;
    private String status;

    public FriendshipDto(String id, UserDto inviterUser, UserDto inviteeUser, DummyUserDto dummyUserDto, String status) {
        this.id = id;
        this.inviterUser = inviterUser;
        this.inviteeUser = inviteeUser;
        this.dummyUserDto = dummyUserDto;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDto getInviterUser() {
        return inviterUser;
    }

    public void setInviterUser(UserDto inviterUser) {
        this.inviterUser = inviterUser;
    }

    public UserDto getInviteeUser() {
        return inviteeUser;
    }

    public void setInviteeUser(UserDto inviteeUser) {
        this.inviteeUser = inviteeUser;
    }

    public DummyUserDto getDummyUser() {
        return dummyUserDto;
    }

    public void setDummyUser(DummyUserDto dummyUserModel) {
        this.dummyUserDto = dummyUserModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
