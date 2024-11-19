create table notice
(
    notice_id      int auto_increment
        primary key,
    notice_title   varchar(255) null,
    notice_content varchar(255) null,
    create_by      varchar(255) null,
    create_time    varchar(255) null,
    remark         varchar(31)  null
);

