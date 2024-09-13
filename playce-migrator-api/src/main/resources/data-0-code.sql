insert into code_category(korean_category_name, english_category_name, description, use_yn) values('타겟 자바 버전','Target Java Version','','Y');
insert into code_category(korean_category_name, english_category_name, description, use_yn) values('타겟 미들웨어 이름','Target Middleware Name','','Y');
insert into code_category(korean_category_name, english_category_name, description, use_yn) values('운영체제','Operating System','','Y');
insert into code_category(korean_category_name, english_category_name, description, use_yn) values('소스 미들웨어 이름','Source Middleware Name','','Y');
insert into code_category(korean_category_name, english_category_name, description, use_yn) values('Deprecated 타입','Deprecated Type','','Y');
insert into code_category(korean_category_name, english_category_name, description, use_yn) values('소스 자바 버전','Source Java Version','','Y');
insert into code_category(korean_category_name, english_category_name, description, use_yn) values('분석처리정책','Analysis Processing Policy','','Y');


insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(1,'JDK11','자바 SE 11','Java SE 11','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(1,'JDK8','자바 SE 8','Java SE 8','Y',3);

insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(2,'TOMCAT','톰캣','Tomcat','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(2,'TOMEE','토미','TomEE','Y',2);


insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(3,'LINUX','리눅스','Linux','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(3,'WINDOWS','윈도우즈','Windows','Y',2);

insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(4,'TOMCAT','톰캣','Tomcat','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(4,'JBOSS','제이보스','JBoss','Y',2);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(4,'JEUS','제우스','Jeus','Y',3);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(4,'WEBLOGIC','웹로직','WebLogic','Y',4);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(4,'WEBSPHERE','웹스피어','WebSphere','Y',5);

insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(5,'CLASS','클래스','Class','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(5,'METHOD','메소드','Method','Y',2);

insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(6,'JDK11','자바 SE 11','Java SE 11','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(6,'JDK9','자바 SE 9','Java SE 9','Y',2);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(6,'JDK8L','자바 SE 8 (8u211 이상)','Java SE 8 (8u211 and later)','Y',3);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(6,'JDK8E','자바 SE 8 (8u211 미만)','Java SE 8 (8u211 and earlier)','Y',4);

insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(7,'CLASS','클래스','Class','Y',1);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(7,'JAR_DEP','Jar 의존성','Jar Dependency','Y',2);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(7,'JAVA','자바','Java','Y',3);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(7,'JSP','자바 서버 페이지','Java Server Page','Y',4);
insert into code_detail(code_category_id, code, korean_code_name, english_code_name, use_yn, display_order) values(7,'PROPERTIES','프로퍼티','Properties','Y',5);