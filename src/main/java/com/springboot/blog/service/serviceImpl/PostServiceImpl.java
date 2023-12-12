package com.springboot.blog.service.serviceImpl;


import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    public PostRepository postRepository;

    //constructor Based DI
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //calling map to entity method
        Post post=mapToEntity(postDto);
        Post newPost=postRepository.save(post);

        //calling entity to DTO entity
        PostDto postResponse=mapToDto(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {
        //it will used to sort in asc and des
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        //pageable instance
        // Pageable pageable= PageRequest.of(pageNo,pageSize);

         Pageable pageable=PageRequest.of(pageNo, pageSize, sort);


         Page<Post> posts=postRepository.findAll(pageable);
         //get content of page object
        List<Post> listOfPosts=posts.getContent();
         //convering these posts to PoseDto because we have to return postDTo
         List<PostDto> content= listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);//adding list to postresponse
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;


    }

    @Override
    public PostDto getPostById(Long id) {
        Post post=postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        //get post by id from the DB
        Post post= postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatePost=postRepository.save(post);
        return mapToDto(updatePost);

    }

    @Override
    public void deletePostById(long id) {
        //get post by id from DB
        Post post=postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);

    }

    //convert entity in to DTO
    private PostDto mapToDto(Post post){
        PostDto postDto=new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }

    //convert DTO to entity
    private Post mapToEntity(PostDto postDto){
        Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }


}
