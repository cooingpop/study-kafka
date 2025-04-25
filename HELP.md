# 🐳 Kafka 4.0 (KRaft 모드) Docker Compose 실행 가이드

이 문서는 Kafka 4.0 이상에서 **Zookeeper 없이 KRaft 모드**로 Kafka를 실행하고, 토픽을 생성 및 테스트한 후 종료하는 과정을 안내합니다.

---
## 1. Docker Compose 파일 작성

Kafka 설정이 포함된 `docker-compose.yml` 파일은 `resources` 디렉토리에 위치합니다.  
Zookeeper 없이 Kafka 단독으로 실행되며, KRaft 모드 설정이 적용되어 있습니다.

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

## 참고 사항

- Kafka는 Zookeeper 없이 실행되며, 내부적으로 KRaft 메타데이터를 사용합니다.
- Kafka는 기본 포트 `9092`에서 메시지를 송수신합니다.
- Kafka CLI 명령어는 컨테이너 내부에서 실행됩니다.
