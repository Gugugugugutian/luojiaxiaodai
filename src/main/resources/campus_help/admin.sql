create table admin
(
    id          int auto_increment
        primary key,
    account     varchar(16)                         not null comment '账号',
    password    varchar(16)                         not null comment '密码',
    username    varchar(16)                         not null comment '用户名',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    state       int       default 0                 not null comment '状态'
)
    charset = utf8mb3
    row_format = DYNAMIC;

