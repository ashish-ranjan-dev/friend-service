package com.outing.friendship.repository;

import com.outing.friendship.model.FriendshipModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends MongoRepository<FriendshipModel, String> {

    List<FriendshipModel> findByStatus(String status);

    List<FriendshipModel> findByInviterUserIdOrInviteeUserIdAndStatusIn(String inviterUserId, String inviteeUserId, List<String> statuses);


    List<FriendshipModel> findByInviterUserIdOrInviteeUserIdAndStatusOrStatusIsNull(String userId , String invitee, String status , String status1);
    List<FriendshipModel> findByInviterUserIdAndStatus(String userId, String status);

    List<FriendshipModel> findByInviteeUserIdAndStatus(String inviteeUserId, String status);

    List<FriendshipModel> findByInviterUserIdAndStatusOrStatusIsNull(String userId, String status);

    FriendshipModel findByInviterUserIdAndInviteeUserId(String inviterUserId, String inviteeUserId);

    FriendshipModel findByIdAndInviterUserId(String friendshipId, String inviterUserId);
    FriendshipModel findByIdAndInviteeUserId(String friendshipId, String inviteeUserId);

    FriendshipModel findByIdAndInviteeUserIdAndStatus(String friendshipId, String inviteeUserId, String status);

    FriendshipModel findByIdAndInviterUserIdAndStatus(String friendshipId, String inviterUserId, String status);

    FriendshipModel findByIdAndStatus(String friendshipId, String status);


    FriendshipModel findByInviteeUserIdAndInviterUserIdOrInviteeUserIdAndInviterUserId(String userId1, String UserId2, String UserId5, String UserID4);

     FriendshipModel findByIdAndStatusOrStatusIsNull(String fid, String status);
}
