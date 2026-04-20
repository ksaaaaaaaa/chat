package com.example.demo.mapper;

import com.example.demo.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    void insert(FileInfo fileInfo);
    void update(FileInfo fileInfo);
    FileInfo findById(Long id);
    FileInfo findByFileIdentifier(String fileIdentifier);
    FileInfo findByMd5(String md5);
    List<FileInfo> findByStatus(Integer status);
    void deleteById(Long id);
}
