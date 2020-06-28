package com.lovesosa.guli.service.edu.service.impl;

import com.lovesosa.guli.service.edu.entity.Comment;
import com.lovesosa.guli.service.edu.mapper.CommentMapper;
import com.lovesosa.guli.service.edu.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
