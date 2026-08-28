# 企业考勤排班管理系统 API

所有业务接口默认位于 `/api`，除 `/public/**` 和健康检查外均需要 HTTP Basic 身份认证。生产环境应接入企业 IAM 或统一身份平台。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/public/about` | 产品、公司、官网和许可元数据 |
| GET | `/catalog` | 业务模块、字段标签和状态动作 |
| GET | `/dashboard` | 业务规模、金额、状态和模块统计 |
| GET/POST | `/records` | 业务台账查询与创建 |
| GET/PUT/DELETE | `/records/{id}` | 详情、草稿修改与删除 |
| POST | `/records/{id}/actions` | 执行服务端状态迁移 |
| POST | `/records/{id}/comments` | 增加协作记录 |
| GET | `/records/{id}/timeline` | 查询完整操作时间线 |
| GET | `/records/search` | 组合检索、分页和逾期筛选 |
| GET | `/records/export.csv` | 导出 UTF-8 CSV |
| GET | `/sla-summary` | SLA、逾期、风险和人员工作量 |
| POST | `/domain/decision` | 执行企业考勤排班管理系统专属领域规则 |
| GET/POST | `/enterprise/controls` | 企业控制项查询与幂等创建 |
| POST | `/enterprise/controls/{id}/submit` | 提交复核 |
| POST | `/admin/enterprise/controls/{id}/review` | 管理员审批或驳回 |
| POST | `/enterprise/controls/{id}/documents` | 登记附件哈希及存储元数据 |
| POST | `/enterprise/controls/{id}/complete` | 凭证完整后办结 |
| POST | `/admin/enterprise/controls/{id}/sync` | 登记外部系统回执 |

## 领域决策字段

| 字段 | 类型 | 含义 |
| --- | --- | --- |
| `period` | String | 考勤期间 |
| `scheduledMinutes` | int | 应出勤分钟 |
| `workedMinutes` | int | 实际出勤分钟 |
| `approvedLeaveMinutes` | int | 已批假期分钟 |
| `approvedOvertimeMinutes` | int | 已批加班分钟 |
| `unresolvedExceptions` | int | 未处理异常数 |
| `schedulePublished` | boolean | 排班已发布 |
| `employeeConfirmed` | boolean | 员工已确认 |
| `managerApproved` | boolean | 主管已审批 |

接口统一返回 `ApiResponse`；业务冲突使用 HTTP 409，参数错误使用 400，未认证使用 401，无权限使用 403。

## 考勤核心 API

`POST/GET /core/attendance/periods` 管理员工月度台账，`POST/GET /periods/{id}/entries` 管理幂等考勤流水。`/resolve-exception` 处理异常，`/submit` 提交月结；管理员使用 `/admin/core/attendance/periods/{id}/approve` 与 `/close` 审批并绑定薪资批次。`GET /core/attendance/summary` 返回期间汇总。
