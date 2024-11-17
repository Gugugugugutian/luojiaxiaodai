create table school
(
    id   int auto_increment comment '学校id'
        primary key,
    name varchar(18) not null comment '学校名称'
)
    charset = utf8mb3
    row_format = DYNAMIC;

