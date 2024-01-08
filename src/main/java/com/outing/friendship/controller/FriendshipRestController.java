package com.outing.friendship.controller;

import com.outing.commons.api.response.OutingResponse;
import com.outing.friendship.api.dto.FriendshipDto;
//import com.outing.commons.api.response.Response;
import com.outing.friendship.model.FriendshipModel;
import com.outing.auth.api.controller.UserController;
import com.outing.friendship.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/friendship")
public class FriendshipRestController {

     @Autowired
     private FriendshipService friendshipService;

     @Autowired
     private UserController userController;

//     @Value("${eureka.instance.instance-id}")
//     private String applicationName;


     @PostMapping("/test")
     public ResponseEntity<?> addTestData(@RequestBody FriendshipModel friend){
         FriendshipModel save = this.friendshipService.addTestData(friend);
         return ResponseEntity.ok(save);
     }

     @GetMapping("/")
     public OutingResponse<List<FriendshipDto>> getAllFriendsByUserId(){
//          String instanceId = applicationName;
//          System.out.println(instanceId);
          List<FriendshipDto> friends = friendshipService.getAllFriendsByUserId();
          List<String> messages = new ArrayList<>();
//          Response<List<FriendshipDto>> response = new Response<>(friends, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(friends,HttpStatus.OK,messages);
     }

     @GetMapping("/sent-requests")
     public OutingResponse<List<FriendshipDto>> getAllFriendsRequests(){
          System.out.println("Sent requests");
          List<FriendshipDto> friends = friendshipService.sentRequests();
          List<String> messages = new ArrayList<>();
//          Response<List<FriendshipDto>> response = new Response<>(friends, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(friends,HttpStatus.OK,messages);
     }

     @GetMapping("/received-requests")
     public OutingResponse<List<FriendshipDto>> getAllFriendsInvitations(){
          List<FriendshipDto> friends = friendshipService.receivedRequests();
          List<String> messages = new ArrayList<>();
//          Response<List<FriendshipDto>> response = new Response<>(friends, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(friends,HttpStatus.OK,messages);
     }


     @PostMapping("/{friendship_id}/accept")
     public OutingResponse<Void> acceptFriendInvitation(@PathVariable("friendship_id") String friendship_id){
          System.out.println("rest accept");
          FriendshipModel friendship = friendshipService.acceptFriendInvitation( friendship_id);
          List<String> messages = new ArrayList<>();
          messages.add("Friendship Accepted Successfully");
//          Response<Void> response = new Response<>(null, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(null,HttpStatus.OK,messages);
     }

     @PostMapping("/{friendship_id}/cancel")
     public OutingResponse<Void> cancelFriendInvitation(@PathVariable("friendship_id") String friendship_id ){
          friendshipService.cancelInvitation(friendship_id);
          List<String> messages = new ArrayList<>();
          messages.add("Friendship Cancelled Successfully");
//          Response<Void> response = new Response<>(null, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(null,HttpStatus.OK,messages);
     }

     @PostMapping("/{friendship_id}/reject")
     public OutingResponse<Void> rejectFriendInvitation(@PathVariable("friendship_id") String friendship_id ){
          friendshipService.rejectInvitation(friendship_id);
          List<String> messages = new ArrayList<>();
          messages.add("Friendship Rejected Successfully");
//          Response<Void> response = new Response<>(null, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(null,HttpStatus.OK,messages);
     }

     @PostMapping("/{friendship_id}/revoke")
     public OutingResponse<Void> revokeInvitation(@PathVariable("friendship_id") String friendship_id ){
          friendshipService.revokeFriendship(friendship_id);
          List<String> messages = new ArrayList<>();
          messages.add("Friendship Revoked Successfully");
//          Response<Void> response = new Response<>(null, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(null,HttpStatus.OK,messages);
     }

     @PostMapping("/invite")
     public OutingResponse<FriendshipDto> sendInvite(@RequestBody Map<String, String> inviteRequest) {
          String name = inviteRequest.get("name");
          String email = inviteRequest.get("email");

          if (StringUtils.hasText(name) || StringUtils.hasText(email)) {


               FriendshipDto result = friendshipService.sendInvite(name, email);
               if (result != null) {
                    System.out.println("Result => "+ result);
                    if(result.getDummyUser() != null){
                         List<String> messages = new ArrayList<>();
                         messages.add("Successfully Created");
//                         Response<FriendshipDto> response = new Response<>(result, messages);
//                         return new ResponseEntity<>(response, HttpStatus.OK);
                         return new OutingResponse<>(result,HttpStatus.OK,messages);                    }
                    else {
                         List<String> messages = new ArrayList<>();
                         messages.add("Friend Request Sent Successfully");
//                         Response<FriendshipDto> response = new Response<>(result, messages);
//                         return new ResponseEntity<>(response, HttpStatus.OK);
                         return new OutingResponse<>(result,HttpStatus.OK,messages);
                    }
               }


          }
          System.out.println("Something is not good");
//          return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
          return new OutingResponse<>(null,HttpStatus.BAD_GATEWAY);

     }


     @GetMapping("/{friendship_id}/friend")
     public OutingResponse<FriendshipDto> getFriendshipById(@PathVariable("friendship_id") String friendship_id){
          FriendshipDto friend = friendshipService.getFriendshipById(friendship_id);
//          Response<FriendshipDto> response = new Response<>(friend, null);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(friend,HttpStatus.OK);
     }

     @PostMapping("/update")
     public OutingResponse<Void> updateFriendDetails(@RequestBody Map<String, String> details){
          String email = details.get("email");
          String friendshipId = details.get("friendship_id");
          System.out.println(email + " " + friendshipId);

          friendshipService.updateFriendDetails(email, friendshipId);

          List<String> messages = new ArrayList<>();
          messages.add("Friend Details Updated Successfully");
//          Response<Void> response = new Response<>(null, messages);
//          return new ResponseEntity<>(response, HttpStatus.OK);
          return new OutingResponse<>(null,HttpStatus.OK,messages);
     }
}
