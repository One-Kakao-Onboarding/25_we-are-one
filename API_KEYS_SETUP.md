# API Keys 설정 가이드

## 보안 주의사항
⚠️ **API 키는 절대 Git에 커밋하지 마세요!**

이 프로젝트는 민감한 API 키를 안전하게 관리하기 위해 다음 방법을 사용합니다.

## 설정 방법

### 방법 1: application-local.properties 사용 (추천)

1. `src/main/resources/` 디렉토리에 `application-local.properties` 파일 생성
2. 아래 내용을 복사하여 실제 API 키로 변경:

```properties
# Kakao API Key
kakao.api.key=your-actual-kakao-api-key

# Gemini API Key
gemini.api.key=your-actual-gemini-api-key
```

3. 이 파일은 `.gitignore`에 포함되어 있어 Git에 올라가지 않습니다.

### 방법 2: 환경변수 사용

```bash
# Mac/Linux
export KAKAO_API_KEY="your-actual-kakao-api-key"
export GEMINI_API_KEY="your-actual-gemini-api-key"

# Windows (PowerShell)
$env:KAKAO_API_KEY="your-actual-kakao-api-key"
$env:GEMINI_API_KEY="your-actual-gemini-api-key"
```

### 방법 3: IDE 설정 (IntelliJ IDEA)

1. Run → Edit Configurations
2. Environment variables에 추가:
   - `KAKAO_API_KEY=your-actual-kakao-api-key`
   - `GEMINI_API_KEY=your-actual-gemini-api-key`

## API 키 발급 방법

### Kakao API Key
1. https://developers.kakao.com/ 접속
2. 내 애플리케이션 → 애플리케이션 추가
3. REST API 키 복사

### Gemini API Key
1. https://ai.google.dev/ 접속
2. Get API key 클릭
3. Google AI Studio에서 API 키 생성

## 현재 상태 확인

애플리케이션 실행 시 다음 로그로 API 키 설정 상태 확인 가능:
```
# 잘못 설정된 경우
gemini.api.key: your-gemini-api-key (placeholder)

# 올바르게 설정된 경우
gemini.api.key: AIzaSy... (실제 키의 일부)
```

## Git 커밋 전 체크리스트

- [ ] `application-local.properties`가 `.gitignore`에 포함되어 있는가?
- [ ] `application.properties`에 실제 API 키가 없는가?
- [ ] 커밋 전 `git diff`로 민감한 정보가 없는지 확인했는가?

## 팀원 온보딩

새로운 팀원이 프로젝트를 클론한 후:
1. 이 문서를 읽고 API 키 설정
2. `application-local.properties` 생성 또는 환경변수 설정
3. 애플리케이션 실행하여 정상 작동 확인
