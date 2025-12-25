-- 测试数据SQL
-- 注意：在插入前请确保已经运行过init.sql创建表结构

USE ai_code_review;

-- 清空现有测试数据（可选）
-- DELETE FROM code_review;
-- DELETE FROM pull_request;
-- DELETE FROM project WHERE id > 1;

-- ============= 项目表测试数据 =============
INSERT INTO `project` (`gitee_project_id`, `project_name`, `project_url`, `gitee_token`, `ai_model`, `status`) VALUES
(123456, '示例项目', 'https://gitee.com/example/sample-project', 'gitee_token_123456', 'qianwen', 1),
(234567, '电商平台', 'https://gitee.com/example/ecommerce-platform', 'gitee_token_234567', 'glm', 1),
(345678, '管理系统', 'https://gitee.com/example/admin-system', 'gitee_token_345678', 'deepseek', 1),
(456789, '社交应用', 'https://gitee.com/example/social-app', 'gitee_token_456789', 'qianwen', 1),
(567890, '在线教育', 'https://gitee.com/example/education-platform', 'gitee_token_567890', 'glm', 1),
(678901, '内容管理系统', 'https://gitee.com/example/cms-platform', 'gitee_token_678901', 'deepseek', 1),
(789012, '数据分析平台', 'https://gitee.com/example/data-analytics', 'gitee_token_789012', 'qianwen', 1),
(890123, 'API网关', 'https://gitee.com/example/api-gateway', 'gitee_token_890123', 'glm', 0),
(901234, '微服务架构', 'https://gitee.com/example/microservices', 'gitee_token_901234', 'deepseek', 1),
(012345, '云原生应用', 'https://gitee.com/example/cloud-native', 'gitee_token_012345', 'qianwen', 1);

-- ============= Pull Request表测试数据 =============
-- 项目1的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(1, 1001, 10, 'feat: 添加用户登录功能', 'https://gitee.com/example/sample-project/pulls/10', 'zhangsan', 'feature/login', 'master', 'merged'),
(1, 1002, 11, 'fix: 修复登录验证bug', 'https://gitee.com/example/sample-project/pulls/11', 'lisi', 'hotfix/login-fix', 'master', 'merged'),
(1, 1003, 12, 'feat: 添加用户权限管理', 'https://gitee.com/example/sample-project/pulls/12', 'wangwu', 'feature/permission', 'master', 'open'),
(1, 1004, 13, 'refactor: 重构订单模块', 'https://gitee.com/example/sample-project/pulls/13', 'zhangsan', 'refactor/order', 'master', 'closed'),
(1, 1005, 14, 'feat: 集成支付宝支付', 'https://gitee.com/example/sample-project/pulls/14', 'lisi', 'feature/alipay', 'master', 'merged');

-- 项目2的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(2, 2001, 20, 'feat: 添加购物车功能', 'https://gitee.com/example/ecommerce-platform/pulls/20', 'zhaoliu', 'feature/shopping-cart', 'main', 'merged'),
(2, 2002, 21, 'fix: 修复库存更新问题', 'https://gitee.com/example/ecommerce-platform/pulls/21', 'qianqi', 'hotfix/inventory-fix', 'main', 'merged'),
(2, 2003, 22, 'feat: 实现订单管理', 'https://gitee.com/example/ecommerce-platform/pulls/22', 'sunba', 'feature/order-management', 'main', 'open');

-- 项目3的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(3, 3001, 30, 'feat: 添加用户管理模块', 'https://gitee.com/example/admin-system/pulls/30', 'zhoujiu', 'feature/user-management', 'dev', 'merged'),
(3, 3002, 31, 'feat: 添加角色权限', 'https://gitee.com/example/admin-system/pulls/31', 'wushi', 'feature/role-permission', 'dev', 'open');

-- 项目4的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(4, 4001, 40, 'feat: 实现用户注册', 'https://gitee.com/example/social-app/pulls/40', 'zhengyi', 'feature/register', 'main', 'merged'),
(4, 4002, 41, 'feat: 添加好友功能', 'https://gitee.com/example/social-app/pulls/41', 'chener', 'feature/friendship', 'main', 'merged'),
(4, 4003, 42, 'feat: 实现动态发布', 'https://gitee.com/example/social-app/pulls/42', 'linliu', 'feature/posts', 'main', 'merged'),
(4, 4004, 43, 'fix: 修复消息推送延迟', 'https://gitee.com/example/social-app/pulls/43', 'wangqi', 'bugfix/push-delay', 'main', 'open');

-- 项目5的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(5, 5001, 50, 'feat: 添加在线课程模块', 'https://gitee.com/example/education-platform/pulls/50', 'liba', 'feature/online-course', 'main', 'merged');

-- 项目6的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(6, 6001, 60, 'feat: 文章编辑器', 'https://gitee.com/example/cms-platform/pulls/60', 'zhangjiu', 'feature/editor', 'main', 'merged'),
(6, 6002, 61, 'feat: 媒体管理', 'https://gitee.com/example/cms-platform/pulls/61', 'lisi', 'feature/media-management', 'main', 'merged');

-- 项目7的PR
INSERT INTO `pull_request` (`project_id`, `gitee_pr_id`, `pr_number`, `pr_title`, `pr_url`, `author`, `source_branch`, `target_branch`, `status`) VALUES
(7, 7001, 70, 'feat: 数据可视化', 'https://gitee.com/example/data-analytics/pulls/70', 'wangwu', 'feature/data-visualization', 'master', 'merged');

-- ============= 代码审核记录表测试数据 =============
-- 项目1的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(1, 'auto', 'qianwen', 'diff content here', '{"summary": "代码质量良好", "issues": [], "suggestions": "无建议"}', 'completed', '2024-12-06 10:00:00', '2024-12-06 10:01:30'),
(1, 'manual', 'glm', 'diff content here', '{"summary": "发现一个小问题", "issues": [{"file": "UserController.java", "line": 50, "type": "bug", "severity": "low", "description": "可能存在空指针异常", "suggestion": "添加null检查"}]}', 'completed', '2024-12-07 14:30:00', '2024-12-07 14:32:15'),
(2, 'auto', 'deepseek', 'diff content here', '{"summary": "修复及时", "issues": [], "suggestions": "建议添加单元测试"}', 'completed', '2024-12-08 09:15:00', '2024-12-08 09:16:45'),
(3, 'auto', 'qianwen', 'diff content here', '{"summary": "功能完整", "issues": [], "suggestions": "建议优化性能"}', 'processing', '2024-12-12 15:20:00', NULL),
(4, 'auto', 'glm', 'diff content here', '{"summary": "代码重构合理", "issues": [], "suggestions": "无需修改"}', 'failed', '2024-12-10 16:45:00', '2024-12-10 16:45:30'),
(5, 'manual', 'deepseek', 'diff content here', '{"summary": "集成完成", "issues": [{"file": "PaymentService.java", "line": 120, "type": "security", "severity": "medium", "description": "敏感信息可能泄露", "suggestion": "加密存储支付密钥"}]}', 'completed', '2024-12-11 11:20:00', '2024-12-11 11:22:10');

-- 项目2的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(6, 'auto', 'glm', 'diff content here', '{"summary": "功能实现正确", "issues": [], "suggestions": "添加错误处理"}', 'completed', '2024-12-09 10:30:00', '2024-12-09 10:31:20'),
(7, 'auto', 'qianwen', 'diff content here', '{"summary": "bug修复有效", "issues": [], "suggestions": "建议添加回归测试"}', 'completed', '2024-12-09 15:45:00', '2024-12-09 15:46:30'),
(8, 'manual', 'deepseek', 'diff content here', '{"summary": "实现良好", "issues": [], "suggestions": "考虑添加库存预警功能"}', 'completed', '2024-12-12 13:15:00', '2024-12-12 13:17:25');

-- 项目3的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(9, 'auto', 'qianwen', 'diff content here', '{"summary": "模块设计合理", "issues": [], "suggestions": "考虑添加批量操作功能"}', 'completed', '2024-12-10 09:00:00', '2024-12-10 09:01:45'),
(10, 'auto', 'glm', 'diff content here', '{"summary": "权限控制完善", "issues": [], "suggestions": "建议优化数据库查询"}', 'processing', '2024-12-12 16:30:00', NULL);

-- 项目4的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(11, 'auto', 'deepseek', 'diff content here', '{"summary": "注册流程完整", "issues": [], "suggestions": "建议添加邮箱验证功能"}', 'completed', '2024-12-06 11:00:00', '2024-12-06 11:02:00'),
(12, 'auto', 'qianwen', 'diff content here', '{"summary": "好友功能实现良好", "issues": [], "suggestions": "可考虑添加好友分组功能"}', 'completed', '2024-12-07 14:20:00', '2024-12-07 14:21:35'),
(13, 'auto', 'glm', 'diff content here', '{"summary": "动态发布功能完善", "issues": [{"file": "PostController.java", "line": 85, "type": "performance", "severity": "medium", "description": "大图片上传可能影响性能", "suggestion": "压缩图片后再上传"}]}', 'completed', '2024-12-08 10:15:00', '2024-12-08 10:17:00'),
(14, 'manual', 'deepseek', 'diff content here', '{"summary": "bug修复中", "issues": [], "suggestions": "需要进一步测试"}', 'failed', '2024-12-12 17:00:00', '2024-12-12 17:01:00');

-- 项目5的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(15, 'auto', 'glm', 'diff content here', '{"summary": "课程模块设计合理", "issues": [], "suggestions": "考虑添加学习进度跟踪"}', 'completed', '2024-12-11 09:30:00', '2024-12-11 09:32:00');

-- 项目6的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(16, 'auto', 'qianwen', 'diff content here', '{"summary": "编辑器功能完善", "issues": [], "suggestions": "建议添加草稿保存功能"}', 'completed', '2024-12-10 15:00:00', '2024-12-10 15:01:30'),
(17, 'auto', 'deepseek', 'diff content here', '{"summary": "媒体管理实现良好", "issues": [], "suggestions": "可添加批量上传功能"}', 'completed', '2024-12-11 16:20:00', '2024-12-11 16:21:45');

-- 项目7的审核记录
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(18, 'auto', 'glm', 'diff content here', '{"summary": "可视化效果出色", "issues": [], "suggestions": "建议增加更多图表类型"}', 'completed', '2024-12-09 13:00:00', '2024-12-09 13:02:00');

-- ============= 审核规则配置表测试数据 =============
INSERT INTO `review_rule` (`project_id`, `rule_name`, `rule_type`, `rule_pattern`, `rule_description`, `severity`, `is_enabled`) VALUES
(1, '禁止使用console.log', 'code_style', 'console\\.log', '代码中不应该有console.log语句', 'medium', 1),
(1, '密码必须加密', 'security', 'password.*=.*[\'\"]\\w+[\'\"]', '密码不能明文存储', 'critical', 1),
(1, '避免N+1查询', 'performance', 'SELECT.*WHERE.*IN.*SELECT', '避免N+1查询问题', 'high', 1),
(2, '价格不能为负数', 'quality', 'price.*<.*0', '商品价格不能为负数', 'high', 1),
(2, '必须验证库存', 'quality', 'stock.*<=.*0', '下单前必须验证库存', 'high', 1),
(3, '权限检查', 'security', '@PreAuthorize', '所有管理接口必须添加权限检查', 'critical', 1),
(4, 'SQL注入防护', 'security', 'SELECT.*\\+', '避免使用字符串拼接SQL', 'critical', 1),
(4, '输入验证', 'quality', '@Valid', '所有用户输入必须验证', 'medium', 1);

-- 更新审核记录的diff_content为更真实的内容
UPDATE code_review SET diff_content = 'diff --git a/src/main/java/com/example/UserController.java b/src/main/java/com/example/UserController.java
index abc123..def456 100644
--- a/src/main/java/com/example/UserController.java
+++ b/src/main/java/com/example/UserController.java
@@ -20,7 +20,7 @@ public class UserController {
     @GetMapping("/{id}")
     public User getUser(@PathVariable Long id) {
-        return userService.findById(id);
+        User user = userService.findById(id);
+        if (user == null) {
+            throw new RuntimeException("User not found");
+        }
+        return user;
     }
 }' WHERE id = 2;

UPDATE code_review SET diff_content = 'diff --git a/src/main/java/com/example/PaymentService.java b/src/main/java/com/example/PaymentService.java
index xyz789..uvw012 100644
--- a/src/main/java/com/example/PaymentService.java
+++ b/src/main/java/com/example/PaymentService.java
@@ -50,7 +50,7 @@ public class PaymentService {
     public boolean processPayment(Order order) {
-        String privateKey = "sk_test_123456789";
+        String privateKey = getPrivateKeyFromConfig();
         PaymentGateway gateway = new PaymentGateway(privateKey);
         return gateway.pay(order);
     }
+
+    private String getPrivateKeyFromConfig() {
+        return configService.get("payment.private_key");
+    }
 }' WHERE id = 5;

-- 添加更多历史数据（模拟最近几天的数据）
INSERT INTO `code_review` (`pr_id`, `review_type`, `ai_model`, `diff_content`, `review_result`, `status`, `trigger_time`, `complete_time`) VALUES
(1, 'auto', 'glm', 'diff content for PR 1', '{"summary": "代码质量良好", "issues": [], "suggestions": "继续保持"}', 'completed', '2024-12-06 09:00:00', '2024-12-06 09:01:00'),
(1, 'auto', 'qianwen', 'diff content for PR 1', '{"summary": "无问题发现", "issues": [], "suggestions": "无"}', 'completed', '2024-12-06 10:00:00', '2024-12-06 10:01:00'),
(1, 'auto', 'deepseek', 'diff content for PR 1', '{"summary": "实现正确", "issues": [], "suggestions": "建议优化"}', 'completed', '2024-12-06 11:00:00', '2024-12-06 11:01:00'),
(2, 'auto', 'glm', 'diff content for PR 2', '{"summary": "bug修复成功", "issues": [], "suggestions": "添加测试"}', 'completed', '2024-12-07 08:00:00', '2024-12-07 08:01:00'),
(2, 'auto', 'qianwen', 'diff content for PR 2', '{"summary": "修复及时", "issues": [], "suggestions": "无需修改"}', 'completed', '2024-12-07 14:00:00', '2024-12-07 14:01:00'),
(3, 'auto', 'deepseek', 'diff content for PR 3', '{"summary": "功能完整", "issues": [], "suggestions": "性能可优化"}', 'completed', '2024-12-08 13:00:00', '2024-12-08 13:01:00'),
(3, 'auto', 'glm', 'diff content for PR 3', '{"summary": "权限设计合理", "issues": [], "suggestions": "无"}', 'completed', '2024-12-08 15:00:00', '2024-12-08 15:01:00'),
(4, 'auto', 'qianwen', 'diff content for PR 4', '{"summary": "重构成功", "issues": [], "suggestions": "代码清晰"}', 'completed', '2024-12-09 09:00:00', '2024-12-09 09:01:00'),
(5, 'auto', 'deepseek', 'diff content for PR 5', '{"summary": "集成完成", "issues": [], "suggestions": "测试通过"}', 'completed', '2024-12-09 16:00:00', '2024-12-09 16:01:00'),
(6, 'auto', 'glm', 'diff content for PR 6', '{"summary": "实现正确", "issues": [], "suggestions": "功能完善"}', 'completed', '2024-12-10 10:00:00', '2024-12-10 10:01:00'),
(7, 'auto', 'qianwen', 'diff content for PR 7', '{"summary": "修复有效", "issues": [], "suggestions": "建议优化"}', 'completed', '2024-12-10 17:00:00', '2024-12-10 17:01:00'),
(8, 'auto', 'deepseek', 'diff content for PR 8', '{"summary": "管理完善", "issues": [], "suggestions": "功能齐全"}', 'completed', '2024-12-11 11:00:00', '2024-12-11 11:01:00'),
(9, 'auto', 'glm', 'diff content for PR 9', '{"summary": "用户管理", "issues": [], "suggestions": "功能完整"}', 'completed', '2024-12-11 15:00:00', '2024-12-11 15:01:00'),
(10, 'auto', 'qianwen', 'diff content for PR 10', '{"summary": "权限控制", "issues": [], "suggestions": "安全可靠"}', 'completed', '2024-12-12 08:00:00', '2024-12-12 08:01:00'),
(11, 'auto', 'deepseek', 'diff content for PR 11', '{"summary": "注册功能", "issues": [], "suggestions": "实现良好"}', 'completed', '2024-12-12 12:00:00', '2024-12-12 12:01:00'),
(12, 'auto', 'glm', 'diff content for PR 12', '{"summary": "好友系统", "issues": [], "suggestions": "社交完整"}', 'completed', '2024-12-12 13:00:00', '2024-12-12 13:01:00'),
(13, 'auto', 'qianwen', 'diff content for PR 13', '{"summary": "动态发布', "issues": [], "suggestions": "功能正常"}', 'completed', '2024-12-12 14:00:00', '2024-12-12 14:01:00');

-- 更新时间，让数据分布在过去7天
UPDATE code_review SET trigger_time = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY),
                      create_time = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY),
                      complete_time = DATE_ADD(trigger_time, INTERVAL FLOOR(60 + RAND() * 120) SECOND)
WHERE id > 18;

-- 输出统计信息
SELECT 'Projects: ' AS Table, COUNT(*) AS Count FROM project
UNION ALL
SELECT 'Pull Requests: ', COUNT(*) FROM pull_request
UNION ALL
SELECT 'Code Reviews: ', COUNT(*) FROM code_review
UNION ALL
SELECT 'Review Rules: ', COUNT(*) FROM review_rule;

-- 输出各模型使用次数
SELECT 'AI Model Usage: ' AS Type, ai_model, COUNT(*) AS count
FROM code_review
GROUP BY ai_model;

-- 输出项目审核排行榜
SELECT 'Project Ranking: ' AS Type, p.project_name, COUNT(cr.id) AS review_count
FROM project p
LEFT JOIN pull_request pr ON p.id = pr.project_id
LEFT JOIN code_review cr ON pr.id = cr.pr_id
WHERE p.deleted = 0
GROUP BY p.id, p.project_name
ORDER BY review_count DESC;

-- 输出最近7天的审核趋势
SELECT DATE(trigger_time) AS date, COUNT(*) AS count
FROM code_review
WHERE trigger_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY DATE(trigger_time)
ORDER BY date;