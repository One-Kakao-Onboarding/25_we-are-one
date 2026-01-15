# 탄소 배출량 관리 시스템 API 명세서

Base URL: `http://localhost:8080/api`

## 1. 인증 (Auth)

### 1.1 로그인
- **URL**: `POST /api/auth/login`
- **Request**:
```json
{
  "username": "employee1",
  "password": "1234"
}
```
- **Response**:
```json
{
  "username": "employee1",
  "name": "김직원",
  "employeeId": "EMP001",
  "department": "개발팀",
  "role": "EMPLOYEE",
  "companyId": "KAKAO"
}
```

### 1.2 로그아웃
- **URL**: `POST /api/auth/logout`
- **Request**: 없음
- **Response**:
```json
{
  "message": "로그아웃되었습니다"
}
```

### 1.3 현재 사용자 정보 조회
- **URL**: `GET /api/auth/me`
- **Request**: 없음
- **Response**:
```json
{
  "username": "employee1",
  "name": "김직원",
  "employeeId": "EMP001",
  "department": "개발팀",
  "role": "EMPLOYEE",
  "companyId": "KAKAO"
}
```

---

## 2. 출퇴근 관리 (Commute)

### 2.1 출퇴근 체크인
- **URL**: `POST /api/commute/check-in`
- **Request**:
```json
{
  "date": "2024-01-15",
  "usedCar": true,
  "vehicleType": "ICE"
}
```
- **Response**:
```json
{
  "id": "CR-1",
  "message": "출퇴근 정보가 등록되었습니다",
  "date": "2024-01-15",
  "usedCar": true,
  "vehicleType": "ICE",
  "emissions": 5.2,
  "monthlyEmissions": 45.8
}
```

### 2.2 출퇴근 기록 조회
- **URL**: `GET /api/commute/history`
- **Query Parameters**:
  - `startDate` (optional): 시작 날짜 (예: 2024-01-01)
  - `endDate` (optional): 종료 날짜 (예: 2024-01-31)
  - `usedCar` (optional): 차량 사용 여부 (true/false)
  - `limit` (optional): 결과 개수 제한
- **Example**: `GET /api/commute/history?startDate=2024-01-01&endDate=2024-01-31&limit=10`
- **Response**:
```json
{
  "data": [
    {
      "id": "CR-1",
      "date": "2024-01-15",
      "usedCar": true,
      "vehicleType": "ICE",
      "distance": 30.0,
      "emissions": 5.19
    }
  ],
  "stats": {
    "totalDays": 15,
    "carUsedDays": 10,
    "carUsageRate": 66.7,
    "totalDistance": 450.0,
    "totalEmissions": 77.9
  }
}
```

### 2.3 출퇴근 통계 조회
- **URL**: `GET /api/commute/statistics`
- **Query Parameters**:
  - `startDate` (optional): 시작 날짜
  - `endDate` (optional): 종료 날짜
- **Example**: `GET /api/commute/statistics?startDate=2024-01-01&endDate=2024-01-31`
- **Response**:
```json
{
  "period": {
    "startDate": "2024-01-01",
    "endDate": "2024-01-31"
  },
  "summary": {
    "totalDays": 20,
    "carUsedDays": 12,
    "carUsageRate": 60.0,
    "totalDistance": 600.0,
    "totalEmissions": 103.8
  },
  "byVehicleType": {
    "ICE": {
      "days": 10,
      "emissions": 86.5
    },
    "HYBRID": {
      "days": 2,
      "emissions": 17.3
    }
  }
}
```

---

## 3. 출장 관리 (Business Trip)

### 3.1 출장 정보 등록
- **URL**: `POST /api/business-trip/register`
- **Content-Type**: `multipart/form-data`
- **Request**:
```
date: 2024-01-15
type: FLIGHT
departure: 서울특별시 강남구 판교역로 235
arrival: 제주특별자치도 제주시 첨단로 242
distance: 453.5
emissions: 64.6
receipt: [파일]
```
- **Response**:
```json
{
  "id": "BT-1",
  "status": "success",
  "message": "출장 정보가 등록되었습니다",
  "registeredAt": "2024-01-15T14:30:00",
  "monthlyTrips": 3,
  "monthlyEmissions": 150.2
}
```

### 3.2 출장 기록 조회
- **URL**: `GET /api/business-trip/history`
- **Query Parameters**:
  - `startDate` (optional): 시작 날짜
  - `endDate` (optional): 종료 날짜
  - `type` (optional): 출장 유형 (FLIGHT, TRAIN, BUS)
  - `limit` (optional): 결과 개수 제한
- **Example**: `GET /api/business-trip/history?type=FLIGHT&limit=10`
- **Response**:
```json
{
  "data": [
    {
      "id": "BT-1",
      "date": "2024-01-15",
      "type": "FLIGHT",
      "departure": "서울특별시 강남구 판교역로 235",
      "arrival": "제주특별자치도 제주시 첨단로 242",
      "distance": 453.5,
      "emissions": 64.6,
      "receiptFileName": "EMP001_1705303800000.pdf"
    }
  ],
  "stats": {
    "totalTrips": 5,
    "totalDistance": 2500.0,
    "totalEmissions": 350.5,
    "byType": {
      "FLIGHT": {
        "count": 3,
        "emissions": 280.0
      },
      "TRAIN": {
        "count": 2,
        "emissions": 70.5
      }
    }
  }
}
```

### 3.3 출장 배출량 예측
- **URL**: `POST /api/business-trip/estimate-emissions`
- **Request**:
```json
{
  "departure": "서울특별시 강남구 판교역로 235",
  "arrival": "제주특별자치도 제주시 첨단로 242",
  "type": "FLIGHT"
}
```
- **Response**:
```json
{
  "distance": 453.5,
  "estimatedEmissions": 64.63,
  "emissionsFactor": 0.14253
}
```

---

## 4. 대시보드 (Dashboard) - 컨설턴트 전용

### 4.1 전체 개요 조회
- **URL**: `GET /api/dashboard/overview`
- **Query Parameters**:
  - `department` (optional): 부서별 필터링
- **Example**: `GET /api/dashboard/overview?department=개발팀`
- **Response**:
```json
{
  "totalEmployees": 150,
  "totalEmissions": 5420.5,
  "activeUsers": 142,
  "departments": [
    {
      "name": "개발팀",
      "employeeCount": 50,
      "totalEmissions": 1800.2
    }
  ]
}
```

### 4.2 월별 추이 조회
- **URL**: `GET /api/dashboard/monthly-trend`
- **Query Parameters**:
  - `department` (optional): 부서별 필터링
  - `months` (optional): 조회 개월 수 (기본값: 6)
- **Example**: `GET /api/dashboard/monthly-trend?months=12`
- **Response**:
```json
{
  "months": [
    {
      "month": "2024-01",
      "totalEmissions": 450.5,
      "commuteEmissions": 250.0,
      "businessTripEmissions": 180.5,
      "kakaoTEmissions": 20.0
    }
  ]
}
```

### 4.3 카카오T 데이터 동기화
- **URL**: `POST /api/dashboard/sync-kakao-t`
- **Request**: 없음
- **Response**:
```json
{
  "status": "success",
  "message": "카카오T 데이터가 동기화되었습니다",
  "syncedRecords": 45,
  "syncedAt": "2024-01-15T14:30:00"
}
```

### 4.4 카카오T 데이터 조회
- **URL**: `GET /api/dashboard/kakao-t-data`
- **Query Parameters**:
  - `employeeId` (optional): 직원 ID
  - `startDate` (optional): 시작 날짜
  - `endDate` (optional): 종료 날짜
  - `serviceType` (optional): 서비스 유형 (TAXI, QUICK, BIKE)
- **Example**: `GET /api/dashboard/kakao-t-data?serviceType=TAXI`
- **Response**:
```json
{
  "data": [
    {
      "id": 1,
      "employeeId": "EMP001",
      "employeeName": "김직원",
      "usageDate": "2024-01-15T10:30:00",
      "serviceType": "TAXI",
      "route": "판교역 → 강남역",
      "distance": 15.5,
      "emissions": 2.68,
      "syncedAt": "2024-01-15T14:30:00"
    }
  ],
  "stats": {
    "totalUsage": 120,
    "totalDistance": 1850.0,
    "totalEmissions": 320.0
  }
}
```

### 4.5 부서별 통계 조회
- **URL**: `GET /api/dashboard/department-stats`
- **Query Parameters**:
  - `startDate` (optional): 시작 날짜
  - `endDate` (optional): 종료 날짜
- **Example**: `GET /api/dashboard/department-stats?startDate=2024-01-01&endDate=2024-01-31`
- **Response**:
```json
{
  "departments": [
    {
      "department": "개발팀",
      "employeeCount": 50,
      "totalEmissions": 1800.5,
      "avgEmissionsPerEmployee": 36.0,
      "commuteEmissions": 1000.0,
      "businessTripEmissions": 750.5,
      "kakaoTEmissions": 50.0
    }
  ]
}
```

---

## 5. 보고서 (Reports) - 컨설턴트 전용

### 5.1 보고서 목록 조회
- **URL**: `GET /api/reports/list`
- **Query Parameters**:
  - `type` (optional): 보고서 유형 (MONTHLY, QUARTERLY, ANNUAL, CUSTOM)
  - `status` (optional): 상태 (READY, GENERATING, FAILED)
- **Example**: `GET /api/reports/list?type=MONTHLY&status=READY`
- **Response**:
```json
{
  "reports": [
    {
      "id": 1,
      "type": "MONTHLY",
      "title": "2024년 1월 월간 보고서",
      "status": "READY",
      "createdAt": "2024-01-31T23:59:00",
      "completedAt": "2024-01-31T23:59:30",
      "fileName": "report_202401.pdf",
      "fileSize": 2048576
    }
  ]
}
```

### 5.2 보고서 생성
- **URL**: `POST /api/reports/generate`
- **Request**:
```json
{
  "type": "MONTHLY",
  "title": "2024년 1월 월간 보고서",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```
- **Response**:
```json
{
  "reportId": 1,
  "status": "GENERATING",
  "message": "보고서 생성이 시작되었습니다"
}
```

### 5.3 보고서 다운로드
- **URL**: `GET /api/reports/download/{reportId}`
- **Example**: `GET /api/reports/download/1`
- **Response**: PDF 파일 (application/pdf)

### 5.4 보고서 생성 상태 조회
- **URL**: `GET /api/reports/status/{reportId}`
- **Example**: `GET /api/reports/status/1`
- **Response**:
```json
{
  "reportId": 1,
  "status": "READY",
  "progress": 100,
  "message": "보고서 생성이 완료되었습니다",
  "completedAt": "2024-01-31T23:59:30"
}
```

---

## 6. 거리 계산 (Distance)

### 6.1 두 지점 간 거리 계산
- **URL**: `POST /api/distance`
- **Request**:
```json
{
  "origin": "서울특별시 강남구 판교역로 235",
  "destination": "서울특별시 강남구 테헤란로 152"
}
```
- **Response**:
```json
{
  "origin": "서울특별시 강남구 판교역로 235",
  "destination": "서울특별시 강남구 테헤란로 152",
  "originCoordinates": {
    "latitude": 37.4010,
    "longitude": 127.1080
  },
  "destinationCoordinates": {
    "latitude": 37.5015,
    "longitude": 127.0395
  },
  "distanceInMeters": 12500,
  "durationInSeconds": 1800
}
```
**Note**: 거리는 미터(m) 단위로 반환됩니다.
```

---

## 공통 사항

### 인증
모든 API는 세션 기반 인증을 사용합니다. 로그인 후 반환된 `JSESSIONID` 쿠키를 이후 요청에 포함해야 합니다.

### 권한
- **EMPLOYEE**: 출퇴근, 출장 관리 API만 접근 가능 (본인 데이터만)
- **CONSULTANT**: 모든 API 접근 가능 (전체 직원 데이터)

### 테스트 계정
```
사원 계정:
- username: employee1, password: 1234 (개발팀)
- username: employee2, password: 1234 (영업팀)
- username: employee3, password: 1234 (인사팀)

컨설턴트 계정:
- username: consultant, password: admin
```

### 배출 계수
- 비행기: 0.14253 kgCO2e/km
- 자가용/택시: 0.17304 kgCO2e/km
- 기차: 0.03546 kgCO2e/km
- 자전거/도보/전기차/대중교통(버스): 0.0 kgCO2e/km

### 에러 응답
```json
{
  "error": "Unauthorized",
  "message": "로그인이 필요합니다"
}
```

HTTP 상태 코드:
- 200: 성공
- 400: 잘못된 요청
- 401: 인증 필요
- 403: 권한 없음
- 404: 리소스 없음
- 500: 서버 오류
