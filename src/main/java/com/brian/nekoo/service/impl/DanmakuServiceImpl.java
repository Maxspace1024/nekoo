package com.brian.nekoo.service.impl;

import com.brian.nekoo.dto.DanmakuDTO;
import com.brian.nekoo.dto.req.DanmakuReqDTO;
import com.brian.nekoo.entity.mongo.Danmaku;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.repository.mongo.DanmakuRepository;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.DanmakuService;
import com.brian.nekoo.service.S3Service;
import com.brian.nekoo.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanmakuServiceImpl implements DanmakuService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final DanmakuRepository danmakuRepository;

    @Override
    public DanmakuDTO createDanmaku(DanmakuReqDTO dto) {
        String uuidFilename = null;
        try {
            MultipartFile file = dto.getImage();
            if (file != null && !file.isEmpty()) {
                uuidFilename = FileUtil.generateUuidFileName(file.getOriginalFilename());
                s3Service.uploadFile(file, uuidFilename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instant now = Instant.now();
        Danmaku dmk = Danmaku.builder()
            .userId(dto.getUserId())
            .assetId(dto.getAssetId())
            .type(dto.getType())
            .content(dto.getContent())
            .imagePath(uuidFilename)
            // attr
            .color(dto.getColor())
            .backgroundColor(dto.getBackgroundColor())
            .visible(dto.getVisible())
            .size(dto.getSize())
            .posX(dto.getPosX())
            .posY(dto.getPosY())
            .appearAt(dto.getAppearAt())
            // time
            .createAt(now)
            .modifyAt(now)
            .build();
        dmk = danmakuRepository.save(dmk);

        User user = userRepository.findById(dmk.getUserId()).get();
        return DanmakuDTO.getDTO(dmk, user);
    }

    @Override
    public DanmakuDTO deleteDanmaku(DanmakuReqDTO dto) {
        Optional<Danmaku> oDmk = danmakuRepository.findById(dto.getDanmakuId());
        if (oDmk.isPresent()) {
            Instant now = Instant.now();
            Danmaku dmk = oDmk.get();
            dmk.setModifyAt(now);
            dmk.setRemoveAt(now);
            dmk = danmakuRepository.save(dmk);

            User user = userRepository.findById(dmk.getUserId()).get();
            return DanmakuDTO.getDTO(dmk, user);
        } else {
            return null;
        }
    }

    @Override
    public DanmakuDTO updateDanmaku(DanmakuReqDTO dto) {
        String uuidFilename = null;
        try {
            MultipartFile file = dto.getImage();
            uuidFilename = FileUtil.generateUuidFileName(file.getOriginalFilename());

            s3Service.uploadFile(file, uuidFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<Danmaku> oDmk = danmakuRepository.findById(dto.getDanmakuId());
        if (oDmk.isPresent()) {
            Instant now = Instant.now();
            Danmaku dmk = oDmk.get();
            // basic info
            Integer type = dto.getType();
            if (!Objects.isNull(type)) {
                dmk.setType(type);
            }
            Integer visible = dto.getVisible();
            if (!Objects.isNull(visible)) {
                dmk.setVisible(visible);
            }
            String content = dto.getContent();
            if (!Objects.isNull(content)) {
                dmk.setContent(content);
            }
            if (!Objects.isNull(uuidFilename)) {
                dmk.setImagePath(uuidFilename);
            }
            // attr info
            Integer size = dto.getSize();
            if (!Objects.isNull(size)) {
                dmk.setSize(size);
            }
            Float posX = dto.getPosX();
            if (!Objects.isNull(posX)) {
                dmk.setPosX(posX);
            }
            Float posY = dto.getPosY();
            if (!Objects.isNull(posY)) {
                dmk.setPosY(posY);
            }
            String color = dto.getColor();
            if (!Objects.isNull(color)) {
                dmk.setColor(color);
            }
            String backgroundColor = dto.getBackgroundColor();
            if (!Objects.isNull(backgroundColor)) {
                dmk.setBackgroundColor(dto.getBackgroundColor());
            }
            Float appearAt = dto.getAppearAt();
            if (!Objects.isNull(appearAt)) {
                dmk.setPosX(appearAt);
            }
            dmk.setModifyAt(now);
            dmk = danmakuRepository.save(dmk);

            User user = userRepository.findById(dmk.getUserId()).get();
            return DanmakuDTO.getDTO(dmk, user);
        } else {
            return null;
        }
    }

    @Override
    public List<DanmakuDTO> findDanmakusByAssetId(DanmakuReqDTO dto) {
//        Page<Danmaku> danmakus = danmakuRepository.findByAssetIdOrderByCreateAtDesc(dto.getAssetId(), null);
//        Page<Danmaku> danmakus = danmakuRepository.findByAssetIdWithQuery(dto.getAssetId(), null);
        List<Danmaku> danmakus = danmakuRepository.findByAssetId(dto.getAssetId());
        return danmakus.stream().map(danmaku -> {
            User user = userRepository.findById(danmaku.getUserId()).get();
            return DanmakuDTO.getDTO(danmaku, user);
        }).toList();
    }
}
