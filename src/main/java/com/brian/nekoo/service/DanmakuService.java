package com.brian.nekoo.service;

import com.brian.nekoo.dto.DanmakuDTO;
import com.brian.nekoo.dto.req.DanmakuReqDTO;

public interface DanmakuService {
    DanmakuDTO createDanmaku(DanmakuReqDTO dto);

    DanmakuDTO deleteDanmaku(DanmakuReqDTO dto);

    DanmakuDTO updateDanmaku(DanmakuReqDTO dto);
}