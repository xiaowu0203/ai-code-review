-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_code_review DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_code_review;

-- 项目表
CREATE TABLE `project` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `gitee_project_id` bigint(20) NOT NULL COMMENT 'Gitee项目ID',
    `project_name` varchar(255) NOT NULL COMMENT '项目名称',
    `project_url` varchar(500) NOT NULL COMMENT '项目URL',
    `webhook_token` varchar(255) COMMENT 'Webhook验证Token',
    `ai_model` varchar(50) DEFAULT 'qianwen' COMMENT '默认AI模型',
    `status` tinyint(4) DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `gitee_token` varchar(500) NOT NULL COMMENT 'Gitee访问令牌',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_gitee_project_id` (`gitee_project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目信息表';

-- Pull Request表
CREATE TABLE `pull_request` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
    `gitee_pr_id` bigint(20) NOT NULL COMMENT 'Gitee PR ID',
    `pr_number` int(11) NOT NULL COMMENT 'PR编号',
    `pr_title` varchar(500) NOT NULL COMMENT 'PR标题',
    `pr_url` varchar(500) NOT NULL COMMENT 'PR URL',
    `author` varchar(255) NOT NULL COMMENT '作者',
    `source_branch` varchar(255) NOT NULL COMMENT '源分支',
    `target_branch` varchar(255) NOT NULL COMMENT '目标分支',
    `status` varchar(50) DEFAULT 'open' COMMENT '状态：open, merged, closed',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_pr` (`project_id`, `gitee_pr_id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Pull Request表';

-- 代码审核记录表
CREATE TABLE `code_review` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `pr_id` bigint(20) NOT NULL COMMENT 'PR ID',
    `review_type` varchar(20) NOT NULL COMMENT '审核类型：auto, manual',
    `ai_model` varchar(50) NOT NULL COMMENT 'AI模型',
    `diff_content` longtext COMMENT '代码差异内容',
    `review_result` longtext COMMENT '审核结果JSON',
    `status` varchar(20) DEFAULT 'processing' COMMENT '状态：processing, completed, failed',
    `error_message` text COMMENT '错误信息',
    `trigger_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '触发时间',
    `complete_time` datetime NULL COMMENT '完成时间',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_pr_id` (`pr_id`),
    KEY `idx_status` (`status`),
    KEY `idx_ai_model` (`ai_model`),
    KEY `idx_trigger_time` (`trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码审核记录表';

-- AI模型配置表
CREATE TABLE `ai_model_config` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `model_code` varchar(50) NOT NULL COMMENT '模型代码',
    `model_name` varchar(100) NOT NULL COMMENT '模型名称',
    `api_endpoint` varchar(500) COMMENT 'API端点',
    `api_key` varchar(500) COMMENT 'API密钥',
    `max_tokens` int(11) DEFAULT 4096 COMMENT '最大Token数',
    `temperature` decimal(3,2) DEFAULT 0.70 COMMENT '温度参数',
    `is_enabled` tinyint(4) DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_model_code` (`model_code`),
    KEY `idx_is_enabled` (`is_enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI模型配置表';

-- 审核规则配置表（可选，未来扩展使用）
CREATE TABLE `review_rule` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
    `rule_name` varchar(255) NOT NULL COMMENT '规则名称',
    `rule_type` varchar(50) NOT NULL COMMENT '规则类型：code_style, security, performance',
    `rule_pattern` varchar(500) COMMENT '匹配模式',
    `rule_description` text COMMENT '规则描述',
    `severity` varchar(20) DEFAULT 'medium' COMMENT '严重程度：low, medium, high, critical',
    `is_enabled` tinyint(4) DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_rule_type` (`rule_type`),
    KEY `idx_is_enabled` (`is_enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审核规则配置表';

-- 初始化AI模型配置数据
INSERT INTO `ai_model_config` (`model_code`, `model_name`, `api_endpoint`, `max_tokens`, `temperature`, `is_enabled`) VALUES
('qianwen', '千问', 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation', 4096, 0.70, 1),
('glm', 'GLM', 'https://open.bigmodel.cn/api/paas/v4/chat/completions', 4096, 0.70, 1),
('deepseek', 'DeepSeek', 'https://api.deepseek.com/chat/completions', 4096, 0.70, 1);

-- 插入示例项目数据（可选）
INSERT INTO `project` (`gitee_project_id`, `project_name`, `project_url`, `gitee_token`, `ai_model`, `status`) VALUES
(123456, '示例项目', 'https://gitee.com/your-username/sample-project', 'your_gitee_token_here', 'qianwen', 1);

-- 插入示例PR数据（可选）
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(1, 789, 1, 'feat: 添加新功能', 'https://gitee.com/your-username/sample-project/pulls/1', 'developer', 'feature/new-feature', 'master', 'open');

-- 插入示例审核记录（可选）
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `status`) VALUES
(1, 'manual', 'qianwen', 'completed');