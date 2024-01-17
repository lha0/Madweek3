# Madweek3

# 양세찬 Game

---

## 무료한 당신, 친구들과 함께 양세찬 게임을 해보는 건 어떨까요?

런닝맨의 대표 게임을 온라인으로도 즐길 수 있는 ‘양세찬 Game’앱!

최대 4명의 친구들과 실시간 채팅을 통해 양세찬 게임을 즐기고 정답을 맞힌 순서대로 점수를 부여 받습니다.

게임에서 높은 점수를 받아 레벨을 높여나가고, 랭킹을 살펴보며 경쟁의 재미를 느낄 수 있습니다.

---

### 개발 팀원

- 양혜민 - DGIST 컴퓨터 공학과 20학번
- 이하영 - UNIST 컴퓨터 공학

### 개발 스택

- Kotlin
- Flask
- Mongo DB
- Android Studio
- Figma

## 양세찬 게임 설명

---

### 한줄 요약

질문을 통해 본인의 키워드를 맞힌다.

필요 인원: 2~4명

키워드 주제 : 랜덤 인물 및 캐릭터

### 진행 방법

1. 순서를 정한다.
2. 순서대로 본인의 키워드에 대한 질문을 던진다.
    
    ex) 이것은 사람입니까?
    
3. 정답을 알 것 같다면, ‘정답 입력’을 눌러 정답을 맞힌다.
4. 1명이 남을 때까지 진행 후, 1명만 남으면 자동 게임 종료

## 앱 기능

---

### 로그인 & 회원가입

- 자체 제작 로그인, 회원가입 구현
- 앱의 첫 화면 → 로그인 화면
    - 데이터 베이스에 있는 정보와 일치할 경우, 로그인 성공
    - 정보와 일치하지 않을 경우 자동으로 회원가입 페이지로 이동

![회원가입.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/ec9e4537-8a63-4d61-9479-fd09a2a34b75/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85.png)

![Android Small - 16.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/8f0ceb2c-b4b2-429d-a8a3-1a30a1661142/Android_Small_-_16.png)

### 메인 화면

- 로그아웃 버튼을 누르면 다시 로그인 화면으로 복귀
- 입장하기 버튼을 누르면 게임방 리스트가 있는 페이지로 넘어가고, 방 찾기 버튼을 누르면 자기가 찾고자 하는 방 코드를 입력할 수 있는 페이지로 넘어간다.
- 랭킹 페이지, 마이페이지로 연결되는 버튼들이 존재
- 물음표 버튼을 누르면, 양세찬 게임에 대한 간략한 설명을 살펴볼 수 있다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/86479282-6c00-4ed7-8792-83b956d91b8e/Untitled.png)

![Android Small - 17.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/d91477a4-c91c-4a26-9f26-4df456fa57ff/Android_Small_-_17.png)

### 마이페이지

- 본인의 닉네임, 이메일, 현재 레벨과 그 레벨의 이미지를 확인할 수 있다.
- 수정 버튼을 누르면 자신의 정보(이메일, 닉네임, 비밀번호)를 수정할 수 있는 기능을 추가했다.
- 물음표 버튼을 누르면 Level에 대한 상세 설명 창이 뜬다. 점수대에 따라서 각자 다른 레벨을 갖도록 설정했다.

![Android Small - 7.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/d718759c-b27e-4ef7-acab-e3bf3b5c6de2/Android_Small_-_7.png)

![image 36.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/4377a44e-a8fe-4e0c-94e9-670a2d1ea596/image_36.png)

### 방 목록 페이지 & 방 찾기 페이지

- 방은 방 목록에서 찾아서 들어가거나, 자신이 특정 방을 찾아서 들어갈 수 있다.
- **방 목록 페이지**에서는 recycler view를 이용하여 현재 존재하는 방들을 보여주었다.
    - 방 제목, 현재 인원, 방의 비밀번호 설정 여부 등을 알 수 있다.
- **방 찾기 페이지**에서는 방마다 존재하는 코드를 입력하면 해당 방으로 이동할 수 있다.
- 본인이 원하는 방에 들어갈 때, 만약 비밀번호가 존재한다면 비밀번호를 입력해서 일치하는 경우에만 들어갈 수 있도록 설정하였다.

![Android Small - 11.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/337d4600-ba00-4fa1-b6e0-48841e625634/Android_Small_-_11.png)

![Android Small - 10.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/d22123d7-1cf3-4460-ba5c-af1720aa8abd/Android_Small_-_10.png)

![Android Small - 13.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/f1105018-2ee7-4869-b832-70299fc0a0b8/Android_Small_-_13.png)

### 방 만들기

- 방 목록 페이지에서는 **방 만들기 버튼**을 누르면, 방을 만들 수 있는 페이지로 넘어간다.
    - 본인이 원하는 방 제목, 비밀번호 설정 여부, 비밀번호를 입력하면 그에 맞게 방이 만들어진다.
    - 방을 만든 사람은 자동적으로 그 방의 방장이 되도록 데이터를 저장하였다.
    - 방을 만들고 나면, 만들어진 방으로 바로 이동하게 된다.

![Android Small - 12.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/8b171559-143f-4a44-9012-c19498c897d2/Android_Small_-_12.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/8e6523e7-8785-4578-8e84-57c3409b3155/Untitled.png)

### 게임 대기실

- 게임 대기실에는 해당 방에 접속한 유저들의 닉네임, 레벨 등이 Grid View로 표시된다.
- 위에 복사하기 버튼을 누르면 해당 방의 코드를 복사할 수 있고, 그 코드는 사용자의 클립보드에 보관된다.
- 모든 유저들이 준비하기 버튼을 누르면 시작하기 버튼이 활성화되고, 방장만이 시작하기 버튼을 눌러 게임을 시작할 수 있다.

![Android Small - 14.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/3261d4e7-a8d8-4b26-91dd-cb65dd8face3/Android_Small_-_14.png)

![Android Small - 15.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/e18b1c3d-8a14-47b0-a1b3-69faf78cdc7b/Android_Small_-_15.png)

### 게임 화면

- 게임을 시작하면 랜덤으로 키워드를 배정받고, 본인을 제외한 나머지 유저들의 키워드를 화면에서 바로 확인할 수 있습니다.
- 실시간으로 채팅이 가능하며, 내가 보낸 채팅을 오른쪽에서, 상대방이 보낸 채팅을 왼쪽에서 확인할 수 있습니다.

![image 48.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/a8fa8feb-86af-4c5c-b9c0-5be10edf50f8/image_48.png)

![image 49.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/f74dc762-b183-42a1-81d9-c3d6273ff389/image_49.png)

- 게임 진행 중 본인의 키워드를 알 것 같다면, 정답 입력 버튼을 눌러 정답을 기입합니다.
- 틀린 경우 ‘틀렸습니다’라는 메세지가 dialog 창으로 뜨게 되고, 정답일 경우 ‘맞혔습니다’라는 메세지가 뜹니다.
- 게임을 쭉 진행해서 1명을 제외한 모든 유저들이 정답을 맞혔다면 게임이 종료됩니다.

![image 50.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/2f4b856a-3fad-4fc2-884f-5642f439a281/image_50.png)

![image 51.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/19ea9de2-63a2-45d7-bf5f-07f7f90438cd/image_51.png)

![image 53.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/be872c9e-82c3-4a7f-86c1-a6bbba2a67cd/image_53.png)

### 게임 종료 화면

- 정답을 맞힌 순서대로 순위 및 점수가 부여됩니다.
- 점수는 바로 데이터 베이스에 반영됩니다.
- 랭킹 페이지에 들어가면 점수 순서대로 나열된 랭킹을 확인할 수 있습니다.
- 돌아가기 버튼을 누르면 방 목록 페이지로 이동합니다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/2329442a-152b-43bc-9169-fffaa608410a/Untitled.png)

![Android Small - 9.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/e22136a7-26f6-48b6-8565-ca35fd17267d/Android_Small_-_9.png)
