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

    // 找那些屬於profileUser的文章，並檢查與reqUser的關係(決定是否要找出非公開貼文)
    PageWrapper<PostDTO> findPostByPageWithUser(PostReqDTO dto, long reqUser, long profileUserid);
}
