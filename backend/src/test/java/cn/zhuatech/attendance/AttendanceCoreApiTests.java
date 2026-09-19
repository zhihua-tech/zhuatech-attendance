/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.attendance;import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;import org.springframework.http.MediaType;import org.springframework.test.web.servlet.*;import java.util.regex.Pattern;import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@SpringBootTest@AutoConfigureMockMvc class AttendanceCoreApiTests{@Autowired MockMvc mvc;
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void monthlyAttendanceRunsThroughLedgerSubmitApproveAndPayrollClose()throws Exception{long id=create("EMP-CORE-1",480);entry(id,"a1","WORK",420);long request=request(id,"REQ-CORE-1","LEAVE",60);review(request,"APPROVE","主管批准年假").andExpect(jsonPath("$.data.status").value("APPROVED"));post(id,"submit",null,"operator").andExpect(jsonPath("$.data.status").value("SUBMITTED"));post(id,"approve","{\"remark\":\"主管复核通过\"}","admin").andExpect(jsonPath("$.data.status").value("APPROVED"));post(id,"close","{\"payrollBatchNo\":\"PAY-2026-08\"}","admin").andExpect(jsonPath("$.data.status").value("CLOSED"));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void unresolvedExceptionBlocksMonthCloseSubmission()throws Exception{long id=create("EMP-CORE-2",480);entry(id,"b1","WORK",480);entry(id,"b2","EXCEPTION",0);post(id,"submit",null,"operator").andExpect(status().isConflict());post(id,"resolve-exception","{\"remark\":\"补签审批通过\"}","operator").andExpect(jsonPath("$.data.openExceptions").value(0));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void entryIdempotencyPreventsDoubleCounting()throws Exception{long id=create("EMP-CORE-3",480);entry(id,"same-att","WORK",480);entry(id,"same-att","WORK",480);mvc.perform(get("/api/core/attendance/summary").param("period","2026-08").with(httpBasic("operator","operator123"))).andExpect(status().isOk()).andExpect(jsonPath("$.data.workedMinutes").isNumber());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void operatorCannotApprovePayrollResults()throws Exception{long id=create("EMP-CORE-4",480);entry(id,"d1","WORK",480);post(id,"submit",null,"operator");post(id,"approve","{\"remark\":\"越权\"}","operator").andExpect(status().isForbidden());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void materialWorkHourVarianceIsRejected()throws Exception{long id=create("EMP-CORE-5",480);entry(id,"e1","WORK",200);post(id,"submit",null,"operator").andExpect(status().isConflict());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void leaveAndOvertimeCannotBypassApprovalWorkflow()throws Exception{long id=create("EMP-CORE-6",480);mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/core/attendance/periods/"+id+"/entries").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"idempotencyKey\":\"bypass-leave\",\"type\":\"LEAVE\",\"minutes\":60,\"referenceNo\":\"LEAVE-X\"}")).andExpect(status().isBadRequest());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void pendingAttendanceRequestBlocksPeriodSubmission()throws Exception{long id=create("EMP-CORE-7",480);entry(id,"g1","WORK",480);long request=request(id,"REQ-CORE-7","OVERTIME",120);post(id,"submit",null,"operator").andExpect(status().isConflict());review(request,"REJECT","证据不足").andExpect(jsonPath("$.data.status").value("REJECTED"));post(id,"submit",null,"operator").andExpect(status().isOk());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void attendanceRequestReviewRequiresAdministrator()throws Exception{long id=create("EMP-CORE-8",480);long request=request(id,"REQ-CORE-8","LEAVE",60);mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/admin/core/attendance/requests/"+request+"/review").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"decision\":\"APPROVE\",\"remark\":\"越权\"}")).andExpect(status().isForbidden());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private long create(String emp,int scheduled)throws Exception{MvcResult r=mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/core/attendance/periods").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"employeeNo\":\""+emp+"\",\"employeeName\":\"测试员工\",\"period\":\"2026-08\",\"scheduledMinutes\":"+scheduled+"}")).andExpect(status().isOk()).andReturn();var m=Pattern.compile("\\\"id\\\":(\\d+)").matcher(r.getResponse().getContentAsString());m.find();return Long.parseLong(m.group(1));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private void entry(long id,String key,String type,int minutes)throws Exception{mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/core/attendance/periods/"+id+"/entries").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"idempotencyKey\":\""+key+"\",\"type\":\""+type+"\",\"minutes\":"+minutes+",\"referenceNo\":\"REF-"+key+"\"}")).andExpect(status().isOk());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private long request(long id,String no,String type,int minutes)throws Exception{MvcResult r=mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/core/attendance/periods/"+id+"/requests").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"requestNo\":\""+no+"\",\"type\":\""+type+"\",\"minutes\":"+minutes+",\"reason\":\"业务申请\"}")).andExpect(status().isOk()).andReturn();var m=Pattern.compile("\\\"id\\\":(\\d+)").matcher(r.getResponse().getContentAsString());m.find();return Long.parseLong(m.group(1));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private ResultActions review(long id,String decision,String remark)throws Exception{return mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/admin/core/attendance/requests/"+id+"/review").with(httpBasic("admin","admin123")).contentType(MediaType.APPLICATION_JSON).content("{\"decision\":\""+decision+"\",\"remark\":\""+remark+"\"}"));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private ResultActions post(long id,String action,String body,String user)throws Exception{var b=org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post((action.equals("approve")||action.equals("close")?"/api/admin":"/api")+"/core/attendance/periods/"+id+"/"+action).with(httpBasic(user,user.equals("admin")?"admin123":"operator123"));if(body!=null)b.contentType(MediaType.APPLICATION_JSON).content(body);return mvc.perform(b);}
}
