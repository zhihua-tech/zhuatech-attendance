/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.service;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class PayrollCloseGovernanceService {
    public Result evaluate(Request request) {
        List<String> blockers = new ArrayList<>();
        int reconciliationGap = Math.max(0, request.expectedEmployees() - request.reconciledEmployees());
        if (!request.sourceLocked()) blockers.add("考勤源数据尚未锁定");
        if (request.managerApprovalsPending() > 0) blockers.add("仍有主管审批未完成");
        if (request.unresolvedExceptions() > 0) blockers.add("仍有未解决考勤异常");
        if (reconciliationGap > 0) blockers.add("人员对账存在缺口: " + reconciliationGap);
        String decision = blockers.isEmpty() ? "READY_TO_CLOSE"
                : !request.sourceLocked() || request.managerApprovalsPending() > 0 ? "BLOCKED" : "RECONCILE";
        int completeness = request.expectedEmployees() == 0 ? 100
                : Math.min(100, request.reconciledEmployees() * 100 / request.expectedEmployees());
        return new Result(request.period(), decision, completeness, reconciliationGap,
                List.copyOf(blockers), blockers.isEmpty());
    }
    public record Request(@NotBlank String period, @Min(0) int expectedEmployees,
                          @Min(0) int reconciledEmployees, @Min(0) int unresolvedExceptions,
                          @Min(0) int managerApprovalsPending, boolean sourceLocked) {
        public Request {
            if (period == null || period.isBlank()) throw new IllegalArgumentException("period is required");
            if (expectedEmployees < 0 || reconciledEmployees < 0 || unresolvedExceptions < 0 || managerApprovalsPending < 0)
                throw new IllegalArgumentException("counts must be non-negative");
            if (reconciledEmployees > expectedEmployees) throw new IllegalArgumentException("reconciledEmployees exceeds expectedEmployees");
        }
    }
    public record Result(String period, String decision, int completenessPercent,
                         int reconciliationGap, List<String> blockers, boolean payrollExportAllowed) {}
}
