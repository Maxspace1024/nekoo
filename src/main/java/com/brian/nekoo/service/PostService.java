package com.brian.nekoo.service;

import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.dto.req.UploadPostReqDTO;
import com.brian.nekoo.entity.mysql.User;

import java.util.List;

public interface PostService {
    PostDTO findPostById(String postId, User reqUser);

    PostDTO createPost(UploadPostReqDTO dto);

    PostDTO deletePost(UploadPostReqDTO dto);

    PostDTO updatePost(UploadPostReqDTO dto);

    List<PostDTO> findPost();

    PageWrapper<PostDTO> findAllPublicPostByPage(PostReqDTO dto);

    PageWrapper<PostDTO> findPostByPageWithUser(PostReqDTO dto, long reqUser, long profileUserid);
}
