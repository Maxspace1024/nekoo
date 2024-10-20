package com.brian.nekoo.enumx;

public class Topix {
    // post
    public static final String POST_NEW = "/topic/post/new";
    public static final String POST_UPDATE = "/topic/post/update";
    public static final String POST_DELETE = "/topic/post/delete";
    public static final String POST_PROGRESS = "/topic/post/progress/";                                 // + userId

    // friendship
    public static final String FRIENDSHIP_NOTIFICATION_NEW = "/topic/friendship/notification/new/";     // + userId

    // chatroom
    public static final String CHATROOM = "/topic/chatroom/";                                           // + chatroomUuid
    public static final String CHATROOM_NEW = "/topic/chatroom/new/";                                   // + userId

    // danmaku
    public static final String DANMAKU = "/topic/danmaku/";                                             // + assetId
    public static final String DANMAKU_DELETE = "/topic/danmaku/delete/";                               // + assetId
}
