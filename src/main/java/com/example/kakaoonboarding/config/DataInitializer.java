package com.example.kakaoonboarding.config;

import com.example.kakaoonboarding.entity.*;
import com.example.kakaoonboarding.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 초기 Mock 데이터 삽입
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CommuteRecordRepository commuteRecordRepository;
    private final BusinessTripRepository businessTripRepository;
    private final KakaoTDataRepository kakaoTDataRepository;

    public DataInitializer(UserRepository userRepository,
                          CommuteRecordRepository commuteRecordRepository,
                          BusinessTripRepository businessTripRepository,
                          KakaoTDataRepository kakaoTDataRepository) {
        this.userRepository = userRepository;
        this.commuteRecordRepository = commuteRecordRepository;
        this.businessTripRepository = businessTripRepository;
        this.kakaoTDataRepository = kakaoTDataRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 스킵
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("========== 초기 Mock 데이터 삽입 시작 ==========");

        // 1. 사용자 데이터
        createUsers();

        // 2. 출퇴근 기록
        createCommuteRecords();

        // 3. 출장 기록
        createBusinessTrips();

        // 4. 카카오T 데이터
        createKakaoTData();

        System.out.println("========== 초기 Mock 데이터 삽입 완료 ==========");
        System.out.println();
        printLoginInfo();
        printDataSummary();
    }

    private void createUsers() {
        System.out.println("\n[1단계] 사용자 데이터 생성");

        // 사원 1 - 김직원 (개발팀)
        User employee1 = new User();
        employee1.setUsername("employee1");
        employee1.setPassword("1234");
        employee1.setName("김직원");
        employee1.setRole(UserRole.EMPLOYEE);
        employee1.setEmployeeId("EMP001");
        employee1.setDepartment("개발팀");
        userRepository.save(employee1);
        System.out.println("✓ 사원 1: " + employee1.getName() + " (개발팀)");

        // 사원 2 - 이사원 (영업팀)
        User employee2 = new User();
        employee2.setUsername("employee2");
        employee2.setPassword("1234");
        employee2.setName("이사원");
        employee2.setRole(UserRole.EMPLOYEE);
        employee2.setEmployeeId("EMP002");
        employee2.setDepartment("영업팀");
        userRepository.save(employee2);
        System.out.println("✓ 사원 2: " + employee2.getName() + " (영업팀)");

        // 사원 3 - 박사원 (인사팀)
        User employee3 = new User();
        employee3.setUsername("employee3");
        employee3.setPassword("1234");
        employee3.setName("박사원");
        employee3.setRole(UserRole.EMPLOYEE);
        employee3.setEmployeeId("EMP003");
        employee3.setDepartment("인사팀");
        userRepository.save(employee3);
        System.out.println("✓ 사원 3: " + employee3.getName() + " (인사팀)");

        // 컨설턴트 - 관리자
        User consultant = new User();
        consultant.setUsername("consultant");
        consultant.setPassword("admin");
        consultant.setName("관리자");
        consultant.setRole(UserRole.CONSULTANT);
        consultant.setEmployeeId("CONS001");
        consultant.setCompanyId("COMPANY001");
        userRepository.save(consultant);
        System.out.println("✓ 컨설턴트: " + consultant.getName());
    }

    private void createCommuteRecords() {
        System.out.println("\n[2단계] 출퇴근 기록 생성");

        LocalDate today = LocalDate.now();
        int recordCount = 0;

        // 김직원 - 최근 7일 출퇴근
        for (int i = 0; i < 7; i++) {
            CommuteRecord record = new CommuteRecord();
            record.setEmployeeId("EMP001");
            record.setEmployeeName("김직원");
            record.setDepartment("개발팀");
            record.setDate(today.minusDays(i));
            record.setDistance(15.2);

            if (i % 3 == 0) {
                // 친환경 교통수단 (대중교통)
                record.setUsedCar(false);
                record.setVehicleType(null);
                record.setEmissions(0.0);
                record.setPoints(10);
            } else if (i % 3 == 1) {
                // 전기차
                record.setUsedCar(true);
                record.setVehicleType(VehicleType.EV);
                record.setEmissions(0.0);
                record.setPoints(10);
            } else {
                // 내연기관 자가용
                record.setUsedCar(true);
                record.setVehicleType(VehicleType.ICE);
                record.setEmissions(2.63);
                record.setPoints(0);
            }

            commuteRecordRepository.save(record);
            recordCount++;
        }

        // 이사원 - 최근 5일 출퇴근
        for (int i = 0; i < 5; i++) {
            CommuteRecord record = new CommuteRecord();
            record.setEmployeeId("EMP002");
            record.setEmployeeName("이사원");
            record.setDepartment("영업팀");
            record.setDate(today.minusDays(i));
            record.setDistance(20.5);

            if (i % 2 == 0) {
                // 대중교통
                record.setUsedCar(false);
                record.setVehicleType(null);
                record.setEmissions(0.0);
                record.setPoints(10);
            } else {
                // 하이브리드
                record.setUsedCar(true);
                record.setVehicleType(VehicleType.HYBRID);
                record.setEmissions(3.55);
                record.setPoints(0);
            }

            commuteRecordRepository.save(record);
            recordCount++;
        }

        // 박사원 - 최근 6일 출퇴근
        for (int i = 0; i < 6; i++) {
            CommuteRecord record = new CommuteRecord();
            record.setEmployeeId("EMP003");
            record.setEmployeeName("박사원");
            record.setDepartment("인사팀");
            record.setDate(today.minusDays(i));
            record.setDistance(10.8);
            record.setUsedCar(false);
            record.setVehicleType(null);
            record.setEmissions(0.0);
            record.setPoints(10);

            commuteRecordRepository.save(record);
            recordCount++;
        }

        System.out.println("✓ 출퇴근 기록 " + recordCount + "건 생성 완료");
    }

    private void createBusinessTrips() {
        System.out.println("\n[3단계] 출장 기록 생성");

        LocalDate today = LocalDate.now();
        int tripCount = 0;

        // 김직원 - 기차 출장
        BusinessTrip trip1 = new BusinessTrip();
        trip1.setEmployeeId("EMP001");
        trip1.setEmployeeName("김직원");
        trip1.setDepartment("개발팀");
        trip1.setDate(today.minusDays(10));
        trip1.setType(TripType.TRAIN);
        trip1.setDeparture("서울역");
        trip1.setArrival("부산역");
        trip1.setDistance(417.0);
        trip1.setEmissions(14.79); // 417 * 0.03546
        businessTripRepository.save(trip1);
        tripCount++;

        // 이사원 - 비행기 출장
        BusinessTrip trip2 = new BusinessTrip();
        trip2.setEmployeeId("EMP002");
        trip2.setEmployeeName("이사원");
        trip2.setDepartment("영업팀");
        trip2.setDate(today.minusDays(15));
        trip2.setType(TripType.FLIGHT);
        trip2.setDeparture("김포공항");
        trip2.setArrival("제주공항");
        trip2.setDistance(453.0);
        trip2.setEmissions(64.57); // 453 * 0.14253
        businessTripRepository.save(trip2);
        tripCount++;

        // 박사원 - 버스 출장
        BusinessTrip trip3 = new BusinessTrip();
        trip3.setEmployeeId("EMP003");
        trip3.setEmployeeName("박사원");
        trip3.setDepartment("인사팀");
        trip3.setDate(today.minusDays(5));
        trip3.setType(TripType.BUS);
        trip3.setDeparture("서울");
        trip3.setArrival("대전");
        trip3.setDistance(150.0);
        trip3.setEmissions(0.0); // 버스는 0
        businessTripRepository.save(trip3);
        tripCount++;

        // 김직원 - 기차 출장 2
        BusinessTrip trip4 = new BusinessTrip();
        trip4.setEmployeeId("EMP001");
        trip4.setEmployeeName("김직원");
        trip4.setDepartment("개발팀");
        trip4.setDate(today.minusDays(3));
        trip4.setType(TripType.TRAIN);
        trip4.setDeparture("서울역");
        trip4.setArrival("대구역");
        trip4.setDistance(294.0);
        trip4.setEmissions(10.43); // 294 * 0.03546
        businessTripRepository.save(trip4);
        tripCount++;

        System.out.println("✓ 출장 기록 " + tripCount + "건 생성 완료");
    }

    private void createKakaoTData() {
        System.out.println("\n[4단계] 카카오T 데이터 생성");

        LocalDateTime now = LocalDateTime.now();
        int kakaoTCount = 0;

        // 김직원 - 전기차 택시
        KakaoTData data1 = new KakaoTData();
        data1.setEmployeeId("EMP001");
        data1.setEmployeeName("김직원");
        data1.setDepartment("개발팀");
        data1.setUsageDate(now.minusDays(2).withHour(18).withMinute(30));
        data1.setServiceType(KakaoTServiceType.TAXI);
        data1.setVehicleType(VehicleType.EV);
        data1.setRoute("판교역 → 강남역");
        data1.setDistance(15.5);
        data1.setEmissions(0.0);
        data1.setPoints(10);
        kakaoTDataRepository.save(data1);
        kakaoTCount++;

        // 이사원 - 일반 택시
        KakaoTData data2 = new KakaoTData();
        data2.setEmployeeId("EMP002");
        data2.setEmployeeName("이사원");
        data2.setDepartment("영업팀");
        data2.setUsageDate(now.minusDays(4).withHour(14).withMinute(20));
        data2.setServiceType(KakaoTServiceType.TAXI);
        data2.setVehicleType(VehicleType.ICE);
        data2.setRoute("여의도 → 강남");
        data2.setDistance(12.3);
        data2.setEmissions(2.13); // 12.3 * 0.17304
        data2.setPoints(0);
        kakaoTDataRepository.save(data2);
        kakaoTCount++;

        // 박사원 - 자전거
        KakaoTData data3 = new KakaoTData();
        data3.setEmployeeId("EMP003");
        data3.setEmployeeName("박사원");
        data3.setDepartment("인사팀");
        data3.setUsageDate(now.minusDays(1).withHour(12).withMinute(0));
        data3.setServiceType(KakaoTServiceType.BIKE);
        data3.setVehicleType(VehicleType.EV); // 자전거는 배출량 0
        data3.setRoute("홍대입구 → 신촌");
        data3.setDistance(2.8);
        data3.setEmissions(0.0);
        data3.setPoints(10);
        kakaoTDataRepository.save(data3);
        kakaoTCount++;

        // 김직원 - 퀵서비스
        KakaoTData data4 = new KakaoTData();
        data4.setEmployeeId("EMP001");
        data4.setEmployeeName("김직원");
        data4.setDepartment("개발팀");
        data4.setUsageDate(now.minusDays(6).withHour(16).withMinute(45));
        data4.setServiceType(KakaoTServiceType.QUICK);
        data4.setVehicleType(VehicleType.ICE);
        data4.setRoute("사무실 → 클라이언트사");
        data4.setDistance(8.7);
        data4.setEmissions(1.51); // 8.7 * 0.17304
        data4.setPoints(0);
        kakaoTDataRepository.save(data4);
        kakaoTCount++;

        System.out.println("✓ 카카오T 데이터 " + kakaoTCount + "건 생성 완료");
    }

    private void printLoginInfo() {
        System.out.println("=== 로그인 테스트 정보 ===");
        System.out.println("사원 1: username=employee1, password=1234 (개발팀 - 김직원)");
        System.out.println("사원 2: username=employee2, password=1234 (영업팀 - 이사원)");
        System.out.println("사원 3: username=employee3, password=1234 (인사팀 - 박사원)");
        System.out.println("컨설턴트: username=consultant, password=admin");
        System.out.println("=======================");
    }

    private void printDataSummary() {
        System.out.println("\n=== Mock 데이터 요약 ===");
        System.out.println("사용자: " + userRepository.count() + "명");
        System.out.println("출퇴근 기록: " + commuteRecordRepository.count() + "건");
        System.out.println("출장 기록: " + businessTripRepository.count() + "건");
        System.out.println("카카오T 데이터: " + kakaoTDataRepository.count() + "건");

        long totalPoints = commuteRecordRepository.findAll().stream()
                .filter(r -> r.getPoints() != null)
                .mapToInt(CommuteRecord::getPoints)
                .sum();
        totalPoints += kakaoTDataRepository.findAll().stream()
                .filter(k -> k.getPoints() != null)
                .mapToInt(KakaoTData::getPoints)
                .sum();

        System.out.println("총 적립 포인트: " + totalPoints + "P");
        System.out.println("======================");
    }
}

