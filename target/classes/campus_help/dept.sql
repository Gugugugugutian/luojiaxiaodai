create table dept
(
    id        int auto_increment comment '系别id'
        primary key,
    name      varchar(18) not null comment '系别名称',
    school_id int         not null comment '学校id',
    constraint FK_DEPT_SCHOOL
        foreign key (school_id) references school (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

