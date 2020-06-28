package com.lovesosa.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.lovesosa.guli.service.edu.entity.Subject;
import com.lovesosa.guli.service.edu.entity.excel.ExcelSubjectData;
import com.lovesosa.guli.service.edu.entity.vo.SubjectVO;
import com.lovesosa.guli.service.edu.listener.ExcelSubjectDataListener;
import com.lovesosa.guli.service.edu.mapper.SubjectMapper;
import com.lovesosa.guli.service.edu.service.ISubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {


    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class,new ExcelSubjectDataListener(baseMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet().doRead();
    }

    @Override
    public List<SubjectVO> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }


}
