# 💰 HUP-App
<div align="center"><h2>중고거래에 실시간의 재미를 더하다, </br> HURRY UP! HUP! 💨 </h2></div>
<p align="center"><img src="https://user-images.githubusercontent.com/61726631/149874766-fcb10202-e727-4841-bfa4-2ebddc515b8d.jpg" width="400" height="400"/><p>

  
## :raising_hand: Introduce

- 큐시즘 학술제 2조 에잇세컨즈의 HUP!입니다.
- 경매에 대한 경험이 부족하고 경매에 부담을 갖고 있는 z세대들에게 더 쉽게 다가갈 수 있는 방안이 필요했습니다.</br>
따라서 중고거래의 즐거움을 높이고 + 경매 진입장벽은 낮추고 + 실시간 거래는 빠른, HUP을 개발했습니다.
- 서버(spring) 레포지토리와 안드로이드 레포지토리로 나뉘어져 있습니다.
- 현재 레포지토리는 안드로이드 레포지토리이며 서버 레포지토리는 아래 주소에서 확인 가능합니다.
- [서버 레포지토리](https://github.com/Kusitms-8Seconds/HUP-Server)

## :mag_right: ItemPoster

 <p align="center"><img src="https://user-images.githubusercontent.com/61726631/149875042-4a0d2719-c8c3-48b9-905d-693234d99310.png"/><p>
  
## ✨ layout
|스플래쉬|로그인|홈|채팅|
|:-:|:-:|:-:|:-:|
|<img width="236" alt="splash" src="https://user-images.githubusercontent.com/87636557/144022180-6099bea3-591d-4c13-8a1b-82d1d21e5d12.png">|<img width="236" alt="login" src="https://user-images.githubusercontent.com/87636557/144022176-ceb00f2c-47d1-45e1-bf59-c9a0ac1b260d.png">|<img width="236" alt="home" src="https://user-images.githubusercontent.com/87636557/144022165-f8fb7d3e-6745-4741-8fc4-33e301aaa572.png">|<img width="236" alt="chatting" src="https://user-images.githubusercontent.com/87636557/144022149-5ee30d84-d82d-49d5-b65b-bcd8fc5a95fb.png">
|아이템 목록|아이템 상세화면|입찰 화면|아이템 업로드|
|<img width="236" alt="itemlist" src="https://user-images.githubusercontent.com/87636557/144022175-98d1488f-1680-4492-a0a9-fc590d1f3218.png">|<img width="236" alt="itemdetail" src="https://user-images.githubusercontent.com/87636557/144022168-84b8fe7e-d712-4677-ac12-e56536568171.png">|<img width="236" alt="bidpage" src="https://user-images.githubusercontent.com/87636557/144022126-9442e14b-2728-4ed3-8879-72e5776a8ce8.png">|<img width="236" alt="upload" src="https://user-images.githubusercontent.com/87636557/144022183-6a031610-629c-4617-a30f-7b6d37c81de3.png">
|수수료 결제창|결제완료|공지사항|아이콘|
|<img width="236" alt="fees" src="https://user-images.githubusercontent.com/87636557/144022153-ebcc134b-e9bc-4958-b0fd-7d72586c1848.png">|<img width="236" alt="finish" src="https://user-images.githubusercontent.com/87636557/144022157-cdc0e02b-1788-4fcc-ae4c-59390f9f31b4.png">|<img width="236" alt="notice" src="https://user-images.githubusercontent.com/87636557/144022178-4bab2ea2-8927-4b9f-9905-734912a5033f.png">|<img width="236" alt="hupicon" src="https://user-images.githubusercontent.com/87636557/144022250-0e5547ce-fade-40dc-9de3-4fa7f220340c.png">

## 👩🏻‍💻 참여자 🧑🏻‍💻
|이름|파트|레포|
|---|---|---|
|고민채|안드로이드|[이동하기](https://github.com/gom1n)|  
|김정우|서버(스프링)|[이동하기](https://github.com/friendshipkim97)|
  
## foldering
  MVP패턴을 도입해, model / view / presenter 의 형태로 폴더링되어있습니다.
  
## 라이브러리 & APIs
✅ __retrofit__ : HTTP통신을 통해 데이터를 가져오거나 수정 및 보냅니다. </br>
✅ __websocket, STOMP__ : 실시간 채팅 및 경매 입찰, 낙찰을 담당합니다. </br>
✅ __FCM (firebase messaging)__ : 푸시알림을 통해 낙찰 시 판매자와 낙찰자의 기기에 알람을 보냅니다. </br>
✅ __SQLite__ : 검색결과 저장 및 삭제 </br>
✅ __google / naver / kakao__ : 구글, 네이버, 카카오 소셜 로그인 구현 및 사용자 정보 연동 </br>

## 협업
1. Github
2. Slack
3. Jira <img src="https://user-images.githubusercontent.com/87636557/159142524-b4b1bbcc-30bf-400b-8293-29d813fad101.png"/>

## :books: HUP API Docs
<a href="http://www.hurryuphup.me/swagger-ui/index.html#/">HUP Swagger REST API Docs</a><br>
<a href="https://expensive-cowbell-87c.notion.site/0a16957c3cdb42ccb9681d16ee1311b9?v=3d543e845738431bb16e11ca359ffede">HUP STOMP API Docs</a><br>

