-- 用户表
create table user(id varchar(80), name varchar(50), passwd varchar(50), create_date datetime, update_date datetime)

-- 初始化用户信息
insert into user values
('1111', 'wangershuai', 'wes123', now(), now()),
('2222', 'leini', 'lei123', now(), now());


