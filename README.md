# HUP-App

## 개요

- 큐시즘 학술제 2조 에잇세컨즈의 HUP!입니다.
- 서버 레포지토리와 안드로이드 레포지토리로 나뉘어져 있습니다.
- 현재 레포지토리는 안드로이드 레포지토리이며 서버 레포지토리는 아래 주소에서 확인 가능합니다.
- [서버 레포지토리](https://github.com/Kusitms-8Seconds/HUP-Server)
- 혹시 모를 상황에 대비한 시연 영상 유튜브 업로드 : https://youtu.be/Fv1n-7LECvQ
- apk(release) 경로 : HUP-App\app\release\app-release.apk
- apk(debug) 경로 : HUP-App\app\build\outputs\apk\debug\app-debug.apk
- Android Studio Emulator를 사용하여 테스트 해 보실 경우, 아래 3개의 파일에서 BaseURL IP주소를 http://10.0.2.2:8080/websocket/websocket 로 설정하셔야 합니다.
- AVD가 아닌 실제 기기에 연결하여 테스트 해 보실 경우, 아래 3개의 파일에서 BaseURL IP 주소를 ws://***.***.*.*:8080/websocket/websocket (* 부분은 cmd > ipconfig > Wifi 주소) 로 설정하셔야 합니다.
 1) app/src/main/java/com/example/auctionapp/global/stomp/HupStomp.java
 2) app/src/main/java/com/example/auctionapp/global/retrofit/RetrofitTool.java
 3) app/src/main/java/com/example/auctionapp/domain/user/constant/Constants.java
