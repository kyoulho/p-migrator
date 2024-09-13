-- analysis report group
insert into analysis_report_group(analysis_report_group_name) values ('Hard-coded IP Addresses');
insert into analysis_report_group(analysis_report_group_name) values ('Java EE Dependencies');
insert into analysis_report_group(analysis_report_group_name) values ('Lookup Patterns');

insert into analysis_rule_report_group_link(analysis_report_group_id, analysis_rule_id)
select 1
    , analysis_rule_id
  from analysis_rule
 where analysis_rule_name like 'Detect - IP%';

insert into analysis_rule_report_group_link(analysis_report_group_id, analysis_rule_id)
select 2
    , analysis_rule_id
  from analysis_rule
 where analysis_rule_name like 'Check dependency%'
    or analysis_rule_name like 'Check EJB%';

insert into analysis_rule_report_group_link(analysis_report_group_id, analysis_rule_id)
select 3
    , analysis_rule_id
  from analysis_rule
 where analysis_rule_name like 'Check encoding%';

insert into analysis_rule_report_group_link(analysis_report_group_id, analysis_rule_id)
select 3
    , analysis_rule_id
  from analysis_rule
 where analysis_rule_name like 'Finding JSP%';
