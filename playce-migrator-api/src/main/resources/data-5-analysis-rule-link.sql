delete from target_analysis_processing_policy_link;

insert into target_analysis_processing_policy_link(target_id, analysis_processing_policy_id)
select t.target_id
     , app.analysis_processing_policy_id
  from target t
     , analysis_processing_policy app;
