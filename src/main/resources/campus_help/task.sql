create table task
(
    id              int auto_increment comment '任务id'
        primary key,
    publish_user_id int                                 not null comment '用户发布id',
    accept_user_id  int                                 null comment '接受任务用户id',
    user_school_id  int                                 not null comment '用户所在的学校id',
    reward          double    default 0                 null comment '任务奖励',
    create_time     timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    order_time      timestamp                           null comment '接单时间',
    end_time        timestamp                           null comment '结束时间',
    task_title      varchar(50)                         not null comment '任务标题',
    task_context    varchar(255)                        not null comment '任务内容',
    state           int       default 0                 not null comment '状态',
    constraint FK_TASK_ACCEPT_USER
        foreign key (accept_user_id) references user (id),
    constraint FK_TASK_PUBLISH_USER
        foreign key (publish_user_id) references user (id),
    constraint FK_TASK_USER_SCHOOL
        foreign key (user_school_id) references school (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

