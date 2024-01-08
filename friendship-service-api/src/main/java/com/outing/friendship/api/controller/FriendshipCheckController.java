package com.outing.friendship.api.controller;

import com.outing.commons.api.response.OutingResponse;
import com.outing.friendship.api.dto.DummyUserDto;
import com.outing.friendship.api.dto.FriendshipDto;
//import com.outing.commons.api.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url = "http://localhost:8091",value = "friendship-service")
public interface    FriendshipCheckController {
//    @PostMapping(value = "/friend-service")
//    ResponseEntity<Response<List<FriendshipDto>>> getAllFriendsByUserId(@RequestBody String userId);

    @PostMapping(value = "/friend-service")
    ResponseEntity<List<FriendshipDto>> getAllFriendsByUserId(@RequestBody String userId);

    @PostMapping(value = "/friend-service/dummy-user/{id}")
    ResponseEntity<DummyUserDto> getDummyUserById(@PathVariable String id);
}



