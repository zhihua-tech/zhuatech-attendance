/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class PayrollCloseGovernanceServiceTest {
    private final PayrollCloseGovernanceService service = new PayrollCloseGovernanceService();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void allowsPayrollExportAfterFullReconciliation() {
        var result = service.evaluate(new PayrollCloseGovernanceService.Request("2026-08", 120, 120, 0, 0, true));
        assertEquals("READY_TO_CLOSE", result.decision());
        assertTrue(result.payrollExportAllowed());
        assertEquals(100, result.completenessPercent());
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksCloseWhenSourceAndApprovalsAreOpen() {
        var result = service.evaluate(new PayrollCloseGovernanceService.Request("2026-08", 120, 115, 2, 3, false));
        assertEquals("BLOCKED", result.decision());
        assertEquals(4, result.blockers().size());
        assertFalse(result.payrollExportAllowed());
    }
}
