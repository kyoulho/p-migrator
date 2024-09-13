insert into migration_rule(migration_rule_name, file_type, migration_rule_content, description, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Convert source to target string in java file', 'JAVA', 'import io.playce.migrator.dto.migration.MigrationRuleRequest
import io.playce.migrator.dto.migration.MigrationRuleResult
import io.playce.migrator.migration.process.util.CommentUtil
import io.playce.migrator.migration.process.util.ResultUtil
import java.util.Map

rule "Convert source to target string in java file"
    when
        request: MigrationRuleRequest(c: content);
    then
        MigrationRuleResult result = ResultUtil.makeMigrationRuleResult(${ruleId}, request);

        Map<String, String> map = request.getInput();
        String source = map.get("source");
        String target = map.get("target");

        if(c.contains(source)) {
            String newContent = c.replaceAll(source, target);
            result.setContent(newContent);
        } else {
            result.setContent(c);
        }
end;', 'Java 파일에서 문자열을 바꿈.', now(), now(), 'admin', 'admin', 'N');
insert into migration_rule(migration_rule_name, file_type, migration_rule_content, description, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Convert source to target string in xml file', 'XML', 'import io.playce.migrator.dto.migration.MigrationRuleRequest
import io.playce.migrator.dto.migration.MigrationRuleResult
import io.playce.migrator.migration.process.util.CommentUtil
import io.playce.migrator.migration.process.util.ResultUtil
import java.util.Map

rule "Convert source to target string in xml file"
    when
        request: MigrationRuleRequest(c: content);
    then
        MigrationRuleResult result = ResultUtil.makeMigrationRuleResult(${ruleId}, request);

        Map<String, String> map = request.getInput();
        String source = map.get("source");
        String target = map.get("target");

        if(c.contains(source)) {
            String newContent = c.replaceAll(source, target);
            result.setContent(newContent);
        } else {
            result.setContent(c);
        }
end;', 'Xml파일에서 문자열 바꿈', now(), now(), 'admin', 'admin', 'N');
insert into migration_rule(migration_rule_name, file_type, migration_rule_content, description, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Convert source to target string in properties file', 'PROPERTIES', 'import io.playce.migrator.dto.migration.MigrationRuleRequest
import io.playce.migrator.dto.migration.MigrationRuleResult
import io.playce.migrator.migration.process.util.CommentUtil
import io.playce.migrator.migration.process.util.ResultUtil
import java.util.Map

rule "Convert source to target string in properties file"
    when
        request: MigrationRuleRequest(c: content);
    then
        MigrationRuleResult result = ResultUtil.makeMigrationRuleResult(${ruleId}, request);

        Map<String, String> map = request.getInput();
        String source = map.get("source");
        String target = map.get("target");

        if(c.contains(source)) {
            String newContent = c.replaceAll(source, target);
            result.setContent(newContent);
        } else {
            result.setContent(c);
        }
end;', 'Properties 파일에서 문자열 바꿈', now(), now(), 'admin', 'admin', 'N');
insert into migration_rule(migration_rule_name, file_type, migration_rule_content, description, regist_datetime, modify_datetime, regist_login_id, modify_login_id, delete_yn)
     values ('Convert source to target string in jsp file', 'JSP', 'import io.playce.migrator.dto.migration.MigrationRuleRequest
import io.playce.migrator.dto.migration.MigrationRuleResult
import io.playce.migrator.migration.process.util.CommentUtil
import io.playce.migrator.migration.process.util.ResultUtil
import java.util.Map

rule "Convert source to target string in jsp file"
    when
        request: MigrationRuleRequest(c: content);
    then
        MigrationRuleResult result = ResultUtil.makeMigrationRuleResult(${ruleId}, request);

        Map<String, String> map = request.getInput();
        String source = map.get("source");
        String target = map.get("target");

        if(c.contains(source)) {
            String newContent = c.replaceAll(source, target);
            result.setContent(newContent);
        } else {
            result.setContent(c);
        }
end;', 'Jsp 파일에서 문자열 바꿈', now(), now(), 'admin', 'admin', 'N');