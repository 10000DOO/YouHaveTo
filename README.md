<p align="center"><img src="https://user-images.githubusercontent.com/57493546/237003537-78c89647-3a5c-4b2a-b006-335d5e8d94ee.png" alt="로고"></p>

# Description
## 작품 개요
운동기구 AR 인식을 통해 알맞은 운동 자세를 애니메이션으로 보여주는 운동 보조 어플리케이션
## 개발 배경
요즘 2030 세대를 중심으로 운동을 시작하는 인구가 증가하고 있다. 그에 따라 자연스럽게 부상을 입는 사람들도 증가하고 있다. PT라는 해결책이 있지만 부담스러운 가격으로 인해 아직 많은 사람들이 PT 없이 스스로 운동하고 있다. 하지만 운동 초보자의 경우 제대로 운동을 하기 어려워 금방 운동에 흥미를 잃거나 운동 실력의 성장 없이 정체되는 경우가 많다.
따라서 이러한 문제점을 해결하여 운동 초보자들이 지속적으로 운동에 흥미를 가지고 자신의 실력을 향상시킬 수 있도록 AR과 GPT를 통한 운동 보조 앱을 만들기로 했다.
## 작품 소개
앱 내의 카메라를 통해 원하는 운동 기구를  비추면 AR 3D 애니메이션을 볼 수 있다. 사용자가 각도, 속도를 조절하면서 볼 수 있어 운동 자세에 대한 보다 자세한 정보를 얻을 수 있다. 추가적으로 초보자들이 쉽게 할 수 있는 잘못된 자세들도 볼 수 있다.
초보자들의 경우 운동 루틴과 식단을 구성하는데 많은 어려움이 있을 수 있다. 이러한 경우 운동 목적, 키, 몸무게를 입력하면 운동 루틴을 추천받을 수 있고 추가적으로 오늘 먹은 음식을 입력하면 식단에 대한 피드백과 추천을 받을 수 있다.
마지막으로 운동 다이어리와 피드백 기능을 활용하여 꾸준한 운동 습관을 만들 수 있다. 운동 후 자극 부위를 선택하면 해당 운동이 적절히 수행됐는지 판단하여 적절한 피드백을 받아볼 수 있다. 또한 자신의 운동 기록을 작성하여 자신의 성장 과정을 볼 수 있다.
## 기대효과
- PT 비용이 부담스러운 사회 초년생과 대학생들이 AR을 통해 정확한 자세에 대한 정보를 얻고 운동을 효율적으로 진행할 수 있다.
- AR을 이용한 기구 사용 및 운동 설명으로 글, 영상보다 정확한 운동 자세를 알려줄 수 있어서 운동 간 잘못된 자세로 인한 부상의 우려가 적어진다.
- GPT를 통해 식단 추천 및 운동 루틴 추천, 운동 후 자극 부위를 통한 운동 피드백을 받아 효율적인 자기관리를 할 수 있다.
- 매일 운동 일지를 기록하며 규칙적인 운동 습관을 기르고, 과거의 자신과 비교를 하면서 얼마나 자신이 발전하였는지 알 수 있다.

## 주요 화면
### 📌AR애니메이션 
<img src="https://github.com/10000DOO/YouHaveTo/assets/57493546/01fff923-34c1-4507-a0a6-cd67963a5115" width="235">  

### 📌ChatGPT를 통한 운동 루틴, 식단 추천
<img src="https://github.com/10000DOO/YouHaveTo/assets/57493546/8f01321e-8dd0-42d2-a453-8c7e2077ecc0" width="200">

### 📌자극 부위를 통한 운동 피드백  
<img src="https://github.com/10000DOO/YouHaveTo/assets/57493546/4f89ced6-fe83-4529-9c21-fd9dff56a745" width="205">

# Environment
## 주요 적용 기술 및 구조
### 백엔드
프레임워크 : SpringBoot  
주요 라이브러리 : Spring Security, SpringData JPA, Querydsl, jjwt  
외부 API : 서울 열린데이터 광장  
개발 언어 : JAVA   
개발 환경 : Mac OS, AWS EC2(AmazonLinux)  
개발 도구 : IntelliJ, DataGrip, Jenkins, Docker  
테스트 : Mockito, JUnit5  
데이터 베이스 : AWS RDS(Postgresql)  
협업 : Git, Notion, Slack, Trello  
### 안드로이드
주요 라이브러리 : Vuforia  
외부 API : 네이버 지도  
개발 언어 : Kotlin, C#  
개발 환경 : Window, Android   
개발 도구 : Android Studio, Unity  
협업 : Git, Notion, Slack, Trello  
## 구조
<img src="https://github.com/10000DOO/YouHaveTo/assets/57493546/7f980335-2480-49a9-bc83-837b8e4e63e9" alt="구조도" width="600">

# Reference
안드로이드 GitHub : https://github.com/alstlr0312/Hcapstone  
API 문서 : https://documenter.getpostman.com/view/18513911/2s935oLPKm  
## 팀 소개
### 팀명 : 헬짱
### 팀원
이건준 : https://github.com/10000DOO  
김민식 : https://github.com/alstlr0312  
이예림 : https://github.com/yerim425  
정윤주 : https://github.com/YJMINT  

