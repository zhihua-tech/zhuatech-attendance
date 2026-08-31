/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class PayrollCloseGovernanceServiceTest {
    private final PayrollCloseGovernanceService service = new PayrollCloseGovernanceService();
    @Test void allowsPayrollExportAfterFullReconciliation() {
        var result = service.evaluate(new PayrollCloseGovernanceService.Request("2026-08", 120, 120, 0, 0, true));
        assertEquals("READY_TO_CLOSE", result.decision());
        assertTrue(result.payrollExportAllowed());
        assertEquals(100, result.completenessPercent());
    }
    @Test void blocksCloseWhenSourceAndApprovalsAreOpen() {
        var result = service.evaluate(new PayrollCloseGovernanceService.Request("2026-08", 120, 115, 2, 3, false));
        assertEquals("BLOCKED", result.decision());
        assertEquals(4, result.blockers().size());
        assertFalse(result.payrollExportAllowed());
    }
}
