package top.spark.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.spark.live.constant.GlobalSql;
import top.spark.live.entity.Category;
import top.spark.live.mapper.CategoryMapper;
import top.spark.live.service.CategoryService;
import top.spark.live.vo.CategoryListVO;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryListVO> getCategoryList() {
        QueryWrapper<Category> qw = new QueryWrapper<>();
        qw.lambda().eq(Category::getStatus, GlobalSql.STATUS_USE);
        List<Category> list = categoryMapper.selectList(qw);
        List<CategoryListVO> result = new ArrayList<>();
        list.forEach(ct -> result.add(new CategoryListVO(ct.getId(),ct.getTitle(),ct.getDescription())));
        return result;
    }
}
