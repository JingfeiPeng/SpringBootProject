package com.JingfeiPeng.ws.ui.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JingfeiPeng.ws.ui.model.request.UpdateUserDetailRequestModel;
import com.JingfeiPeng.ws.ui.model.request.UserDetailRequestModel;
import com.JingfeiPeng.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") // http:///localhost:8080/users 
public class UserController {
	
	Map<String, UserRest> users;
	
	@GetMapping(path="/{userId}", produces = { 
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			})
	public ResponseEntity<UserRest> getUser(@PathVariable String userId) 
	{
		if (users.containsKey(userId)) {
			return new ResponseEntity<>(users.get(userId),HttpStatus.OK);

		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping
	public String getUsers(
			@RequestParam(value="page", defaultValue="1", required=false) int page,
			@RequestParam(value="limit", defaultValue="50") int limit,
			@RequestParam(value="sort",defaultValue="desc" ,required = false) String sort
			) 
	{
		return sort +" get users was called with Page: " + page + " limit :" + limit;
	}
	
	@PostMapping(
			consumes = { 
				MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE
			}, 
			produces = { 
				MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE
			}
			)
	public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailRequestModel userDetails)
	{
		UserRest returnValue = new UserRest();
		returnValue.setEmail(userDetails.getEmail());
		returnValue.setFirstName(userDetails.getFirstName());
		returnValue.setLastName(userDetails.getLastName());
		
		if (users == null) {
			users = new HashMap<>();
		}
		String userId = UUID.randomUUID().toString();
		returnValue.setUserId(userId);
		users.put(userId, returnValue);
		
		return (new ResponseEntity<UserRest>(returnValue,HttpStatus.OK));
	}
	
	@PatchMapping 
	public String patchUser()
	{
		return "patch user was called";
	}
	
	@PutMapping(
			path="/{userId}",
			consumes = { 
				MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE
			}, 
			produces = { 
				MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE
			}
			)
	public UserRest updateUser(
			@PathVariable String userId,
			@Valid @RequestBody UpdateUserDetailRequestModel userDetails )
	{
		UserRest storedUserDetail = users.get(userId);
		storedUserDetail.setFirstName(userDetails.getFirstName());
		storedUserDetail.setLastName(userDetails.getLastName());
		users.put(userId, storedUserDetail);
		return storedUserDetail;
		
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id)
	{
		users.remove(id);
		return ResponseEntity.noContent().build();
	}
}
