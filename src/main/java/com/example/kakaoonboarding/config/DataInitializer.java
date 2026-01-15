package com.example.kakaoonboarding.config;

import com.example.kakaoonboarding.entity.*;
import com.example.kakaoonboarding.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 초기 Mock 데이터 삽입 (2019-2026 연도별 추이 데이터 포함)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CommuteRecordRepository commuteRecordRepository;
    private final BusinessTripRepository businessTripRepository;
    private final KakaoTDataRepository kakaoTDataRepository;
    private final Random random = new Random();

    private final String[][] EMPLOYEES = {
        {"employee1", "김직원", "EMP001", "개발팀"},
        {"employee2", "이사원", "EMP002", "영업팀"},
        {"employee3", "박사원", "EMP003", "인사팀"},
        {"employee4", "최대리", "EMP004", "개발팀"},
        {"employee5", "정과장", "EMP005", "영업팀"},
        {"employee6", "강사원", "EMP006", "마케팅팀"},
        {"employee7", "윤대리", "EMP007", "인사팀"},
        {"employee8", "조과장", "EMP008", "개발팀"}
    };

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
        System.out.println("📊 2019-2026년 연도별 추이 데이터 생성 중...");

        createUsers();
        createHistoricalData();

        System.out.println("========== 초기 Mock 데이터 삽입 완료 ==========");
        System.out.println();
        printLoginInfo();
        printDataSummary();
    }

    private void createUsers() {
        System.out.println("\n[1단계] 사용자 데이터 생성");

        for (String[] emp : EMPLOYEES) {
            User user = new User();
            user.setUsername(emp[0]);
            user.setPassword("1234");
            user.setName(emp[1]);
            user.setRole(UserRole.EMPLOYEE);
            user.setEmployeeId(emp[2]);
            user.setDepartment(emp[3]);
            userRepository.save(user);
            System.out.println("✓ " + emp[1] + " (" + emp[3] + ")");
        }

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

    private void createHistoricalData() {
        System.out.println("\n[2단계] 연도별 히스토리 데이터 생성");

        int totalCommute = 0, totalTrip = 0, totalKakaoT = 0;

        // 2019년부터 2026년까지 데이터 생성
        for (int year = 2019; year <= 2026; year++) {
            System.out.println("\n  [" + year + "년] 데이터 생성 중...");

            // 연도별 친환경 비율 (점진적 증가)
            double ecoRatio = getEcoFriendlyRatio(year);

            // 각 직원별 데이터 생성
            for (String[] emp : EMPLOYEES) {
                // 월별 데이터 (1-12월)
                for (int month = 1; month <= 12; month++) {
                    // 출퇴근 데이터 (월 평균 20일)
                    int workDays = 18 + random.nextInt(5); // 18-22일
                    for (int day = 1; day <= workDays && day <= getLastDayOfMonth(year, month); day++) {
                        if (random.nextDouble() < 0.9) { // 90% 확률로 출근 기록
                            createCommuteRecord(emp, year, month, day, ecoRatio);
                            totalCommute++;
                        }
                    }

                    // 출장 데이터 (월 평균 0-2회)
                    int tripCount = random.nextDouble() < 0.3 ? 1 : 0;
                    if (random.nextDouble() < 0.1) tripCount = 2;
                    for (int i = 0; i < tripCount; i++) {
                        int tripDay = 1 + random.nextInt(Math.min(28, getLastDayOfMonth(year, month)));
                        createBusinessTrip(emp, year, month, tripDay);
                        totalTrip++;
                    }

                    // 카카오T 데이터 (2022년부터, 월 평균 0-3회)
                    if (year >= 2022) {
                        int kakaoTCount = random.nextInt(4); // 0-3회
                        for (int i = 0; i < kakaoTCount; i++) {
                            int kakaoTDay = 1 + random.nextInt(Math.min(28, getLastDayOfMonth(year, month)));
                            createKakaoTRecord(emp, year, month, kakaoTDay, ecoRatio);
                            totalKakaoT++;
                        }
                    }
                }
            }

            System.out.println("  ✓ " + year + "년 완료 (친환경 비율: " + String.format("%.0f", ecoRatio * 100) + "%)");
        }

        System.out.println("\n  총 생성 데이터:");
        System.out.println("  - 출퇴근: " + totalCommute + "건");
        System.out.println("  - 출장: " + totalTrip + "건");
        System.out.println("  - 카카오T: " + totalKakaoT + "건");
    }

    // 연도별 친환경 교통수단 이용 비율
    private double getEcoFriendlyRatio(int year) {
        switch (year) {
            case 2019: return 0.15; // 15%
            case 2020: return 0.25; // 25%
            case 2021: return 0.35; // 35%
            case 2022: return 0.50; // 50%
            case 2023: return 0.65; // 65%
            case 2024: return 0.75; // 75%
            case 2025: return 0.85; // 85%
            case 2026: return 0.90; // 90%
            default: return 0.50;
        }
    }

    private void createCommuteRecord(String[] emp, int year, int month, int day, double ecoRatio) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            CommuteRecord record = new CommuteRecord();
            record.setEmployeeId(emp[2]);
            record.setEmployeeName(emp[1]);
            record.setDepartment(emp[3]);
            record.setDate(date);
            record.setDistance(10.0 + random.nextDouble() * 15.0); // 10-25km

            if (random.nextDouble() < ecoRatio) {
                // 친환경 교통수단
                if (random.nextDouble() < 0.7) {
                    // 대중교통/자전거/도보
                    record.setUsedCar(false);
                    record.setVehicleType(null);
                    record.setEmissions(0.0);
                    record.setPoints(10);
                } else {
                    // 전기차
                    record.setUsedCar(true);
                    record.setVehicleType(VehicleType.EV);
                    record.setEmissions(0.0);
                    record.setPoints(10);
                }
            } else {
                // 내연기관 차량
                record.setUsedCar(true);
                if (random.nextDouble() < 0.7) {
                    record.setVehicleType(VehicleType.ICE);
                    record.setEmissions(record.getDistance() * 0.17304);
                } else {
                    record.setVehicleType(VehicleType.HYBRID);
                    record.setEmissions(record.getDistance() * 0.17304);
                }
                record.setPoints(0);
            }

            commuteRecordRepository.save(record);
        } catch (Exception e) {
            // 날짜 오류 무시 (2월 30일 등)
        }
    }

    private void createBusinessTrip(String[] emp, int year, int month, int day) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            BusinessTrip trip = new BusinessTrip();
            trip.setEmployeeId(emp[2]);
            trip.setEmployeeName(emp[1]);
            trip.setDepartment(emp[3]);
            trip.setDate(date);

            String[][] routes = {
                {"서울역", "부산역", "417"},
                {"서울역", "대구역", "294"},
                {"서울", "대전", "150"},
                {"김포공항", "제주공항", "453"},
                {"인천공항", "김해공항", "395"},
                {"서울", "광주", "268"}
            };

            String[] route = routes[random.nextInt(routes.length)];
            trip.setDeparture(route[0]);
            trip.setArrival(route[1]);
            trip.setDistance(Double.parseDouble(route[2]));

            // 출장 유형 선택
            double rand = random.nextDouble();
            if (rand < 0.6) {
                // 기차 (60%)
                trip.setType(TripType.TRAIN);
                trip.setEmissions(trip.getDistance() * 0.03546);
            } else if (rand < 0.8) {
                // 버스 (20%)
                trip.setType(TripType.BUS);
                trip.setEmissions(0.0);
            } else {
                // 비행기 (20%)
                trip.setType(TripType.FLIGHT);
                trip.setEmissions(trip.getDistance() * 0.14253);
            }

            businessTripRepository.save(trip);
        } catch (Exception e) {
            // 날짜 오류 무시
        }
    }

    private void createKakaoTRecord(String[] emp, int year, int month, int day, double ecoRatio) {
        try {
            LocalDateTime dateTime = LocalDateTime.of(year, month, day,
                9 + random.nextInt(12), random.nextInt(60));

            KakaoTData data = new KakaoTData();
            data.setEmployeeId(emp[2]);
            data.setEmployeeName(emp[1]);
            data.setDepartment(emp[3]);
            data.setUsageDate(dateTime);
            data.setDistance(5.0 + random.nextDouble() * 15.0); // 5-20km

            String[][] routes = {
                {"판교역", "강남역"},
                {"여의도", "강남"},
                {"홍대입구", "신촌"},
                {"사무실", "클라이언트사"},
                {"강남역", "선릉역"},
                {"삼성역", "역삼역"}
            };

            String[] route = routes[random.nextInt(routes.length)];
            data.setRoute(route[0] + " → " + route[1]);

            if (random.nextDouble() < 0.3) {
                // 자전거 (30%)
                data.setServiceType(KakaoTServiceType.BIKE);
                data.setVehicleType(VehicleType.EV);
                data.setEmissions(0.0);
                data.setPoints(10);
            } else if (random.nextDouble() < ecoRatio) {
                // 전기차 택시 (친환경 비율에 따라)
                data.setServiceType(random.nextDouble() < 0.8 ? KakaoTServiceType.TAXI : KakaoTServiceType.QUICK);
                data.setVehicleType(VehicleType.EV);
                data.setEmissions(0.0);
                data.setPoints(10);
            } else {
                // 일반 택시/퀵
                data.setServiceType(random.nextDouble() < 0.8 ? KakaoTServiceType.TAXI : KakaoTServiceType.QUICK);
                data.setVehicleType(VehicleType.ICE);
                data.setEmissions(data.getDistance() * 0.17304);
                data.setPoints(0);
            }

            kakaoTDataRepository.save(data);
        } catch (Exception e) {
            // 날짜 오류 무시
        }
    }

    private int getLastDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1).lengthOfMonth();
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
        System.out.println("📅 기간: 2019년 ~ 2026년 (8년)");
        System.out.println("👥 사용자: " + userRepository.count() + "명");
        System.out.println("🚗 출퇴근 기록: " + commuteRecordRepository.count() + "건");
        System.out.println("✈️  출장 기록: " + businessTripRepository.count() + "건");
        System.out.println("🚕 카카오T 데이터: " + kakaoTDataRepository.count() + "건");

        long totalPoints = commuteRecordRepository.findAll().stream()
                .filter(r -> r.getPoints() != null)
                .mapToInt(CommuteRecord::getPoints)
                .sum();
        totalPoints += kakaoTDataRepository.findAll().stream()
                .filter(k -> k.getPoints() != null)
                .mapToInt(KakaoTData::getPoints)
                .sum();

        System.out.println("💰 총 적립 포인트: " + totalPoints + "P");

        // 연도별 통계
        System.out.println("\n📊 연도별 배출량 추이:");
        for (int year = 2019; year <= 2026; year++) {
            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = LocalDate.of(year, 12, 31);

            Double commuteEmissions = commuteRecordRepository.sumEmissionsByDateBetween(start, end);
            Double tripEmissions = businessTripRepository.sumEmissionsByDateBetween(start, end);

            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);
            Double kakaoTEmissions = year >= 2022 ?
                kakaoTDataRepository.sumEmissionsByUsageDateBetween(startDateTime, endDateTime) : 0.0;

            Double total = (commuteEmissions != null ? commuteEmissions : 0.0) +
                          (tripEmissions != null ? tripEmissions : 0.0) +
                          (kakaoTEmissions != null ? kakaoTEmissions : 0.0);

            System.out.println("  " + year + "년: " + String.format("%.2f", total) + " kg/CO₂");
        }

        System.out.println("======================");
    }
}
