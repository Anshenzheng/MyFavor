-- 手办阁数据库初始化脚本
-- 数据库: myfavor_db
-- 字符集: utf8mb4

-- 创建数据库
CREATE DATABASE IF NOT EXISTS myfavor_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE myfavor_db;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    avatar VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 标签表
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_user_tag (user_id, name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 手办表
CREATE TABLE IF NOT EXISTS figures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    purchase_date DATE,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    is_public BOOLEAN DEFAULT TRUE,
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    want_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_category_id (category_id),
    INDEX idx_is_public (is_public),
    INDEX idx_like_count (like_count),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 手办图片表
CREATE TABLE IF NOT EXISTS figure_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    figure_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_figure_id (figure_id),
    FOREIGN KEY (figure_id) REFERENCES figures(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 手办标签关联表
CREATE TABLE IF NOT EXISTS figure_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    figure_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_figure_id (figure_id),
    INDEX idx_tag_id (tag_id),
    UNIQUE KEY uk_figure_tag (figure_id, tag_id),
    FOREIGN KEY (figure_id) REFERENCES figures(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 点赞表
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    figure_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_figure_id (figure_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_figure_user (figure_id, user_id),
    FOREIGN KEY (figure_id) REFERENCES figures(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 想要表
CREATE TABLE IF NOT EXISTS wants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    figure_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_figure_id (figure_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_figure_user (figure_id, user_id),
    FOREIGN KEY (figure_id) REFERENCES figures(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试数据
-- 测试用户 (密码: 123456)
INSERT INTO users (username, password, email) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', 'admin@myfavor.com'),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', 'user1@myfavor.com');

-- 测试分类
INSERT INTO categories (name, user_id) VALUES 
('手办', 1),
('模型', 1),
('粘土人', 2);

-- 测试标签
INSERT INTO tags (name, user_id) VALUES 
('限定版', 1),
('正版', 1),
('景品', 2);

-- 测试手办
INSERT INTO figures (name, description, price, purchase_date, user_id, category_id, is_public, view_count, like_count, want_count) VALUES 
('初音未来 10周年纪念版', '初音未来10周年限定版手办，做工精美，配件丰富。', 1299.00, '2023-06-15', 1, 1, TRUE, 156, 42, 18),
('海贼王 路飞 五档', '路飞五档尼卡形态手办，造型霸气，还原度高。', 899.00, '2023-08-20', 1, 1, TRUE, 234, 67, 32),
('原神 雷电将军', '原神雷电将军手办，细节处理到位，特效件精美。', 1599.00, '2023-10-05', 2, 2, TRUE, 189, 55, 28);

-- 测试图片
INSERT INTO figure_images (figure_id, image_url, is_primary, sort_order) VALUES 
(1, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=anime%20figure%20Hatsune%20Miku%2010th%20anniversary%20edition%20colorful%20detailed&image_size=square', TRUE, 1),
(2, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=one%20piece%20luffy%20gear%205%20nika%20form%20action%20figure%20dynamic&image_size=square', TRUE, 1),
(3, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=genshin%20impact%20raiden%20shogun%20anime%20figure%20purple%20detailed&image_size=square', TRUE, 1);

-- 测试手办标签关联
INSERT INTO figure_tags (figure_id, tag_id) VALUES 
(1, 1),
(1, 2),
(2, 2),
(3, 1);
