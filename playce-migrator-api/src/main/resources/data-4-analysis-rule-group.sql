insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Servlet', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('EJB', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Spring', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('JPA', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('MyBatis', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('AspectJ', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('JDBC', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('WebSocket', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Common Annotations', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Mail', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('JCA', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('JAX-RS', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Bean Validation', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Java EE Batch', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('Java EE JSON-P', null);
insert into analysis_rule_group (group_name, parent_analysis_rule_group_id) values ('CDI', null);

-- Servlet --
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (1,18);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (1,19);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (1,20);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (1,47);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (1,48);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (1,49);

-- EJB --
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,3);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,21);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,22);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,23);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,24);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,25);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,26);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,50);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,51);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,52);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,53);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,54);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (2,55);

-- Spring --
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (3,100);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (3,101);

-- JPA --
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (4,102);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (4,103);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (4,104);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (4,105);

-- MyBatis--
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (5,106);
insert into analysis_rule_group_link(analysis_rule_group_id,analysis_rule_id) values (5,107);

-- AspectJ --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (6,108);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (6,109);

-- JDBC --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (7,17);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (7,46);

-- WebSocket --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (8,110);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (8,111);

-- Common Annotations --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (9,112);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (9,113);

-- mail --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (10,114);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (10,115);

-- JCA --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (11,116);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (11,117);

-- JAX-RS --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (12,118);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (12,119);

-- Bean Validation --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (13,120);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (13,121);

-- Java EE Batch --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (14,122);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (14,123);

-- Java EE JSON-P --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (15,124);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (15,125);

-- CDI --
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (16,126);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (16,127);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (16,128);
insert into analysis_rule_group_link(analysis_rule_group_id, analysis_rule_id) values (16,129);
