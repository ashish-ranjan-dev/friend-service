package com.outing.friendship.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friendships")
public class FriendshipModel {

    @Id
    private String id;

    @NonNull
    private String inviterUserId;
    private String inviteeUserId = null;
    private String dummyUserId = null;
    private String status = null;

    private String inviteeName = null;

    private String inviterName = null;

    private String dummyUserName = null;

    public FriendshipModel(){

    }

    public FriendshipModel(@NonNull String inviterUserId, String inviteeUserId, String status) {
        this.inviterUserId = inviterUserId;
        this.inviteeUserId = inviteeUserId;
        this.status = status;
    }



    public FriendshipModel(String id, @NonNull String inviterUserId, String inviteeUserId, String dummyUserId, String status) {
        this.id = id;
        this.inviterUserId = inviterUserId;
        this.inviteeUserId = inviteeUserId;
        this.dummyUserId = dummyUserId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public FriendshipModel(@NonNull String inviterUserId, String dummyUserId) {
        this.inviterUserId = inviterUserId;
        this.dummyUserId = dummyUserId;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getInviterUserId() {
        return inviterUserId;
    }

    public void setInviterUserId(@NonNull String inviterUserId) {
        this.inviterUserId = inviterUserId;
    }

    public String getInviteeUserId() {
        return inviteeUserId;
    }

    public void setInviteeUserId(String inviteeUserId) {
        this.inviteeUserId = inviteeUserId;
    }

    public String getDummyUserId() {
        return dummyUserId;
    }

    public void setDummyUserId(String dummyUserId) {
        this.dummyUserId = dummyUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
