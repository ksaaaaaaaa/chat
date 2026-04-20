-- 文件信息表
CREATE TABLE IF NOT EXISTS file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_identifier VARCHAR(64) NOT NULL COMMENT '文件唯一标识',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    file_type VARCHAR(50) COMMENT '文件类型',
    file_path VARCHAR(500) COMMENT '文件存储路径',
    md5 VARCHAR(32) COMMENT '文件MD5值',
    status INT DEFAULT 0 COMMENT '状态: 0-上传中, 1-已完成, 2-失败',
    total_chunks INT COMMENT '总分片数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_file_identifier (file_identifier),
    KEY idx_md5 (md5),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';

-- 文件分片表
CREATE TABLE IF NOT EXISTS file_chunk (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_identifier VARCHAR(64) NOT NULL COMMENT '文件唯一标识',
    chunk_number INT NOT NULL COMMENT '分片序号(从0开始)',
    chunk_size BIGINT COMMENT '分片大小(字节)',
    chunk_path VARCHAR(500) COMMENT '分片存储路径',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_file_chunk (file_identifier, chunk_number),
    KEY idx_file_identifier (file_identifier)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件分片表';
