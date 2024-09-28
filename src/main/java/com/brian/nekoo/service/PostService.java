package com.brian.nekoo.service;

import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostReqDTO dto);

    PostDTO deletePost(PostReqDTO dto);

    PostDTO updatePost(PostReqDTO dto);

    List<PostDTO> findPost();
}
