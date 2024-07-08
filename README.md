## 💻 프로젝트 소개
MBTI는 2020년대 들어 대한민국의 2030 세대에게 열풍적인 인기를 얻게 된 성격 유형 지표입니다.
<br>
<br>
단순한 인간상 분류뿐만 아니라 하나의 놀이문화로서 대화 주제, 공감대 형성, 서로 간의 이해도를 높이는 주제이기도 합니다.
<br>
<br>
MBTIsland는 이를 주제로 한 커뮤니티 사이트로, 회원마다 개성 및 다양성을 MBTI를 통해 더욱 드러낼 수 있습니다.
<br>
<br>
MBTI를 주제로 한 고유의 기능을 제공하기 위해 개발하게 되었습니다.
<br>
<br>
<br>

## 🗓️ 개발 기간
* 2023.10 ~ 2023.12 (2개월)
<br>
<br>

## 👥 팀원 구성
|이름|역할|개발 내용|
|---|---|-----|
|김한희|FE + BE (팀장)|로그인 및 회원가입, 알림/쪽지, 마이페이지|
|류인수|FE + BE|MBTWhy 게시판, M-Battle 게시판, 신고, 회원 제재|
|유하영|FE + BE|MBTmi 게시판, 메인/헤더/푸터, 관리자 문의, 공지, 배포|
<br>
<br>

## ⚙ 개발 환경
* 언어 : <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS-1572B6?style=flat&logo=css3&logoColor=white"/> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=white"/>
* 서버 : <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat&logo=amazonec2&logoColor=white"/>
* 프레임워크 : <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=springboot&logoColor=white">
* DB : <img src="https://img.shields.io/badge/MariaDB-003545?style=flat&logo=mariadb&logoColor=white">
* IDE : STS3, VSCode, HeidiSQL, Postman
* API, 라이브러리 : RestfulAPI (JSON), Kakao/Naver Login API, QueryDSL, React, Redux, Axios, Recharts, Quill
<br>
<br>

## ✏️ 피그마
[MBTIsland-Figma URL](https://www.figma.com/design/Afu0ZnaOYMVVww6cZbuUZL/MBTIsland?node-id=0-1&t=jETTpPduKKxEr9YW-1)

<br>
<br>

## 💾 ERD
![그림1](https://github.com/hanhee-kim/MBTIsland_back/assets/68523711/798bcb7c-544c-477b-b86f-ced180e14008)
<br>
<br>
<br>

## 🔧 프로젝트 구조
![그림2](https://github.com/hanhee-kim/MBTIsland_back/assets/68523711/1156c81e-66e1-46fd-8697-2e73e578c82c)
<br>
<br>
<br>

## 📃 API 설계
### Alarm
|Description|Method|URI|Return Type|
|---|---|---|---|
|알람 목록 조회|GET|/alarmList|AlarmDto|
|알람 읽음처리|PUT|/updatealarmisread|"읽음처리 성공"|
|알람 일괄 읽음처리|PUT|/updatealarmisreadall|"읽음처리 성공"|
|쪽지/알람 조회|GET|/getnoteandalarm|AlarmDto, NoteDto|
|알람 확인|PUT|/checkalarm/{no}|"읽음처리 성공"|

<br>

### Answer
|Description|Method|URI|Return Type|
|---|---|---|---|
|문의 답글 등록|POST|/answerwrite|Answer|

<br>

### Bookmark
|Description|Method|URI|Return Type|
|---|---|---|---|
|북마크 목록 조회|GET|/mybookmarklist/{username}/{page}|BookmarkDto|
|북마크 삭제|DELETE|/deletebookmark|"삭제 성공"|

<br>

### MBattle
|Description|Method|URI|Return Type|
|---|---|---|---|
|게시판 조회|GET|/mbattle|PageInfo, MbattleDto, Mbattle|
|게시글 상세 조회|GET|/mbattledetail/{no}|Mbattle, MbattleVoter, Boolean, MbattleResult, MbattleResult|
|이미지 출력|GET|/mbattleimg/fileIdx||
|게시글 등록|POST|/mbattlewrite|Integer|
|게시글 삭제|DELETE|/mbattledelete/{no}|"삭제 성공"|
|게시글 북마크/해제|POST|/mbattlebookmark|"북마크/해제 성공"|
|랜덤 게시글 이동|GET|/mbattlerandom|Integer|
|댓글 목록 조회|GET|/mbattlecommentlist/{no}|PageInfo, MbattleComment, Integer|
|댓글 작성|POST|/mbattlecomment|PageInfo, MbattleComment, Integer, Integer|
|댓글 삭제|GET|/mbattlecommentdelete/{commentNo}|"삭제된 댓글입니다"|
|투표|POST|/mbattlevote/{voterMbti}/{vote}|"투표 성공"|
|특정 회원 게시글 목록 조회|GET|/mbattlelistbyuser|PageInfo, Mbattle|
|특정 회원 게시글 선택 삭제|DELETE|/deletembattlelist|"삭제 성공"|

<br>

### MBTmi
|Description|Method|URI|Return Type|
|---|---|---|---|
|주간 인기글 목록 조회|GET|/weeklyhotmbtmi|MbtmiDto|
|게시판 조회|GET|/mbtmilist|PageInfo, MbtmiDto|
|게시글 상세 조회|GET|/mbtmidetail/{no}|Mbtmi, Integer, Boolean, Boolean|
|게시글 삭제|DELETE|/deletembtmi/{no}|"삭제 성공"|
|특정 회원 게시글 선택 삭제|DELETE|/deletembtmilist|"삭제 성공"|
|댓글 목록 조회|GET|/mbtmicommentlist/{no}|PageInfo, MbtmiCommnet|
|댓글 삭제|GET|/deletembtmicomment/{commentNo}|"삭제된 댓글입니다"|
|댓글 등록|POST|/mbtmicomment|PageInfo, MbtmiComment, Integer, Integer|
|게시글 등록|POST|/mbtmiwrite|MBTMI|
|게시글 추천/해제|POST|/mbtmirecommend|Integer|
|게시글 북마크/해제|POST/mbtmibookmark|"북마크/해제 성공"|
|게시글 수정|POST|/mbtmimodify|Mbtmi|
|이미지 없이 게시글 등록|POST|/mbtmiwritewithoutimages|Mbtmi|
|이미지 업로드|POST|/uploadImage|Integer|
|게시글 이미지 인덱스 수정|POST|/updateFileIdxs/{postNo}|Mbtmi|
|게시글 이미지 태그 수정|POST|/mbtmiContainingImgTags/{postNo}|Mbtmi|
|이미지 출력|GET|/mbtmiimg/{fileidx}||
|정지기간 조회|GET|/userbanperiod/{username}|Ban|

<br>

### MBTWhy
|Description|Method|URI|Return Type|
|---|---|---|---|
|게시판 조회|GET|/mbtwhy|PageInfo, MbtwhyDto, MbtwhyDto|
|게시글 상세 조회|GET|/mbtwhydetail|Mbtwhy, Boolean, Boolean|
|게시글 등록|POST|/mbywhywrite|Integer|
|게시글 수정폼 조회|GET|/getmbtwhymodify/{no}|Mbtwhy|
|게시글 수정|POST|/mbtwhymodify/{no}/{content}|"수정 성공"|
|게시글 삭제|DELETE|/mbtwhydelete/{no}|"삭제 성공"|
|게시글 추천/해제|POST|/mbtwhyrecommned|Integer|
|게시글 북마크/해제|POST|/mbtwhybookmark|"북마크/해제 성공"|
|댓글 목록 조회|GET|/mbtwhycommentlist/{no}|PageInfo, MbtwhyComment, Integer|
|댓글 작성|POST|/mbtwhycomment|PageInfo, MbtwhyComment, Integer, Integer|
|댓글 삭제|GET|/mbtwhycommentdelete/{commentNo}|"삭제된 댓글입니다"|
|특정 회원 게시글 목록 조회|GET|/mymbtwhy/{username}/{page}|PageInfo, Mbtwhy|
|특정 회원 게시글 선택 삭제|DELETE|/deletembtwhy|"삭제 성공"|

<br>

### Note
|Description|Method|URI|Return Type|
|---|---|---|---|
|쪽지 작성|POST|/notewrite|"note 등록 성공"|
|쪽지 목록 조회|GET|/notelistofuser|PageInfo, NoteDto|
|쪽지 상세 조회|GET|/notedetail/{noteNo}/{userType}|NoteDto|
|쪽지 일괄 읽음처리|PUT|/updatenoteisreadall|"읽음처리"|
|쪽지 읽음처리|PUT|/readnote|"읽음처리"|

<br>

### Notice
|Description|Method|URI|Return Type|
|---|---|---|---|
|공지사항 목록 조회|GET|/noticelist|PageInfo, Notice, Map<String, Integer>|
|공지사항 일괄 숨김/해제|GET|/hidenotice/{noArr}|"일괄처리 성공"|
|공지사항 일괄 삭제|DELETE|/deletenotice/{noArr}|"일괄삭제 성공"|
|공지사항 상세 조회|GET|/noticedetail/{no}|Notice|
|공지사항 등록|POST|/noticewrite|Notice|
|공지사항 수정|POST|/noticemodify|Notice|

<br>

### Question
|Description|Method|URI|Return Type|
|---|---|---|---|
|문의 목록 조회|GET|/questionlist|PageInfo, Question, Map<String, Integer>|
|문의 등록|POST|/questionwrite|"문의글 등록 성공"|
|문의 상세 조회|GET|/questiondetail/{no}|Question, Answer|

<br>

### Report
|Description|Method|URI|Return Type|
|---|---|---|---|
|신고|POST|/report|"신고 성공"|
|신고 목록 조회|GET|/adminreport/{page}/{filter}/{boardType}|PageInfo, Report|
|신고 목록 조회(정지 페이지 내)|GET|/adminreport/{username}/{page}|PageInfo, Report|
|신고 상세 조회|GET|/reportdetail/{no}|Report|
|이미지 출력|GET|/reportimg/{fileIdx}||
|경고 처리|POST|/reportwarning|"경고 성공"|
|단순 처리 (미경고)|POST|/reportprocess|"처리 성공"|
|정지 목록 조회|GET|/adminban|PageInfo, UserEntity|
|정지 상세 조회|GET|/adminbandetail/{username}|UserEntity|
|정지 해제|POST|/unfreezeban/{username}|"정지 해제 성공"|

<br>

### User
|Description|Method|URI|Return Type|
|---|---|---|---|
|ID 중복 체크|GET|/duplicate/{username}|"사용불가"|
|이메일 인증|GET|/sendmail/{userEmail}|String|
|가입|POST|/join|String|
|메인 페이지 회원 조회|GET|/user|UserEntity|
|회원정보 수정|POST|/user/modify|UserEntity|
|회원 MBTI 수정|GET|/guest/{userMbti}|UserEntity|
|아이디/비밀번호 찾기|POST|/find|String|
|마이페이지 조회|GET|/mypage/{username}|Integer|
|회원 탈퇴|GET|/leaveuser|"탈퇴 성공"|
