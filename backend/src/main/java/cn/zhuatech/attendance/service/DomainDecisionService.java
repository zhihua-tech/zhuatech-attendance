/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { if(request.workedMinutes()+request.approvedLeaveMinutes()>request.scheduledMinutes()+request.approvedOvertimeMinutes())throw new IllegalArgumentException("出勤与假期合计不能超过应出勤与批准加班合计");int variance=request.scheduledMinutes()-request.workedMinutes()-request.approvedLeaveMinutes();int score=100;List<String> actions=new ArrayList<>();if(!request.schedulePublished()){score-=35;actions.add("发布并冻结正式排班");}if(request.unresolvedExceptions()>0){score-=Math.min(50,request.unresolvedExceptions()*10);actions.add("关闭全部考勤异常");}if(!request.employeeConfirmed()){score-=20;actions.add("取得员工考勤确认");}if(!request.managerApproved()){score-=40;actions.add("完成主管审批");}if(Math.abs(variance)>60){score-=20;actions.add("复核出勤工时差异");}return result(score,actions,"READY_FOR_PAYROLL","REVIEW_REQUIRED","CLOSE_BLOCKED",Map.of("attendanceVarianceMinutes",variance,"approvedOvertimeMinutes",request.approvedOvertimeMinutes(),"openExceptions",request.unresolvedExceptions(),"payableMinutes",request.workedMinutes()+request.approvedLeaveMinutes()+request.approvedOvertimeMinutes())); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @Pattern(regexp="\\d{4}-\\d{2}") String period,
        @Positive int scheduledMinutes,
        @PositiveOrZero int workedMinutes,
        @PositiveOrZero int approvedLeaveMinutes,
        @PositiveOrZero int approvedOvertimeMinutes,
        @PositiveOrZero int unresolvedExceptions,
        boolean schedulePublished,
        boolean employeeConfirmed,
        boolean managerApproved) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
