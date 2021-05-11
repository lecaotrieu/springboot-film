package com.movie.core.service.impl;

import com.movie.core.convert.CommentVideoConvert;
import com.movie.core.dto.CommentVideoDTO;
import com.movie.core.entity.CommentVideoEntity;
import com.movie.core.repository.CommentVideoRepository;
import com.movie.core.service.ICommentVideoService;
import com.movie.core.service.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentVideoService implements ICommentVideoService {

    @Autowired
    private PagingUtils pagingUtils;

    @Autowired
    private CommentVideoRepository commentVideoRepository;

    @Autowired
    private CommentVideoConvert commentVideoConvert;

    @Override
    public List<CommentVideoDTO> findByProperties(Long videoId, Long commentId, String search, String userName, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        search = search.toLowerCase();
        List<CommentVideoEntity> commentVideoEntities;
        if (commentId == null) {
            if (videoId == null) {
                commentVideoEntities = commentVideoRepository.findAllByUserAndProperties(userName, search, pageable);
            } else {
                commentVideoEntities = commentVideoRepository.findAllByVideoIdAndUser(videoId, userName, search, pageable);
            }
        } else {
            commentVideoEntities = commentVideoRepository.findAllByCommentIdAndUser(commentId, userName, search, pageable);
        }
        List<CommentVideoDTO> commentVideoDTOS = new ArrayList<>();
        for (CommentVideoEntity entity : commentVideoEntities) {
            CommentVideoDTO commentVideoDTO = commentVideoConvert.toDTO(entity);
            commentVideoDTO.setSubCommentCount(entity.getCommentVideos().size());
            commentVideoDTOS.add(commentVideoDTO);
        }
        return commentVideoDTOS;
    }

    @Override
    public List<CommentVideoDTO> findByProperties(Long commentId, Long videoId, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<CommentVideoEntity> commentVideoEntities = commentVideoRepository.findAllByCommentVideo_IdAndVideo_Id(commentId, videoId, pageable);
        List<CommentVideoDTO> commentVideoDTOS = new ArrayList<CommentVideoDTO>();
        for (CommentVideoEntity entity : commentVideoEntities) {
            CommentVideoDTO commentVideoDTO = commentVideoConvert.toDTO(entity);
            commentVideoDTOS.add(commentVideoDTO);
        }
        return commentVideoDTOS;
    }

    @Override
    public int totalComment(Long videoId, Long commentId, String search, String userName) {
        int rs = 0;
        if (commentId == null) {
            rs = (int) commentVideoRepository.countAllByUserAndProperties(userName, search);
        } else {
            if (videoId == null) {
                rs = (int) commentVideoRepository.countAllByCommentIdAndUser(commentId, userName, search);
            } else {
                rs = (int) commentVideoRepository.countAllByVideoIdAndUser(videoId, userName, search);
            }
        }
        return rs;
    }

    @Override
    public List<CommentVideoDTO> findAllByCommentId(Long commentId) {
        List<CommentVideoEntity> commentVideoEntities = commentVideoRepository.findAllByCommentVideo_Id(commentId);
        List<CommentVideoDTO> commentVideoDTOS = new ArrayList<>();
        for (CommentVideoEntity entity : commentVideoEntities) {
            CommentVideoDTO commentVideoDTO = commentVideoConvert.toDTO(entity);
            commentVideoDTOS.add(commentVideoDTO);
        }
        return commentVideoDTOS;
    }

    @Override
    public void deleteCommentVideo(Long[] ids) {
        for (Long id : ids) {
            commentVideoRepository.deleteById(id);
        }
    }

    @Override
    public CommentVideoDTO findOneById(Long commentId) {
        CommentVideoEntity commentVideoEntity = commentVideoRepository.getOne(commentId);
        return commentVideoConvert.toDTO(commentVideoEntity);
    }

    @Override
    public int totalComment(Long videoId) {
        int result = (int) commentVideoRepository.countAllByVideo_Id(videoId);
        return result;
    }

    @Transactional
    public Long save(CommentVideoDTO commentVideoDTO) throws Exception {
        CommentVideoEntity entity;
        if (commentVideoDTO.getId() != null) {
            entity = commentVideoRepository.getOne(commentVideoDTO.getId());
            entity.setContent(commentVideoDTO.getContent());
        } else {
            entity = commentVideoConvert.toEntity(commentVideoDTO);
        }
        if (entity.getTotalLike() == null){
            entity.setTotalLike(0);
        }
        entity = commentVideoRepository.save(entity);
        return entity.getId();
    }

    @Override
    public int totalComment(Long commentId, Long videoId) {
        int result = (int) commentVideoRepository.countAllByVideo_IdAndCommentVideo_Id(videoId, commentId);
        return result;
    }
}
