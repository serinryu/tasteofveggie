#작성할 SQL 구문 확인용 -> 실제 작업하는 곳 아님

# 댓글 테이블 생성 구문
CREATE TABLE reply(
    reply_id int primary key auto_increment,
    blog_id int not null,
    reply_writer varchar(40) not null,
    reply_content varchar(200) not null,
    published_at datetime default now(),
    updated_at datetime default now()
);

# 외래키 설정
# blog_id 에는 기존에 존재하는 글의 blog_id 만 들어가야 한다.
alter table reply add constraint fk_reply foreign key (blog_id) references blog(blog_id);

#더미데이터 입력용 구문 (테스트 DB 에서만 사용)
INSERT INTO reply VALUES
    (null, 1, "a", "a apple is wow", now(), now()),
    (null, 2, "b", "b buddha is wow", now(), now()),
    (null, 3, "c", "c cider is wow", now(), now())