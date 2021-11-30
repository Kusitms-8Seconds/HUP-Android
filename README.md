# HUP-App

- 큐시즘 학술제 2조 에잇세컨즈의 HUP!입니다.
- 실시간 경매 중고거래 안드로이드 어플입니다.
<img width="447" alt="itemposter" src="https://user-images.githubusercontent.com/87636557/144025631-72675be4-f3f4-4dca-8511-735ec1117c09.png">

## layout
|스플래쉬|로그인|홈|채팅|
|:-:|:-:|:-:|:-:|
|<img width="236" alt="splash" src="https://user-images.githubusercontent.com/87636557/144022180-6099bea3-591d-4c13-8a1b-82d1d21e5d12.png">|<img width="236" alt="login" src="https://user-images.githubusercontent.com/87636557/144022176-ceb00f2c-47d1-45e1-bf59-c9a0ac1b260d.png">|<img width="236" alt="home" src="https://user-images.githubusercontent.com/87636557/144022165-f8fb7d3e-6745-4741-8fc4-33e301aaa572.png">|<img width="236" alt="chatting" src="https://user-images.githubusercontent.com/87636557/144022149-5ee30d84-d82d-49d5-b65b-bcd8fc5a95fb.png">
|아이템 목록|아이템 상세화면|입찰 화면|아이템 업로드|
|<img width="236" alt="itemlist" src="https://user-images.githubusercontent.com/87636557/144022175-98d1488f-1680-4492-a0a9-fc590d1f3218.png">|<img width="236" alt="itemdetail" src="https://user-images.githubusercontent.com/87636557/144022168-84b8fe7e-d712-4677-ac12-e56536568171.png">|<img width="236" alt="bidpage" src="https://user-images.githubusercontent.com/87636557/144022126-9442e14b-2728-4ed3-8879-72e5776a8ce8.png">|<img width="236" alt="upload" src="https://user-images.githubusercontent.com/87636557/144022183-6a031610-629c-4617-a30f-7b6d37c81de3.png">
|수수료 결제창|결제완료|
|<img width="236" alt="fees" src="https://user-images.githubusercontent.com/87636557/144022153-ebcc134b-e9bc-4958-b0fd-7d72586c1848.png">|<img width="236" alt="finish" src="https://user-images.githubusercontent.com/87636557/144022157-cdc0e02b-1788-4fcc-ae4c-59390f9f31b4.png">


## 개요
- 서버 레포지토리와 안드로이드 레포지토리로 나뉘어져 있습니다.
- 현재 레포지토리는 안드로이드 레포지토리이며 서버 레포지토리는 아래 주소에서 확인 가능합니다.
- [서버 레포지토리](https://github.com/gom1n/HUP-Server)
- 시연 영상 유튜브 업로드 : https://youtu.be/Fv1n-7LECvQ
- apk(release) 경로 : HUP-App\app\release\app-release.apk
- apk(debug) 경로 : HUP-App\app\build\outputs\apk\debug\app-debug.apk
- Android Studio Emulator를 사용하여 테스트 해 보실 경우, 아래 3개의 파일에서 BaseURL IP주소를 http://10.0.2.2:8080/websocket/websocket 로 설정하셔야 합니다.
- AVD가 아닌 실제 기기에 연결하여 테스트 해 보실 경우, 아래 3개의 파일에서 BaseURL IP 주소를 ws://***.***.*.*:8080/websocket/websocket (* 부분은 cmd > ipconfig > Wifi 주소) 로 설정하셔야 합니다.
 1) app/src/main/java/com/example/auctionapp/global/stomp/HupStomp.java
 2) app/src/main/java/com/example/auctionapp/global/retrofit/RetrofitTool.java
 3) app/src/main/java/com/example/auctionapp/domain/user/constant/Constants.java
