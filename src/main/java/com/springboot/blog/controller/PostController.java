package com.springboot.blog.controller;


import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {
    private PostService postService;

    //constructor Based DI
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create Blog post
    @PostMapping()
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get All post rest Api
    //adding pagination added to this method
    @GetMapping()
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    //get post by id
    //
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    //update post by id rest api
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable(name="id") Long id){

        PostDto postResponse=postService.updatePost(postDto,id);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    //delete post by rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully",HttpStatus.OK);
    }

}
