/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.controller;
import cn.zhuatech.attendance.common.ApiResponse;
import cn.zhuatech.attendance.service.PayrollCloseGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/attendance")
public class PayrollCloseGovernanceController {
    private final PayrollCloseGovernanceService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public PayrollCloseGovernanceController(PayrollCloseGovernanceService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/payroll-close")
    public ApiResponse<PayrollCloseGovernanceService.Result> evaluate(
            @Valid @RequestBody PayrollCloseGovernanceService.Request request) { return ApiResponse.ok(service.evaluate(request)); }
}
