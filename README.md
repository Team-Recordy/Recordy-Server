![유영](https://github.com/user-attachments/assets/6dbdc4a3-2adc-4b79-b28a-25d4c7e0289a)

## 🌊 유영
### <span style="color:#ADD8E6">**유저가 ‘공간’을 ‘영상’으로 디깅하고, 나만의 ‘공간 취향’을 발견하는 서비스**</span>

</br>

## 🔢 목차
[프로젝트 설명](#프로젝트-설명) </br>
[팀원](#팀원) </br>
[Git Flow](#Git-Flow) </br>
[ERD](#ERD) </br>
[폴더링](#폴더링) </br>
[레코디 컨벤션 및 API docs](#레코디-컨벤션-및-API-docs) </br>
[폴더링](#폴더링) </br>
</br>

## 🅿️ 프로젝트 설명 
내 취향에 맞는 공간을 촬영하고 업로드하는 숏폼을 활용하는 라이프스타일 플랫폼입니다. 사용자들은 새로운 장소를 자유롭게 둘러보고 취향을 찾으며, 다른 사용자들과 공간 경험을 나눌 수 있습니다. 동영상을 활용하여 방문 전에도 실제와 유사한 공간감을 느낄 수 있는 공간영상을 제공합니다. 또한 키워드와 취향에 맞는 유저 구독 기능을 통해 무분별한 알고리즘에서 벗어나 취향에 맞는 공간 정보만 탐색할 수 있도록 하는 가치를 제공합니다.
</br>

### 🪼 우리가 해결하고자 하는 문제
**- 취향에 맞는 공간 정보를 받을 수 있는 플랫폼의 분산**</br>
**- 공간감을 느낄 수 없는 가공된 사진**</br>
**- 기존 플랫폼의 영상 알고리즘으로 내 취향에 맞지 않는 공간 노출** 
</br>

### 📔 해결책
**- 내 취향에 맞는 공간을 영상을 통해 향유할 수 있는 서비스가 필요**
</br>

### 🙋‍♀️ 타겟
**-공간을 좋아하는 1020 유저. 자신의 취향이 뚜렷한 유저**
</br>

### 🧩 우리의 서비스
**-유저가 ‘공간’을 ‘영상’으로 디깅하고, 나만의 ‘공간 취향’을 발견하는 서비스**
</br>

### 🔑 핵심 키워드
***'취향' '공간' '영상'***
</br>

### 🏅 주요 기능
1. **내 공간 경험 업로드하기**: 사용자가 다양한 장소를 촬영한 공간감이 느껴지는 짧은 영상을 앱에 업로드할 수 있으며, 간편한 인터페이스로 촬영부터 업로드까지 손쉽게 진행할 수 있습니다.
2. **취향 분석표 수집하기**: 사용자 취향을 분석하여 맞춤형 취향 분석표를 제공합니다. 나만의 분석표를 받고 공간 취향을 알아볼 수 있습니다.
3. **취향 기반 유저의 소식 받기:** 다른 유저를 팔로우하고, 그들의 영상을 저장하고 소식을 받을 수 있습니다.
4. **관심 있는 공간 저장하기:** 마음에 드는 공간 영상을 저장하고, 쉽게 보관할 수 있습니다.
</br>


## 👤 팀원
<img src="https://github.com/user-attachments/assets/a1f90c12-4c08-4734-9d64-dac155cc6643" width="830" /> 
</br>

| <img src="https://github.com/user-attachments/assets/d0e48286-1069-4632-ae49-27b5ca5e4a6c" width="250" /> | <img src="https://github.com/user-attachments/assets/525c9171-d194-43c0-b54d-b3dd1e50bf2b" width="250" /> | <img src="https://github.com/user-attachments/assets/414c2872-d449-4025-bf4b-86e8df71f139" width="250" /> |
|:---------:|:---------:|:---------:|
|[@sebbbin](https://github.com/sebbbin)|[@elive7](https://github.com/elive7)|[@jinkonu](https://github.com/jinkonu)|
| `나세빈` | `박수빈` | `👑 진건우` | 
</br>

|기능명|담당자|
|:---------:|:---------:|
| 프로젝트 기초 세팅 |	세빈, 수빈 건우 |
 | EC2 세팅, RDS 세팅 |	세빈, 수빈 건우 |
 | API 작업	|	세빈, 수빈 건우 |

</br>


## 🐬 Git Flow
<img width="757" alt="image" src="https://github.com/Team-Recordy/Recordy-Server/assets/94737768/d70f51f0-cc0f-4208-91d9-6aed63ef8ab9">

| 브랜치 명 | 설명 |
| :-------: | :--------------------------------------------------------------------------------- |
| main | 소프트웨어 제품 배포하는 용도로 쓰는 브랜치 |
| develop | 개발용 default 브랜치로, 이 브랜치를 기준으로 feature 브랜치를 따고, 합치는 브랜치 |
| feat | 단위 기능 개발용 브랜치 |
| fix | 단위 기능 개발 수정용 브랜치 |
| hotfix | master에 배포 코드가 합쳐진 후 버그 발생 시 긴급 수정하는 브랜치 |

### **📌 Branch**

- 깃플로우에 따라 기능별로 브랜치 생성
- 형식: `브랜치 명/#이슈번호`
</br>

## 🛠️ Architecture
<img width="747" alt="스크린샷 2024-07-08 오후 7 36 22" src="https://github.com/Team-Recordy/Recordy-Server/assets/94737768/c37583a4-ca8d-4f66-aa99-eb5dc3cf961b">

## 🌱 ERD
<img width="1006" alt="스크린샷 2024-07-03 오후 5 28 07" src="https://github.com/Team-Recordy/Recordy-Server/assets/94737768/c1c53d3b-7774-4fce-89a3-98d1ac558926">


## ❗ 레코디 컨벤션 및 API docs
**레코디 컨벤션 및 협업 규칙:**  [Git Convention](https://bohyunnkim.notion.site/0c9e0fe52b204329a7a927e05c125c16?pvs=4) </br>
**API docs:**  [API docs](https://www.notion.so/bohyunnkim/API-Docs-365270d3fb184ac0b3599dc28be5337d) </br>

## 🗂️ 폴더링
```
.
├── build
│   ├── classes
│   │   └── java
│   │       ├── main
│   │       │   └── org
│   │       │       └── recordy
│   │       │           └── server
│   │       │               ├── 📁 auth
│   │       │               │   ├── domain
│   │       │               │   ├── exception
│   │       │               │   ├── repository
│   │       │               │   │   └── impl
│   │       │               │   ├── security
│   │       │               │   │   └── handler
│   │       │               │   └── service
│   │       │               │       ├── dto
│   │       │               │       └── impl
│   │       │               │           ├── apple
│   │       │               │           ├── kakao
│   │       │               │           └── token
│   │       │               ├── 📁 common
│   │       │               │   ├── config
│   │       │               │   ├── domain
│   │       │               │   ├── dto
│   │       │               │   │   └── response
│   │       │               │   ├── exception
│   │       │               │   ├── handler
│   │       │               │   ├── message
│   │       │               │   └── util
│   │       │               ├── 📁 external
│   │       │               ├── 📁 keyword
│   │       │               ├── 📁 record
│   │       │               ├── 📁 record_stat
│   │       │               └── 📁 user
│   │       │                   ├── controller
│   │       │                   │   └── dto
│   │       │                   │       ├── request
│   │       │                   │       └── response
│   │       │                   ├── domain
│   │       │                   │   └── usecase
│   │       │                   ├── exception
│   │       │                   ├── repository
│   │       │                   │   └── impl
│   │       │                   └── service
│   │       │                       └── impl
│   │       └── test
│   │           └── org
│   │               └── recordy
│   │                   └── server
│   │                       ├── 📁 auth
│   │                       ├── 📁 common
│   │                       ├── 📁 external
│   │                       ├── 📁 keyword
│   │                       ├── 📁 mock
│   │                       │   ├── auth
│   │                       │   ├── keyword
│   │                       │   ├── record
│   │                       │   └── user
│   │                       ├── 📁 record
│   │                       ├── 📁 user
│   │                       └── 📁 util
```
</br>
