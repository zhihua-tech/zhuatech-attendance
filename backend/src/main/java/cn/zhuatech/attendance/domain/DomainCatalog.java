/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.domain;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class DomainCatalog {
    private final Map<String, WorkflowAction> actions = new LinkedHashMap<>();
    public DomainCatalog() {
        actions.put("SUBMIT", new WorkflowAction("SUBMIT", "提交考勤月结", List.of("草稿"), "待复核", "OPERATOR"));
        actions.put("APPROVE", new WorkflowAction("APPROVE", "批准考勤结果", List.of("待复核"), "待结转", "ADMIN"));
        actions.put("CLOSE", new WorkflowAction("CLOSE", "锁定并结转薪资", List.of("待结转"), "已月结", "ADMIN"));
    }
    public String systemName() { return "知华科技企业考勤排班管理系统"; }
    public String scene() { return "考勤组、班次、排班、打卡、请假、出差、加班、异常、工时、月结与薪资对接"; }
    public String initialStatus() { return "草稿"; }
    public String partyLabel() { return "员工/部门/考勤组"; }
    public String amountLabel() { return "人工成本"; }
    public String quantityLabel() { return "出勤工时"; }
    public String dueLabel() { return "考勤月结日期"; }
    public List<ModuleDefinition> modules() { return List.of(
            new ModuleDefinition("ATTENDANCE_GROUP", "考勤组", "按法人、地点、部门、岗位配置日历和打卡规则"),
            new ModuleDefinition("SHIFT", "班次规则", "设置弹性、跨天、轮班、休息、迟到和早退规则"),
            new ModuleDefinition("SCHEDULE", "智能排班", "支持周期班次、批量排班、换班和冲突检查"),
            new ModuleDefinition("CLOCK", "打卡记录", "接收移动端、门禁、考勤机和补签数据"),
            new ModuleDefinition("LEAVE", "休假出差", "管理假期额度、请假、出差、销假和审批结果"),
            new ModuleDefinition("OVERTIME", "加班管理", "执行申请、审批、调休或加班费结转"),
            new ModuleDefinition("EXCEPTION", "异常处理", "闭环缺卡、迟到、早退、跨区和设备异常"),
            new ModuleDefinition("TIMESHEET", "工时核算", "按项目、成本中心和工作日历汇总有效工时"),
            new ModuleDefinition("MONTHLY_CLOSE", "考勤月结", "执行确认、锁定、重开、薪资推送和审计")
        ); }
    public Map<String, WorkflowAction> actions() { return Collections.unmodifiableMap(actions); }
    public record ModuleDefinition(String code,String name,String description) {}
    public record WorkflowAction(String code,String label,List<String> from,String to,String requiredRole) {}
}
