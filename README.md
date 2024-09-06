# News-Feed
뉴스피드 서버 개발
간단한 SNS 기능을 포함하는 Server API  개발
<br/><br/><br/>

# 팀 소개
![Unity-removebg-preview](https://github.com/user-attachments/assets/60bff1c7-907c-4312-8e9c-be3674b0babf)
- 팀명 : Team Unity
- 팀 소개 : Team Unity는 우리 모두가 서로 협력하면서 하나가 되어 함께 성장하자는 의미 입니다.

<br/><br/><br/>
# 팀원
| 남진현 | 이시우 | 강수민 | 김건우|
|---|---|---|---|
|![남진현](https://github.com/user-attachments/assets/d719ef05-1917-4910-aaa1-d0fe52baf147)|![이시우](https://github.com/user-attachments/assets/814f8e70-601f-4222-b624-3774131dc232)|![강수민](https://github.com/user-attachments/assets/dbad0f9c-2e5c-4bdb-ac74-76e96acfa085)|![김건우](https://github.com/user-attachments/assets/add21073-94be-4b7c-b18d-f93165fd3a8e)|

<br/><br/><br/>
# 팀 노션 바로가기
### [Team Unity 노션 바로가기](https://www.notion.so/teamsparta/Team-Unity-16edc918022b4b3b9170f35a05466f8e)
[![notion-labs-inc-logo-vector](https://github.com/user-attachments/assets/20f54b5c-274a-4b81-ab80-502b5b60b23d)](https://www.notion.so/teamsparta/Team-Unity-16edc918022b4b3b9170f35a05466f8e)


<br/><br/><br/>
# 사용 기술
- DB : MySQL
- Language : Java, SpringBoot
- 배포/관리 : Github
- API Test : Postman

<br/><br/><br/>
# 1. 프로젝트 소개 & 주요 기능

-   프로젝트 명 : 뉴스피드 
-   소개 : 뉴스피드 서버 기능 구현하기
-   기능
    -   프로필 관리
        -   프로필 조회
        -   프로필 수정
    -   게시물 관리
        -   게시물 작성
        -   게시물 전체 조회
        -   게시물 수정
        -   게시물 삭제
    -   사용자 관리
        -   회원가입 기능
        -   회원 탈퇴 기능
    -   친구 관리
        -   친구 추가
        -   친구 수락 대기 목록 조회
        -   친구 수락
        -   친구 목록 조회
        -   친구 삭제

# 2. 와이어 프레임

![image](https://github.com/user-attachments/assets/7a292f39-66cc-4499-b0a7-39aea8e4f663)

# 3. API 명세서
![image](https://github.com/user-attachments/assets/33b7acff-b8a2-4fa2-ab77-8a375e3bbdfd)

# 4. ERD 설계도

![NewsFeed ERD - Page 1 (2)](https://github.com/user-attachments/assets/3e951f5c-fe00-4210-ad69-01fe8eccb3db)


# 6. DB Table SQL
```
CREATE TABLE user (
                      id BIGINT auto_increment primary key ,
                      email VARCHAR(40) not null ,
                      password VARCHAR(80) not null ,
                      user_name VARCHAR(20) not null,
                      is_activated TINYINT(1) not null ,
                      create_at DATETIME not null ,
                      edit_at DATETIME not null 
);

CREATE TABLE post (
                      id BIGINT auto_increment primary key ,
                      title VARCHAR(30) not null ,
                      content VARCHAR(100) not null ,
                      create_at DATETIME not null ,
                      edit_at DATETIME not null ,
                      user BIGINT not null ,
                      FOREIGN KEY (user) REFERENCES user(id)
);

CREATE TABLE friends (
                        id BIGINT auto_increment primary key ,
                        to_user BIGINT not null ,
                        from_user BIGINT not null ,
                        is_accepted TINYINT(1) not null ,
                        request_at DATETIME not null ,
                        FOREIGN KEY (to_user) REFERENCES user(id),
                        FOREIGN KEY (from_user) REFERENCES user(id)
);

CREATE TABLE token(
                    id BIGINT auto_increment primary key ,
                    access_token VARCHAR(255) not null,
                    refresh_token VARCHAR(255) not null,
                    FOREIGN KEY (user) REFERENCES user(id)
);

CREATE TABLE blacklist_token(
                    blacklist_token VARCHAR(255) primary key
);
```

# 7. 클래스 다이어그램

![image](https://github.com/user-attachments/assets/6fe28a79-8e10-4a14-b4b1-ec8c0ba36a07)

# 8. API 로직
![백엔드 로직 drawio (1)](https://github.com/user-attachments/assets/c61dd06d-4dfd-4901-9f54-2066eb3d77d9)


# 9. PosdtMan Document 

### [Team Unity News Feed API Document 보러가기](https://documenter.getpostman.com/view/37567058/2sAXjQ2VsS)

[![Postman](https://github.com/user-attachments/assets/1a88b641-ccac-4385-9b0a-4ad8f7ffbe91)](https://documenter.getpostman.com/view/37567058/2sAXjQ2VsS)

# 10. 프로젝트 시연
### [Team Unity : News Feed 서버 기능 시연 영상](https://www.youtube.com/watch?v=ZBDCZ5RT-5A)
