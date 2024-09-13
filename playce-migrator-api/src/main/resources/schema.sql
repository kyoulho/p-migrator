
drop table if exists user_access;



drop table if exists analysis_history_detail;



drop table if exists analysis_history;



drop table if exists migration_rule_link;



drop table if exists migration_rule;



drop table if exists application_summary;



drop table if exists application_overview;



drop table if exists code_link;



drop table if exists code_detail;



drop table if exists code_category;



drop table if exists target_analysis_processing_policy_link;



drop table if exists analysis_rule_report_group_link;



drop table if exists analysis_report_group;



drop table if exists deprecated_info;



drop table if exists deleted_info;



drop table if exists dependencies_info;



drop table if exists analysis_rule_group_link;



drop table if exists analysis_rule_group;



drop table if exists analysis_rule;



drop table if exists analysis_processing_policy;



drop table if exists changed_file;



drop table if exists migration_history;



drop table if exists application;



drop table if exists credential;



drop table if exists project;



drop table if exists base_image;



drop table if exists target;



create table target
(
	target_id            bigint auto_increment,
	os_name              varchar(100) null,
	middleware_name      varchar(100) null,
	middleware_version   varchar(100) null,
	java_version         varchar(100) null,
	containerization_yn  varchar(1) not null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null,
	modify_login_id      varchar(20) null,
	modify_datetime      datetime not null
 ,
	primary key (target_id)
);



create unique index ak1_target_target_id on target
(
	os_name,
	middleware_name,
	middleware_version,
	java_version
);



create table base_image
(
	base_image_id        bigint not null,
	image_name           varchar(200) null,
	target_id            bigint not null,
	primary key (base_image_id),
	foreign key fk__base_image_target_id__target_target_id (target_id) references target (target_id) on delete cascade
);



create table project
(
	project_id           bigint auto_increment,
	project_name         text null,
	description          varchar(1024) null,
	target_id            bigint not null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null,
	modify_login_id      varchar(20) null,
	modify_datetime      datetime not null
 ,
	primary key (project_id),
	foreign key fk__project_target_id__target_target_id (target_id) references target (target_id) on delete cascade
);



create table credential
(
	credential_id        bigint auto_increment,
	password_yn          varchar(1) not null,
	credential_username  varchar(20) not null,
	password             varchar(512) null,
	key_content          varchar(4096) null,
	primary key (credential_id)
);



create table application
(
	application_id       bigint auto_increment,
	scm_yn               varchar(1) not null,
	target_id            bigint not null,
	project_id           bigint not null,
	application_name     varchar(100) null,
	origin_path          varchar(512) not null,
	origin_file_name     varchar(200) not null,
	origin_file_size     bigint null,
	description          varchar(1024) null,
	application_type     varchar(10) not null,
	middleware_type      varchar(10) null,
	java_version         varchar(100) null,
	credential_id        bigint null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null,
	modify_login_id      varchar(20) null,
	modify_datetime      datetime not null
 ,
	primary key (application_id),
	foreign key fk__application_target_id__target_target_id (target_id) references target (target_id) on delete cascade,
	foreign key fk__application_project_id__project_project_id (project_id) references project (project_id) on delete cascade,
	foreign key fk__application_credential_id__credential_credential_id (credential_id) references credential (credential_id) on delete cascade
);



create table migration_history
(
	migration_history_id bigint auto_increment,
	compress_result_path varchar(512) null,
	build_result_path    varchar(512) null,
	dockerfile_result_path varchar(512) null,
	docker_image_result_path varchar(512) null,
	job_status           varchar(10) null,
	error_message        varchar(2048) null,
	application_id       bigint not null,
	base_image_name      varchar(200) null,
	dockerfile_config    text null,
	tag_name             varchar(200) null,
	start_datetime       datetime null,
	end_datetime         datetime null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null
 ,
	primary key (migration_history_id),
	foreign key fk__migration_history_application_id__application_application_id (application_id) references application (application_id) on delete cascade
);



create table changed_file
(
	changed_file_id      bigint auto_increment,
	origin_file_absolute_path varchar(512) null,
	file_path_in_result  varchar(512) null,
	migration_history_id bigint not null
 ,
	primary key (changed_file_id),
	foreign key fk__changed_file_migration_history_id__migration_history_migrati (migration_history_id) references migration_history (migration_history_id) on delete cascade
);



create table analysis_processing_policy
(
	analysis_processing_policy_id bigint auto_increment,
	processing_type      varchar(10) null
 ,
	primary key (analysis_processing_policy_id)
);



create unique index ak1_analysis_processing_policy_analysis_processing_policy_id on analysis_processing_policy
(
	analysis_processing_policy_id,
	processing_type
);



create table analysis_rule
(
	analysis_rule_id     bigint auto_increment,
	analysis_rule_name   varchar(100) null,
	importance           varchar(10) null,
	analysis_rule_content text null,
	todo                 varchar(1024) null,
	link                 varchar(512) null,
	description          varchar(1024) null,
	comment              varchar(2048) null,
	delete_yn            varchar(1) not null,
	analysis_processing_policy_id bigint not null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null,
	modify_login_id      varchar(20) null,
	modify_datetime      datetime not null
 ,
	primary key (analysis_rule_id),
	foreign key fk__analysis_rule_analysis_processing_policy_id__analysis_proces (analysis_processing_policy_id) references analysis_processing_policy (analysis_processing_policy_id) on delete cascade
);



create table analysis_rule_group
(
	analysis_rule_group_id bigint auto_increment,
	group_name           varchar(200) null,
	parent_analysis_rule_group_id bigint null
 ,
	primary key (analysis_rule_group_id),
	foreign key fk__analysis_rule_group_parent_analysis_rule_group_id__analysis_ (parent_analysis_rule_group_id) references analysis_rule_group (analysis_rule_group_id) on delete cascade
);



create table analysis_rule_group_link
(
	analysis_rule_id     bigint not null,
	analysis_rule_group_id bigint not null,
	primary key (analysis_rule_id,analysis_rule_group_id),
	foreign key fk__analysis_rule_group_link_analysis_rule_id__analysis_rule_ana (analysis_rule_id) references analysis_rule (analysis_rule_id) on delete cascade,
	foreign key fk__analysis_rule_group_link_analysis_rule_group_id__analysis_ru (analysis_rule_group_id) references analysis_rule_group (analysis_rule_group_id) on delete cascade
);



create table dependencies_info
(
	dependencies_info_id bigint auto_increment,
	sha1_value           varchar(200) null,
	found_at_path        varchar(512) null,
	description          text null,
	application_id       bigint not null
 ,
	primary key (dependencies_info_id),
	foreign key fk__dependencies_info_application_id__application_application_id (application_id) references application (application_id) on delete cascade
);



create table deleted_info
(
	deleted_info_id      bigint auto_increment,
	used_class_name      varchar(200) null,
	deleted_name         varchar(200) null,
	replacement_comment  varchar(200) null,
	application_id       bigint not null
 ,
	primary key (deleted_info_id),
	foreign key fk__deleted_info_application_id__application_application_id (application_id) references application (application_id) on delete cascade
);



create table deprecated_info
(
	deprecated_info_id   bigint auto_increment,
	used_class_name      varchar(200) null,
	deprecated_type      varchar(10) null,
	deprecated_name      varchar(200) null,
	for_removal          varchar(1) not null,
	application_id       bigint not null
 ,
	primary key (deprecated_info_id),
	foreign key fk__deprecated_info_application_id__application_application_id (application_id) references application (application_id) on delete cascade
);



create table analysis_report_group
(
	analysis_report_group_id bigint auto_increment,
	analysis_report_group_name varchar(200) null
 ,
	primary key (analysis_report_group_id)
);



create table analysis_rule_report_group_link
(
	analysis_rule_id     bigint not null,
	analysis_report_group_id bigint not null,
	primary key (analysis_rule_id,analysis_report_group_id),
	foreign key fk__analysis_rule_report_group_link_analysis_rule_id__analysis_r (analysis_rule_id) references analysis_rule (analysis_rule_id) on delete cascade,
	foreign key fk__analysis_rule_report_group_link_analysis_report_group_id__an (analysis_report_group_id) references analysis_report_group (analysis_report_group_id) on delete cascade
);



create table target_analysis_processing_policy_link
(
	target_id            bigint not null,
	analysis_processing_policy_id bigint not null,
	primary key (target_id,analysis_processing_policy_id),
	foreign key fk__target_analysis_processing_policy_link_target_id__target_tar (target_id) references target (target_id) on delete cascade,
	foreign key fk__target_analysis_processing_policy_link_analysis_processing_p (analysis_processing_policy_id) references analysis_processing_policy (analysis_processing_policy_id) on delete cascade
);



create table code_category
(
	code_category_id     bigint auto_increment,
	korean_category_name varchar(100) null,
	english_category_name varchar(200) null,
	description          varchar(1024) null,
	use_yn               varchar(1) not null
 ,
	primary key (code_category_id)
);



create table code_detail
(
	code_detail_id       bigint auto_increment,
	code_category_id     bigint not null,
	code                 varchar(10) null,
	korean_code_name     varchar(100) null,
	english_code_name    varchar(200) null,
	display_order        integer null,
	use_yn               varchar(1) not null
 ,
	primary key (code_detail_id),
	foreign key fk__code_detail_code_category_id__code_category_code_category_id (code_category_id) references code_category (code_category_id) on delete cascade
);



create unique index ak1_code_detail_code_detail_id on code_detail
(
	code,
	code_category_id
);



create table code_link
(
	parent_code_detail_id bigint not null,
	code_detail_id       bigint not null,
	primary key (parent_code_detail_id,code_detail_id),
	foreign key fk__code_link_parent_code_detail_id__code_detail_code_detail_id (parent_code_detail_id) references code_detail (code_detail_id) on delete cascade,
	foreign key fk__code_link_code_detail_id__code_detail_code_detail_id (code_detail_id) references code_detail (code_detail_id) on delete cascade
);



create table application_overview
(
	application_id       bigint not null,
	library_names        text null,
	prepare_process_yn   varchar(1) not null,
	target_file_count    bigint null,
	primary key (application_id),
	foreign key fk__application_overview_application_id__application_application (application_id) references application (application_id) on delete cascade
);



create table application_summary
(
	application_summary_id bigint auto_increment,
	application_id       bigint not null,
	summary_gubun        varchar(500) null,
	file_count           integer null
 ,
	primary key (application_summary_id,application_id),
	foreign key fk__application_summary_application_id__application_application_ (application_id) references application (application_id) on delete cascade
);



create table migration_rule
(
	migration_rule_id    bigint auto_increment,
	migration_rule_name  varchar(100) null,
	file_type            varchar(10) null,
	migration_rule_content text null,
	description          varchar(1024) null,
	delete_yn            varchar(1) not null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null,
	modify_login_id      varchar(20) null,
	modify_datetime      datetime not null
 ,
	primary key (migration_rule_id)
);



create table migration_rule_link
(
	migration_rule_link_id bigint auto_increment,
	migration_history_id bigint not null,
	migration_rule_id    bigint not null,
	migration_condition  text null
 ,
	primary key (migration_rule_link_id),
	foreign key fk__migration_rule_link_migration_history_id__migration_history_ (migration_history_id) references migration_history (migration_history_id) on delete cascade,
	foreign key fk__migration_rule_link_migration_rule_id__migration_rule_migrat (migration_rule_id) references migration_rule (migration_rule_id) on delete cascade
);



create table analysis_history
(
	analysis_history_id  bigint auto_increment,
	job_status           varchar(10) null,
	error_message        varchar(2048) null,
	selected_library_names text null,
	application_id       bigint not null,
	start_datetime       datetime null,
	end_datetime         datetime null,
	regist_login_id      varchar(20) null,
	regist_datetime      datetime not null
 ,
	primary key (analysis_history_id),
	foreign key fk__analysis_history_application_id__application_application_id (application_id) references application (application_id) on delete cascade
);



create table analysis_history_detail
(
	analysis_history_detail_id bigint auto_increment,
	analysis_history_id  bigint not null,
	analysis_line_number integer null,
	analysis_file_path   varchar(512) null,
	analysis_origin_file_path varchar(512) null,
	analysis_file_name   varchar(200) null,
	detected_string      text null,
	override_comment     varchar(2048) null,
	analysis_rule_id     bigint not null
 ,
	primary key (analysis_history_detail_id),
	foreign key fk__analysis_history_detail_analysis_history_id__analysis_histor (analysis_history_id) references analysis_history (analysis_history_id) on delete cascade,
	foreign key fk__analysis_history_detail_analysis_rule_id__analysis_rule_anal (analysis_rule_id) references analysis_rule (analysis_rule_id) on delete cascade
);



create table user_access
(
	login_id             varchar(20) not null,
	user_name            varchar(100) null,
	password             varchar(200) not null,
	primary key (login_id)
);


