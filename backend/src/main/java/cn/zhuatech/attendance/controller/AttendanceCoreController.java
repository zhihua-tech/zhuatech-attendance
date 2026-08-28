/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance.controller;import cn.zhuatech.attendance.common.ApiResponse;import cn.zhuatech.attendance.service.AttendanceCoreService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController public class AttendanceCoreController{private final AttendanceCoreService service;public AttendanceCoreController(AttendanceCoreService service){this.service=service;}
 @PostMapping("/api/core/attendance/periods")ApiResponse<AttendanceCoreService.AttendancePeriod>create(@Valid@RequestBody AttendanceCoreService.CreatePeriodRequest r){return ApiResponse.ok(service.create(r));}
 @GetMapping("/api/core/attendance/periods")ApiResponse<List<AttendanceCoreService.AttendancePeriod>>list(@RequestParam(required=false)String period,@RequestParam(required=false)String status){return ApiResponse.ok(service.periods(period,status));}
 @GetMapping("/api/core/attendance/periods/{id}/entries")ApiResponse<List<AttendanceCoreService.AttendanceEntry>>entries(@PathVariable Long id){return ApiResponse.ok(service.entries(id));}
 @PostMapping("/api/core/attendance/periods/{id}/entries")ApiResponse<AttendanceCoreService.AttendanceEntry>entry(@PathVariable Long id,@Valid@RequestBody AttendanceCoreService.EntryRequest r){return ApiResponse.ok(service.addEntry(id,r));}
 @PostMapping("/api/core/attendance/periods/{id}/resolve-exception")ApiResponse<AttendanceCoreService.AttendancePeriod>resolve(@PathVariable Long id,@Valid@RequestBody AttendanceCoreService.RemarkRequest r){return ApiResponse.ok(service.resolveException(id,r));}
 @PostMapping("/api/core/attendance/periods/{id}/submit")ApiResponse<AttendanceCoreService.AttendancePeriod>submit(@PathVariable Long id){return ApiResponse.ok(service.submit(id));}
 @PostMapping("/api/admin/core/attendance/periods/{id}/approve")ApiResponse<AttendanceCoreService.AttendancePeriod>approve(@PathVariable Long id,@Valid@RequestBody AttendanceCoreService.RemarkRequest r){return ApiResponse.ok(service.approve(id,r));}
 @PostMapping("/api/admin/core/attendance/periods/{id}/close")ApiResponse<AttendanceCoreService.AttendancePeriod>close(@PathVariable Long id,@Valid@RequestBody AttendanceCoreService.CloseRequest r){return ApiResponse.ok(service.close(id,r));}
 @GetMapping("/api/core/attendance/summary")ApiResponse<AttendanceCoreService.MonthlySummary>summary(@RequestParam String period){return ApiResponse.ok(service.summary(period));}
}
