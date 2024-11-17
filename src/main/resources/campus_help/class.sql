create table class
(
    id        int auto_increment comment '班级id'
        primary key,
    name      varchar(18) not null comment '班级名',
    school_id int         not null comment '学校id',
    dept_id   int         not null comment '系别id',
    constraint FK_CLASS_DEPT
        foreign key (dept_id) references dept (id),
    constraint FK_CLASS_SCHOOL
        foreign key (school_id) references school (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

