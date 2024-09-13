insert into target(os_name, middleware_name, middleware_version, java_version, containerization_yn, regist_datetime, modify_datetime, regist_login_id, modify_login_id) values
                 ('LINUX', 'TOMCAT', null, 'JDK8L', 'N', now(), now(), 'admin', 'admin');

-- insert into target_analysis_rule_group_link(target_id, analysis_rule_group_id) values (1, 1); -- JSP
-- insert into target_analysis_rule_group_link(target_id, analysis_rule_group_id) values (1, 2); -- JAVA

insert into project(project_name, target_id, regist_datetime, modify_datetime, regist_login_id, modify_login_id) values ('test-project', 1, now(), now(), 'admin', 'admin');
insert into application(target_id, project_id, application_name, origin_path, origin_file_name, application_type, regist_datetime, modify_datetime, regist_login_id, modify_login_id, scm_yn) values
    (1, 1, 'sample', 'test-files/zip', 'sample.zip', 'ZIP', now(), now(), 'admin', 'admin', 'N');
insert into application_overview(application_id, library_names, prepare_process_yn) values (1, '["cart-ejb.jar", "po-ejb-client.jar"]', 'N');
insert into analysis_history(application_id, analysis_history_id, job_status, selected_library_names, regist_datetime, regist_login_id) values
    (1, 1, 'REQ', '["cart-ejb.jar", "po-ejb-client.jar"]', now(), 'admin');

insert into migration_history(job_status, application_id, regist_login_id, regist_datetime) values ('REQ', 1, 'admin', now());
insert into migration_rule_link(migration_history_id, migration_rule_id, migration_condition) values (1, 1, '{"source":"192.168.4.61", "target":"192.168.4.77"}');
insert into migration_rule_link(migration_history_id, migration_rule_id, migration_condition) values (1, 2, '{"source":"roro", "target":"playce-roro"}');



---- last 모든 타켓에 모든 분석룰 그룹 적용.
delete from target_analysis_processing_policy_link;

insert into target_analysis_processing_policy_link(target_id, analysis_processing_policy_id)
select t.target_id
     , app.analysis_processing_policy_id
  from target t
     , analysis_processing_policy app;