package com.example.demo.mapper;

import com.example.demo.entity.FileChunk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileChunkMapper {
    void insert(FileChunk fileChunk);
    FileChunk findByFileIdentifierAndChunkNumber(@Param("fileIdentifier") String fileIdentifier, 
                                                  @Param("chunkNumber") Integer chunkNumber);
    List<FileChunk> findByFileIdentifier(String fileIdentifier);
    int countByFileIdentifier(String fileIdentifier);
    void deleteByFileIdentifier(String fileIdentifier);
}
