package com.outing.friendship.controller;

import com.outing.commons.api.response.OutingResponse;
//import com.outing.commons.api.response.Response;
import com.outing.friendship.api.controller.FriendshipCheckController;
import com.outing.friendship.api.dto.DummyUserDto;
import com.outing.friendship.api.dto.FriendshipDto;
import com.outing.friendship.services.FriendshipCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendshipCheckRestController implements FriendshipCheckController {
    @Autowired
    FriendshipCheckService friendshipCheckService;
    @Override
    public ResponseEntity<List<FriendshipDto>> getAllFriendsByUserId(String userId) {
        List<FriendshipDto> friends = friendshipCheckService.getAllFriendsByUserId(userId);
        List<String> messages = new ArrayList<>();
//        Response<List<FriendshipDto>> response = new Response<>(friends, messages);
        return new ResponseEntity<>(friends, HttpStatus.OK);
//        return new OutingResponse<>(friends);
    }

    @Override
    public ResponseEntity<DummyUserDto> getDummyUserById(String id) {
        DummyUserDto dummyUserDto = this.friendshipCheckService.getDummyUserById(id);
        return new ResponseEntity<>(dummyUserDto,HttpStatus.OK);
    }
}
