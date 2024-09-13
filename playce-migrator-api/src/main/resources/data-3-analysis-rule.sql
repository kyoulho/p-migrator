insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Finding JSP directives', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Finding JSP directives"
    when
        request: AnalysisRuleRequest(content.contains("<%@") && content.contains("page") && content.contains("%>"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
        
end;', 'A JSP directive was found.', 'Finding JSP directives', 1, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - weblogic.jndi.WLInitialContextFactory', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - weblogic.jndi.WLInitialContextFactory"
    when
        request: AnalysisRuleRequest(content.contains("weblogic.jndi.WLInitialContextFactory"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The weblogic.jndi.WLInitialContextFactory setting exists.', null, 1, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - javax.ejb', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - javax.ejb"
    when
        request: AnalysisRuleRequest(content.contains("javax.ejb"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb setting exists.', null, 1, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - ctx.lookup', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - ctx.lookup"
    when
        request: AnalysisRuleRequest(content.contains("ctx.lookup"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The ctx.lookup setting exists.', null, 1, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Detect - IP Pattern in jsp file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Detect - IP Pattern in jsp file"
    when
        request: AnalysisRuleRequest(content matches ".*[^a-zA-Z]([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3}).*", c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'IP Pattern found.', null, 1, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - EUC-KR in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - EUC-KR in java file"
    when
        request: AnalysisRuleRequest(content.contains("EUC-KR"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check EUC-KR encoding', 'Java 파일에서 EUC-KR 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - euc-kr in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - euc-kr in java file"
    when
        request: AnalysisRuleRequest(content.contains("euc-kr"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check euc-kr encoding', 'Java 파일에서 euc-kr 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - MS949 in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - MS949 in java file"
    when
        request: AnalysisRuleRequest(content.contains("MS949"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check MS949 encoding', 'Java 파일에서 MS949 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - ms949 in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - ms949 in java file"
    when
        request: AnalysisRuleRequest(content.contains("ms949"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check ms949 encoding', 'Java 파일에서 ms949 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - KSC5601 in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - KSC5601 in java file"
    when
        request: AnalysisRuleRequest(content.contains("KSC5601"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check KSC5601 encoding', 'Java 파일에서 KSC5601 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - ksc5601 in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - ksc5601 in java file"
    when
        request: AnalysisRuleRequest(content.contains("ksc5601"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check ksc5601 encoding', 'Java 파일에서 ksc5601 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - UTF- in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - UTF- in java file"
    when
        request: AnalysisRuleRequest(content.contains("UTF-"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check UTF- encoding', 'Java 파일에서 UTF- 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - utf- in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - utf- in java file"
    when
        request: AnalysisRuleRequest(content.contains("utf-"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check utf- encoding', 'Java 파일에서 utf- 인코딩 확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - 8859-1in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - 8859-1in java file"
    when
        request: AnalysisRuleRequest(content.contains("8859-1"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check 8859-1 encoding', 'Java 파일에서 8859-1 인코딩확인', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - weblogic.ch2, Exception in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - weblogic.ch2, Exception in java file"
    when
        request: AnalysisRuleRequest(content.contains("Exception") && content.contains("weblogic.ch2"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The weblogic.ch2 "Exception" exists.', 'Java 파일에서 weblogic.ch2.Exception 종속성 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - javax.transaction in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - javax.transaction in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.transaction"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "javax.transaction" exists.', 'Java 파일에서 transaction 종속성 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - JDBC in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - JDBC in java file"
    when
        request: AnalysisRuleRequest(content.contains("java.sql") || content.contains("javax.sql"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "java.sql" exists.', 'Java 파일에서 JDBC 의존 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Httpservlet inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Httpservlet inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("extends HttpServlet"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The Servlet inheritance exists.', 'Java 파일에서 HttpServlet 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Httpservlet inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Httpservlet inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("extends javax.servlet.http.HttpServlet"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The Servlet inheritance exists.', 'Java 파일에서 HttpServlet 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Httpservlet inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Httpservlet inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("import org.springframework.web.servlet"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The Servlet inheritance exists.', 'Java 파일에서 HttpServlet 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check EJBHome inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check EJBHome inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("extends EJBHome"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The EJBHome inheritance exists.', 'Java 파일에서 EJBHome 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.ejb.EJBHome inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.ejb.EJBHome inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("extends javax.ejb.EJBHome"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb.EJBHome inheritance exists.', 'Java 파일에서 EJBHome 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check EJBObject inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check EJBObject inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("extends EJBObject"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The EJBObject inheritance exists.', 'Java 파일에서 EJBObject 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.ejb.EJBObject inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.ejb.EJBObject inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("extends javax.ejb.EJBObject"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb.EJBObject inheritance exists.', 'Java 파일에서 EJBObject 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check SessionBean inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check SessionBean inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("implements SessionBean"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The SessionBean inheritance exists.', 'Java 파일에서 SessionBeen 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.ejb.SessionBean inheritance in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.ejb.SessionBean inheritance in java file"
    when
        request: AnalysisRuleRequest(content.contains("implements javax.ejb.SessionBean"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb.SessionBean inheritance exists.', 'Java 파일에서 SessionBeen 상속 여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Detect - IP pattern in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Detect - IP pattern in java file"
    when
        request: AnalysisRuleRequest(content matches ".*[^a-zA-Z]([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3}).*", c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'IP Pattern found.', 'Java 파일에서 IP 패턴 존재여부를 확인한다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Middleware properties dependency check - com.bea.weblogic,com.oracle.weblogic', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Middleware properties dependency check - com.bea.weblogic,com.oracle.weblogic"
    when
        request: AnalysisRuleRequest(content.contains("com.bea.weblogic,com.oracle.weblogic"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "com.bea.weblogic,com.oracle.weblogic" setting exists.', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Middleware properties dependency check - com.tmaxsoft', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Middleware properties dependency check - com.tmaxsoft"
    when
        request: AnalysisRuleRequest(content.contains("com.tmaxsoft"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "com.tmaxsoft setting" exists.', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Middleware properties dependency check - TBDriver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Middleware properties dependency check - TBDriver"

    when
        request: AnalysisRuleRequest(content.contains("com.tmax.tibero.jdbc.TbDriver"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "com.tmax.tibero.jdbc.TbDriver" setting exists.', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Middleware properties dependency check - WLInitialContextFactory', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Middleware properties dependency check - WLInitialContextFactory"

    when
        request: AnalysisRuleRequest(content.contains("weblogic.jndi.WLInitialContextFactory"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "weblogic.jndi.WLInitialContextFactory" setting exists.', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Middleware properties dependency check - jdbc url', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Middleware properties dependency check - jdbc url"

    when
        request: AnalysisRuleRequest(content.contains("jdbc:"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "jdbc:" setting exists.', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Middleware properties dependency check - jndi', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Middleware properties dependency check - jndi"

    when
        request: AnalysisRuleRequest(content.contains(".jndi."), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The ".jndi." setting exists.', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Detect - IP Pattern in properties', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Detect - IP Pattern in properties"

    when
        request: AnalysisRuleRequest(content matches ".*[^a-zA-Z]([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3}).*", c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'IP Pattern found', null, 3, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - EUC-KR in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - EUC-KR in class file"
    when
        request: AnalysisRuleRequest(content.contains("EUC-KR"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check EUC-KR encoding', 'Class 파일에서 EUC-KR 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - euc-kr in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - euc-kr in class file"
    when
        request: AnalysisRuleRequest(content.contains("euc-kr"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check euc-kr encoding', 'Class 파일에서 euc-kr 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - MS949 in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - MS949 in class file"
    when
        request: AnalysisRuleRequest(content.contains("MS949"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check MS949 encoding', 'Class 파일에서 MS949 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - ms949 in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - ms949 in class file"
    when
        request: AnalysisRuleRequest(content.contains("ms949"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check ms949 encoding', 'Class 파일에서 ms949 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - KSC5601 in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - KSC5601 in class file"
    when
        request: AnalysisRuleRequest(content.contains("KSC5601"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check KSC5601 encoding', 'Class 파일에서 KSC5601 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - ksc5601 in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - ksc5601 in class file"
    when
        request: AnalysisRuleRequest(content.contains("ksc5601"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check ksc5601 encoding', 'Class 파일에서 ksc5601 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - UTF- in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - UTF- in class file"
    when
        request: AnalysisRuleRequest(content.contains("UTF-"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check UTF- encoding', 'Class 파일에서 UTF- 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - utf- in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - utf- in class file"
    when
        request: AnalysisRuleRequest(content.contains("utf-"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check utf- encoding', 'Class 파일에서 utf- 인코딩 확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check encoding - 8859-1 in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check encoding - 8859-1 in class file"
    when
        request: AnalysisRuleRequest(content.contains("8859-1"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check 8859-1 encoding', 'Class 파일에서 8859-1 인코딩확인', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - weblogic.ch2, Exception in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - weblogic.ch2, Exception in class file"
    when
        request: AnalysisRuleRequest(content.contains("Exception") && content.contains("weblogic.ch2"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The weblogic.ch2 "Exception" exists.', 'Class 파일에서 weblogic.ch2.Exception 종속성 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - javax.transaction in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - javax.transaction in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.transaction"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "javax.transaction" exists.', 'Class 파일에서 transaction 종속성 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check dependency - JDBC in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check dependency - JDBC in class file"
    when
        request: AnalysisRuleRequest(content.contains("java.sql") || content.contains("javax.sql"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The "java.sql" exists.', 'Class 파일에서 sql 종속성 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check HttpServlet inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check HttpServlet inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("extends HttpServlet"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The HttpServlet inheritance exists.', 'Class 파일에서 HttpServlet 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.servlet.http.HttpServlet inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.servlet.http.HttpServlet inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("extends javax.servlet.http.HttpServlet"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.servlet.http.HttpServlet inheritance exists.', 'Class 파일에서 HttpServlet 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check org.springframework.web.servlet inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check org.springframework.web.servlet inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("import org.springframework.web.servlet"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The org.springframework.web.servlet inheritance exists.', 'Class 파일에서 HttpServlet 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check EJBHome inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check EJBHome inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("extends EJBHome"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The EJBHome inheritance exists.', 'Class 파일에서 EJBHome 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.ejb.EJBHome inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.ejb.EJBHome inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("extends javax.ejb.EJBHome"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb.EJBHome inheritance exists.', 'Class 파일에서 EJBHome 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check EJBObject inheritance In class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check EJBObject inheritance In class file"
    when
        request: AnalysisRuleRequest(content.contains("extends EJBObject"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The EJBObject inheritance exists.', 'Class 파일에서 EJBObject 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.ejb.EJBObject inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.ejb.EJBObject inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("extends javax.ejb.EJBObject"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb.EJBObject inheritance exists.', 'Class 파일에서 EJBObject 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check SessionBean inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check SessionBean inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("implements SessionBean"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The SessionBean inheritance exists.', 'Class 파일에서 EJBObject 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check javax.ejb.SessionBean inheritance in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check javax.ejb.SessionBean inheritance in class file"
    when
        request: AnalysisRuleRequest(content.contains("implements javax.ejb.SessionBean"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The javax.ejb.SessionBean inheritance exists.', 'Class 파일에서 EJBObject 상속 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Detect - IP pattern in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Detect - IP pattern in class file"
    when
        request: AnalysisRuleRequest(content matches ".*[^a-zA-Z]([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3})\\\\.([0-9]{1,3}).*", c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'IP Pattern found.', 'Class 파일에서 IP 패턴 존재 여부를 확인한다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Hibernate', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Hibernate"

    when
        request: AnalysisRuleRequest(content.contains("hibernate"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Hibernate framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - HTTP Client', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - HTTP Client"
    when
        request: AnalysisRuleRequest(content.contains("http-client"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a HTTP client.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - JUnit', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - JUnit"
    when
        request: AnalysisRuleRequest(content.contains("junit"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the JUnit framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Swagger', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Swagger"
    when
        request: AnalysisRuleRequest(content.contains("swagger"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Swagger library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Tomcat', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Tomcat"
    when
        request: AnalysisRuleRequest(content.contains("tomcat"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Tomcat framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - XMLUnit', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - XMLUnit"
    when
        request: AnalysisRuleRequest(content.contains("xmlunit"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the XML Unit framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - H2 Driver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - H2 Driver"
    when
        request: AnalysisRuleRequest(content.contains("h2"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds an H2 driver.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Apache Common Logging', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Apache Common Logging"
    when
        request: AnalysisRuleRequest(content.contains("commons-logging"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds an Apache Common Logging library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Apache Common Validator', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Apache Common Validator"
    when
        request: AnalysisRuleRequest(content.contains("commons-validator"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds an Apache Common Validator library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Apache Log4J', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Apache Log4J"
    when
        request: AnalysisRuleRequest(content.contains("log4j"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds an Apache Log4J library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Apache Santuario', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Apache Santuario"
    when
        request: AnalysisRuleRequest(content.contains("xmlsec"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Apache Santuario library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Bouncy Castle', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Bouncy Castle"
    when
        request: AnalysisRuleRequest(content.contains("lcrypto"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Bouncy Castle library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Bouncy Castle', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Bouncy Castle"
    when
        request: AnalysisRuleRequest(content.contains("bcprov"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Bouncy Castle library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Bouncy Castle', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Bouncy Castle"
    when
        request: AnalysisRuleRequest(content.contains("bcpg"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Bouncy Castle library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Bouncy Castle', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Bouncy Castle"
    when
        request: AnalysisRuleRequest(content.contains("bcmail"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Bouncy Castle library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Bouncy Castle', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Bouncy Castle"
    when
        request: AnalysisRuleRequest(content.contains("bcpkix") ,c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Bouncy Castle library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Bouncy Castle', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Bouncy Castle"
    when
        request: AnalysisRuleRequest(content.contains("bctls"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Bouncy Castle library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Geronimo JTA', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Geronimo JTA"
    when
        request: AnalysisRuleRequest(content.contains("geronimo-jta"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Geronimo JTA library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - JBoss logging', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - JBoss logging"
    when
        request: AnalysisRuleRequest(content.contains("jboss-logging"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a JBoss logging library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Logback', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Logback"
    when
        request: AnalysisRuleRequest(content.contains("logback"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Logback library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - OAUTH', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - OAUTH"
    when
        request: AnalysisRuleRequest(content.contains("oauth"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds an OAUTH library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - OWASP ESAPI', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - OWASP ESAPI"
    when
        request: AnalysisRuleRequest(content.contains("esapi"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds an OWASP ESAPI library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - SLJ4J', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - SLJ4J"
    when
        request: AnalysisRuleRequest(content.contains("slf4j"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Simple Logging Facade for Java (SLJ4J) library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - WSDL', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - WSDL"
    when
        request: AnalysisRuleRequest(content.contains("wsdl"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a WSDL library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - Microsoft SQL Driver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - Microsoft SQL Driver"
    when
        request: AnalysisRuleRequest(content.contains("mssql-jdbc"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Microsoft SQL Driver library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - Microsoft SQL Driver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - Microsoft SQL Driver"
    when
        request: AnalysisRuleRequest(content.contains("sqljdbc"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Microsoft SQL Driver library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - MySQL Driver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - MySQL Driver"
    when
        request: AnalysisRuleRequest(content.contains("mysql-connector"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a MySQL Driver.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - Oracle DB Driver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - Oracle DB Driver"
    when
        request: AnalysisRuleRequest(content.contains("ojdbc"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Oracle DB Driver.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - Oracle DB Driver', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - Oracle DB Driver"
    when
        request: AnalysisRuleRequest(content.contains("jodbc"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Oracle DB Driver.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Spring', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Spring"
    when
        request: AnalysisRuleRequest(content.contains("spring"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Spring framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Spring Boot', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Spring Boot"
    when
        request: AnalysisRuleRequest(content.contains("spring-boot"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Spring Boot framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Spring Data', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Spring Data"
    when
        request: AnalysisRuleRequest(content.contains("spring-data"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Spring Data library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Spring DI', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Spring DI"
    when
        request: AnalysisRuleRequest(content.contains("spring-beans"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Spring DI library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Spring Web', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Spring Web"
    when
        request: AnalysisRuleRequest(content.contains("spring-web"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Spring Web framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Spring Messaging Client', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Spring Messaging Client"
    when
        request: AnalysisRuleRequest(content.contains("spring-messaging"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Spring Messaging client library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Spring Messaging Client', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Spring Messaging Client"
    when
        request: AnalysisRuleRequest(content.contains("spring-jms"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Spring Messaging client library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Spring MVC', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Spring MVC"
    when
        request: AnalysisRuleRequest(content.contains("spring-webmvc"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Spring MVC library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Spring Security', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Spring Security"
    when
        request: AnalysisRuleRequest(content.contains("spring-security"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds a Spring Security library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded - Spring Data JPA', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded - Spring Data JPA"
    when
        request: AnalysisRuleRequest(content.contains("spring-data-jpa"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds Spring Data JPA.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Apache CXF', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Apache CXF"
    when
        request: AnalysisRuleRequest(content.contains("cxf"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Apache CXF framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - Apache Geronimo', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - Apache Geronimo"
    when
        request: AnalysisRuleRequest(content.contains("geronimo"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Apache Geronimo framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded framework - AspectJ', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded framework - AspectJ"
    when
        request: AnalysisRuleRequest(content.contains("aspectj"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the AspectJ framework.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Embedded library - Hamcrest', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Embedded library - Hamcrest"
    when
        request: AnalysisRuleRequest(content.contains("hamcrest"), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'The application embeds the Hamcrest library.', null, 5, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - SpringFramework in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - SpringFramework in java file"
    when
        request: AnalysisRuleRequest(content.contains("org.springframework.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the spring framework dependencies', '자바 파일에서 스프링 프레임워크 의존성을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - SpringFramework in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - SpringFramework in class file"
    when
        request: AnalysisRuleRequest(content.contains("org.springframework.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the spring framework dependencies', '클래스 파일에서 스프링 프레임워크 의존성을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - JPA in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - JPA in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.persistence.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check JPA technology dependencies', '자바 파일에서 JPA 기술 의존성을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - JPA in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - JPA in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.persistence") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check JPA technology dependencies', '클래스 파일에서 JPA 기술 의존성을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Spring Data JPA in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Spring Data JPA in java file"
    when
        request: AnalysisRuleRequest(content.contains("org.springframework.data.jpa.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check dependencies to Spring Data JPA', '자바 파일에서 Spring Data JPA에 의존성을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Spring Data JPA in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Spring Data JPA in class file"
    when
        request: AnalysisRuleRequest(content.contains("org.springframework.data.jpa.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check dependencies to Spring Data JPA', '클래스 파일에서 Spring Data JPA에 의존성을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - MyBatis in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - MyBatis in java file"
    when
        request: AnalysisRuleRequest(content.contains("org.apache.ibatis.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the MyBatis Mapper Interface', '자바 파일에서 MyBatis Mapper Interface을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - MyBatis in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - MyBatis in class file"
    when
        request: AnalysisRuleRequest(content.contains("org.apache.ibatis.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the Mybatis Mapper interface', '클래스 파일에서 MyBatis Mapper Interface을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - AspectJ in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - AspectJ in java file"
    when
        request: AnalysisRuleRequest(content.contains("org.aspectj.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the AspectJ technology', '자바 파일에서 AspectJ 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - AspectJ in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - AspectJ in class file"
    when
        request: AnalysisRuleRequest(content.contains("org.aspectj.") && (content.contains("import") || content.contains("@") ), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the AspectJ technology', '클래스 파일에서 AspectJ 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - WebSocket in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - WebSocket in java file"
    when
        request: AnalysisRuleRequest(content.contains("import javax.websocket."), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the WebSocket technology', '자바 파일에서 WebSocket 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - WebSocket in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - WebSocket in class file"
    when
        request: AnalysisRuleRequest(content.contains("import javax.websocket."), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check the WebSocket technology', '클래스 파일에서 WebSocket 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Common Annotations in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Common Annotations in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.annotation.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Common Annotations technology', '자바 파일에서 Common Annotations 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Common Annotations in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Common Annotations in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.annotation.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Common Annotations technology', '클래스 파일에서 Common Annotations 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Mail in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Mail in java file"
    when
        request: AnalysisRuleRequest(content.contains("import javax.mail."), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Mail skills', '자바 파일에서 Mail 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Mail in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Mail in class file"
    when
        request: AnalysisRuleRequest(content.contains("import javax.mail."), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Mail skills', '클래스 파일에서 Mail 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - JCA in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - JCA in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.resource.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check JCA technology', '자바 파일에서 JCA 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - JCA in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - JCA in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.resource.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check JCA technology', '클래스 파일에서 JCA 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - JAX-RS in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - JAX-RS in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.ws.rs.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check JAX-RS technology', '자바 파일에서 JAX-RS 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - JAX-RS in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - JAX-RS in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.ws.rs.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check JAX-RS technology', '클래스 파일에서 JAX-RS 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Bean Validation in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Bean Validation in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.validation.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Bean Validation technology', '자바 파일에서 Bean Validation 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Bean Validation in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Bean Validation in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.validation.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Bean Validation technology', '클래스 파일에서 Bean Validation 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Java EE Batch in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Java EE Batch in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.batch.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Java EE Batch technology', '자바 파일에서 Java EE Batch 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Java EE Batch in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Java EE Batch in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.batch.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Java EE Batch technology', '클래스 파일에서 Java EE Batch 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Java EE JSON-P in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Java EE JSON-P in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.json.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Java EE JSON-P technology', '자바 파일에서 Java EE JSON-P 기술을 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - Java EE JSON-P in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - Java EE JSON-P in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.json.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check Java EE JSON-P technology', '클래스 파일에서 Java EE JSON-P 기술을 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - CDI in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - CDI in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.inject.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check whether the javax.inject package', '자바 파일에서 javax.inject 패키지 여부를 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - CDI in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - CDI in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.inject.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check whether the javax.inject package', '클래스 파일에서 javax.inject 패키지 여부를 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - CDI in java file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - CDI in java file"
    when
        request: AnalysisRuleRequest(content.contains("javax.enterprise.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check whether the javax.enterprise', '자바 파일에서 javax.enterprise 패키지 여부를 확인합니다.', 2, now(), now(), 'admin', 'admin', 'N');
insert into analysis_rule(analysis_rule_name, analysis_rule_content, comment, description, analysis_processing_policy_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Check Technology - CDI in class file', 'import io.playce.migrator.dto.analysis.AnalysisRuleRequest
import io.playce.migrator.dto.analysis.AnalysisRuleResult
import io.playce.migrator.migration.process.util.ResultUtil
 
rule "Check Technology - CDI in class file"
    when
        request: AnalysisRuleRequest(content.contains("javax.enterprise.") && (content.contains("import") || content.contains("@")), c: content)
    then
        AnalysisRuleResult result = ResultUtil.makeAnalysisRuleResult(${ruleId}, request);
        result.setDetectedString(c);
end;', 'Check whether the javax.enterprise', '클래스 파일에서 javax.enterprise 패키지 여부를 확인합니다.', 4, now(), now(), 'admin', 'admin', 'N');