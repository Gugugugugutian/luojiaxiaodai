create table user
(
    id          int auto_increment comment '用户id'
        primary key,
    student_id  varchar(16)                         not null comment '学号',
    password    varchar(128)                        not null comment '密码',
    phone       varchar(11)                         null comment '手机号',
    school_id   int                                 not null comment '学校id',
    dept_id     int                                 null comment '系别id',
    class_id    int                                 null comment '班级id',
    sex         int       default 0                 null comment '性别',
    username    varchar(16)                         null comment '用户名',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    balance     double    default 0                 null comment '余额',
    state       int       default 0                 not null comment '状态',
    constraint FK_USER_CLASS
        foreign key (class_id) references class (id),
    constraint FK_USER_DEPT
        foreign key (dept_id) references dept (id),
    constraint FK_USER_SCHOOL
        foreign key (school_id) references school (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

