# 스프링부트 블로그 만들기

## 기술스택

- Springboot 2.7
- JDK 11
- VSCODE
- MySQL8.0

## 의존성

- Lombok
- DevTools
- Spring WEB
- JPA
- h2
- MySQL
- Mustache

## 프로젝트 시작

```sql
create database blogdb;
```

## 댓글 상세보기 한방쿼리

```sql
select
b.id board_id,
b.content board_content,
b.title board_title,
b.user_id board_user_id,
r.id reply_id,
r.comment reply_comment,
r.user_id reply_user_id,
ru.username reply_user_username,
case when r.user_id = 2 then true else false end reply_owner
from board_tb b left outer join reply_tb r
on b.id = r.board_id
left outer join user_tb ru
on r.user_id = ru.id
where b.id = 1;
```
