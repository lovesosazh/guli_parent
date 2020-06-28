package com.lovesosa.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lovesosa.guli.service.edu.entity.Subject;
import com.lovesosa.guli.service.edu.entity.excel.ExcelSubjectData;
import com.lovesosa.guli.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lovesosa
 */
@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {


    private SubjectMapper subjectMapper;
//    private List<String> list = new ArrayList();

    public ExcelSubjectDataListener() {
    }

    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext analysisContext) {
        log.info("解析到一条记录 {}",data);
        // 处理读取到的数据
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();
        log.info("levelOneTitle {}", levelOneTitle);
        log.info("levelTwoTitle {}", levelTwoTitle);

        Subject subjectLevelOne = getByTitle(levelOneTitle);
        String parentId = null;
        if (subjectLevelOne == null) {
//             组装一级分类
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);
            subjectMapper.insert(subject);
            parentId = subject.getId();
        } else {
            parentId = subjectLevelOne.getId();
        }

        Subject subjectLevelTwo = getSubTitle(levelTwoTitle, parentId);
        if (subjectLevelTwo == null) {
//            组装二级类别
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("全部数据解析完成！");
    }


    private Subject getByTitle(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");
        return subjectMapper.selectOne(queryWrapper);
    }


    private Subject getSubTitle(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        return subjectMapper.selectOne(queryWrapper);
    }




}
