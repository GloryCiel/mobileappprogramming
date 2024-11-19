# 대구 시민을 위한 다목적 러닝 보조 애플리케이션 : 모바일 앱 프로그래밍 6팀

## 프로젝트 배경

현재 20~30대 젊은 층 사이에서 러닝/산책이 인기 있는 취미로 자리 잡고 있다.

이에 비해서 러닝 코스에 대한 정보를 체계적이고 사용자 친화적인 플랫폼이 부족과 더불어

러닝/산책에 필요한 정보들과 같은 취미를 가진 다양한 사람들과의 교류를 함께 제공하여

효율적이고 즐거운 러닝/산책 경험을 누릴 수 있게 하기 위한 프로젝트이다.

## 기능 설명

- 러닝/산책 코스 추천 및 시각화
- 필수 정보(편의시설, 신호등 등) 제공
- 러닝/산책 현재 상황(거리, 시간, 페이스 등) 제공

### 추가 정보

[정부 기관](data.go.kr)에서 제공하는 산책로 데이터를 사용하였다.

### 세팅해야 할 사항

build.gradle.kts(Module :app)
works_for_gloryciel 브랜치 참고해서, 의존성 주입하기

build.gradle.kts(Project : runningapp)
위에 언급된 것과 똑같이 세팅하기

local.properties에
com.naver.maps.map.CLIENT_ID= '발급받은 API ID'넣기, 네이버에서 API 생성시 mobile dynamic map(android) 이걸로 선택d.gradle.kts(Module :app)
works_for_gloryciel 브랜치 참고해서, 의존성 주입하기

build.gradle.kts(Project : runningapp)
위에 언급된 것과 똑같이 세팅하기

local.properties에
com.naver.maps.map.CLIENT_ID= '발급받은 API ID'넣기, 네이버에서 API 생성시 Mobile Dynamic Map Android 이걸로 발급받기
