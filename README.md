# 카카오페이 뿌리기 API 서버 개발
   ## 프로젝트 구성
   * Java 1.8
   * Spring Boot
   * JPA
   * H2 Embedded
   
   ## 실행하기
   소스 코드 빌드
   ```shell
   $ mvn clean package
   ```
   jar 파일 실행
   ```shell
   $ java -jar moneyplex-0.0.1.jar
   ```
   ## 핵심 문제 해결 전략
   1. 뿌리기
      * 뿌리기 요청건에 대한 고유한 token 을 발급 (3자리, 예측 불가함)
      * token 생성시 유효성 검사 -> DB조회 (token, roomId, userId, 최근 10분간 생성된 토큰에 대한 검사)
      * 중복 입력에 대한 고민
         * 동시성에 대한 문제가 있을수 있으나, 동일한 톡방에 동일 사용자가 동시에 호출 하는 일은 드물것이라고 판단
         
   2. 받기
      * 할당되지 않은 분배건 하나를 선택 하여 할당
      * 동시성 문제를 해결하기 위해, Transaction Isolation Level 을 REPEATABLE_READ 로 설정
      * 중복 받기 처리는 database의 uniquekey 로 처리
      
   ## API 테스트
   1. 뿌리기
      * `HTTP` `POST` `/api/v1/plex`
      * `Request`
      ```
      curl --location --request POST 'localhost:8080/api/v1/plex' \
      --header 'X-ROOM-ID: _rHsfX' \
      --header 'X-USER-ID: 25322263452' \
      --header 'Content-Type: application/json' \
      --data '{
      	"amount" : 10300,
      	"targetCount" : 5
      }'
      ```
      * `Response`
      ```
      {
          "result": true,
          "description": "Success",
          "data": {
              "token": "Bxy"
          }
      }
      ```
  2. 받기
        * `HTTP` `PUT` `/api/v1/take/{token}`
        * `Request`
        ```
        curl --location --request PUT 'localhost:8080/api/v1/take/{token}' \
        --header 'X-ROOM-ID: _rHsfX' \
        --header 'X-USER-ID: 11342423834'
        ```
      * `Response`
      ```
      {
          "result": true,
          "description": "Success",
          "data": {
              "takeMoney": 5849
          }
      }
      ```
  3. 내역 조회
        * `HTTP` `GET` `/api/v1/plex/{token}`
        * `Request`
        ```
        curl --location --request GET 'localhost:8080/api/v1/plex/{token}' \
        --header 'X-ROOM-ID: _rHsfX' \
        --header 'X-USER-ID: 25322263452'
        ```
      * `Response`
      ```
      {
          "result": true,
          "description": "Success",
          "data": {
              "createdTime": "2020-08-17 15:12:48",
              "amount": 10300,
              "takeMoney": 1557,
              "takeMoneyDetails": [
                  {
                      "receiveMoney": 1557,
                      "receiveUserId": 2533990
                  }
              ]
          }
      }
      ```