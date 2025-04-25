# 🐳 Kafka 4.0 (KRaft 모드) Docker Compose 실행 가이드

이 문서는 Kafka 4.0 이상에서 **Zookeeper 없이 KRaft 모드**로 Kafka를 실행하고, 토픽을 생성 및 테스트한 후 종료하는 과정을 안내합니다.

---
## 1. Docker Compose 파일 작성

Kafka 설정이 포함된 `docker-compose.yml` 파일은 `resources` 디렉토리에 위치합니다.  
Zookeeper 없이 Kafka 단독으로 실행되며, KRaft 모드 설정이 적용되어 있습니다.
추가로, kafka-ui도 같이 설치되어, 이후 웹 브라우저에서 http://localhost:8989 으로 접속하여 kafka UI 사용 가능합니다.

---
## 2. Kafka 컨테이너 실행

Kafka를 실행하려면 다음 명령어를 입력합니다:

```bash
docker-compose up -d
```

### 실행 중인 컨테이너 확인

```bash
docker ps
```

예시 출력:

```
CONTAINER ID   IMAGE                          PORTS                    NAMES
xxxxx          confluentinc/cp-kafka:7.5.0    0.0.0.0:9092->9092/tcp   kafka
```

---

## 3. Kafka 토픽 생성 및 테스트

### 3.1 Kafka 컨테이너 접속

```bash
docker exec -it kafka bash
```

### 3.2 토픽 생성

```bash
kafka-topics --bootstrap-server localhost:9092 --create \
  --topic my-topic \
  --partitions 1 \
  --replication-factor 1
```

### 3.3 토픽 목록 확인

```bash
kafka-topics --bootstrap-server localhost:9092 --list
```

### 3.4 메시지 전송 (Producer)

```bash
kafka-console-producer --bootstrap-server localhost:9092 --topic my-topic
```

실행 후 터미널에 아래와 같이 직접 메시지를 입력할 수 있습니다:

```
{"name":"홍길동", "age":30}
```

### 3.5 메시지 수신 (Consumer)

```bash
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic my-topic \
  --from-beginning
```

이 명령어는 해당 토픽에 저장된 메시지를 **처음부터 모두 출력**합니다.

---

## 4. Kafka 중지 및 정리

### 4.1 Kafka 컨테이너 중지

Kafka 컨테이너를 종료하려면 다음 명령어를 입력합니다:

```bash
docker-compose down
```

### 4.2 로그 및 볼륨까지 모두 삭제 (선택)

Kafka의 저장 데이터까지 정리하려면 아래 명령어를 사용하세요:

```bash
docker-compose down -v
```

> `-v` 옵션은 `/kafka-data`와 같은 내부 로그 디렉토리까지 제거합니다.

---

# 📡 Kafka API 호출 및 메시지 확인 방법

Kafka 4.0 (KRaft 모드) 기반 Spring Kafka 예제에서 메시지를 API로 전송하고, 실제 Kafka에서 수신 확인하는 방법을 안내합니다.

---

## ✅ 1. Kafka 메시지 전송 API 호출

Spring Boot 애플리케이션이 실행 중일 때 아래와 같은 방식으로 Kafka에 메시지를 전송할 수 있습니다.

### 🔗 POST API

- URL: `http://localhost:8080/kafka/send`
- Method: `POST`
- Content-Type: `application/json`

### 📥 Request Body 예시

```json
{
  "name": "홍길동"
}
```

### 🧪 cURL 예시

```bash
curl -X POST http://localhost:8080/kafka/send \
  -H "Content-Type: application/json" \
  -d '{"name":"홍길동"}'
```

---

## ✅ 2. 메시지 수신 확인 방법

### 📌 방식 1: 서버 콘솔 로그 확인

Spring Kafka Consumer 클래스에서 로그를 통해 메시지 수신 여부를 확인합니다.

```java
@KafkaListener(topics = "my-topic", groupId = "json-group", containerFactory = "kafkaListenerContainerFactory")
public void consume(MyMessage message) {
    System.out.println("수신한 메시지: " + message.getName();
}
```

- 기대 출력:
  ```
  수신한 메시지: 홍길동
  ```

---

### 📌 방식 2: Kafka CLI 수동 확인 (선택)

Kafka 컨테이너에 접속 후 수신된 메시지를 직접 확인할 수 있습니다:

```bash
docker exec -it kafka bash
```

```bash
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic my-topic \
  --from-beginning
```

→ 전송된 메시지가 JSON 형태로 출력됩니다.

---

## 📝 참고

- Kafka 컨테이너가 실행 중이어야 합니다 (`docker ps`)
- Spring Boot 애플리케이션의 포트는 `8080` 기준입니다.
- 토픽 이름과 설정 값(`my-topic`, `json-group`)이 일치해야 정상 수신됩니다.



---

## 참고 사항

- Kafka는 Zookeeper 없이 실행되며, 내부적으로 KRaft 메타데이터를 사용합니다.
- Kafka는 기본 포트 `9092`에서 메시지를 송수신합니다.
- Kafka CLI 명령어는 컨테이너 내부에서 실행됩니다.
