package com.brian.nekoo.service;

import com.brian.nekoo.dto.DanmakuDTO;
import com.brian.nekoo.dto.req.DanmakuReqDTO;

import java.util.List;

public interface DanmakuService {
    DanmakuDTO createDanmaku(DanmakuReqDTO dto);

    DanmakuDTO deleteDanmaku(DanmakuReqDTO dto);

    DanmakuDTO updateDanmaku(DanmakuReqDTO dto);

    List<DanmakuDTO> findDanmakusByAssetId(DanmakuReqDTO dto);
}