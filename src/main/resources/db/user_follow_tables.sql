-- 用户关注关系表
CREATE TABLE IF NOT EXISTS forum_follow (
    id INT AUTO_INCREMENT PRIMARY KEY,
    follower_id INT NOT NULL COMMENT '关注者ID',
    following_id INT NOT NULL COMMENT '被关注者ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_follower_following (follower_id, following_id),
    KEY idx_follower_id (follower_id),
    KEY idx_following_id (following_id),
    FOREIGN KEY (follower_id) REFERENCES forum_user(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES forum_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注关系表';

-- 为用户表添加关注数和粉丝数字段
ALTER TABLE forum_user ADD COLUMN IF NOT EXISTS following_count INT DEFAULT 0 COMMENT '关注数' AFTER likes_count;
ALTER TABLE forum_user ADD COLUMN IF NOT EXISTS followers_count INT DEFAULT 0 COMMENT '粉丝数' AFTER following_count;
