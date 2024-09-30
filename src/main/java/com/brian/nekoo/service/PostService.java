package com.brian.nekoo.service;

import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.dto.req.UploadPostReqDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(UploadPostReqDTO dto);

    PostDTO deletePost(UploadPostReqDTO dto);

    PostDTO updatePost(UploadPostReqDTO dto);

    List<PostDTO> findPost();

    List<PostDTO> findPostByPage(PostReqDTO dto);
}
