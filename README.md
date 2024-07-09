![image](https://github.com/Team-Recordy/Recordy-Server/assets/94737768/34b93fd0-0dc3-4838-8c58-34c5ef72419e)


## 🌊 유영
<span style="color:#ADD8E6">**유저가 ‘공간’을 ‘영상’으로 디깅하고, 나만의 ‘공간 취향’을 발견하는 서비스**</span>

## 🔢 목차
[프로젝트 설명](#프로젝트-설명) </br>
[팀원](#팀원) </br>
[Git Flow](#Git-Flow) </br>
[ERD](#ERD) </br>
[폴더링](#폴더링) </br>
[레코디 컨벤션 및 API docs](#레코디-컨벤션-및-API-docs) </br>
[폴더링](#폴더링) </br>

## 🅿️ 프로젝트 설명 

### 🪼 우리가 해결하고자 하는 문제
**- 취향에 맞는 공간 정보를 받을 수 있는 플랫폼의 분산**</br>
**- 공간감을 느낄 수 없는 가공된 사진**</br>
**- 기존 플랫폼의 영상 알고리즘으로 내 취향에 맞지 않는 공간 노출** 

### 📔 해결책
**- 내 취향에 맞는 공간을 영상을 통해 향유할 수 있는 서비스가 필요**

### 🙋‍♀️ 타겟
**-공간을 좋아하는 1020 유저. 자신의 취향이 뚜렷한 유저**

### 🧩 우리의 서비스
**-유저가 ‘공간’을 ‘영상’으로 디깅하고, 나만의 ‘공간 취향’을 발견하는 서비스**

### 🔑 핵심 키워드
### '취향' '공간' '영상'

### 🏅 기능 우선순위 3가지 (MVP에서 가져갈 기능)
**- 영상 업로드** </br>
**- 취향 키워드칩 선택** </br>
**- 타 유저 영상 시청 후 북마크**
</br>


## 👤 팀원
|<img src="https://github.com/Team-Recordy/Recordy-Server/assets/94737768/76b261b4-bc99-49ef-b221-abd32126db78" width="128" /> |<img src="https://github.com/Team-Recordy/Recordy-Server/assets/94737768/b38469c6-9239-4972-b821-ca5263f889be" width="128" /> |<img src="https://github.com/Team-Recordy/Recordy-Server/assets/94737768/50606469-9d7b-4ced-aff2-acd3876fc9db" width="128" />|
|:---------:|:---------:|:---------:|
|[나세빈](https://github.com/sebbbin)|[박수빈](https://github.com/elive7)|[진건우](https://github.com/jinkonu)|
| `나세빈` | `박수빈` | `👑 진건우` | 
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
