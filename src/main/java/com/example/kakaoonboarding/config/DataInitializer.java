package com.example.kakaoonboarding.config;

import com.example.kakaoonboarding.entity.User;
import com.example.kakaoonboarding.entity.UserRole;
import com.example.kakaoonboarding.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 초기 사용자 데이터 삽입
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 스킵
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("========== 초기 사용자 데이터 삽입 시작 ==========");

        // 사원 1 - 김직원 (개발팀)
        User employee1 = new User();
        employee1.setUsername("employee1");
        employee1.setPassword("1234");  // 평문 (실제로는 암호화 필요)
        employee1.setName("김직원");
        employee1.setRole(UserRole.EMPLOYEE);
        employee1.setEmployeeId("EMP001");
        employee1.setDepartment("개발팀");
        userRepository.save(employee1);
        System.out.println("사원 1 생성: " + employee1.getUsername() + " / " + employee1.getName());

        // 사원 2 - 이사원 (영업팀)
        User employee2 = new User();
        employee2.setUsername("employee2");
        employee2.setPassword("1234");
        employee2.setName("이사원");
        employee2.setRole(UserRole.EMPLOYEE);
        employee2.setEmployeeId("EMP002");
        employee2.setDepartment("영업팀");
        userRepository.save(employee2);
        System.out.println("사원 2 생성: " + employee2.getUsername() + " / " + employee2.getName());

        // 사원 3 - 박사원 (인사팀)
        User employee3 = new User();
        employee3.setUsername("employee3");
        employee3.setPassword("1234");
        employee3.setName("박사원");
        employee3.setRole(UserRole.EMPLOYEE);
        employee3.setEmployeeId("EMP003");
        employee3.setDepartment("인사팀");
        userRepository.save(employee3);
        System.out.println("사원 3 생성: " + employee3.getUsername() + " / " + employee3.getName());

        // 컨설턴트 - 관리자
        User consultant = new User();
        consultant.setUsername("consultant");
        consultant.setPassword("admin");
        consultant.setName("관리자");
        consultant.setRole(UserRole.CONSULTANT);
        consultant.setEmployeeId("CONS001");
        consultant.setCompanyId("COMPANY001");
        userRepository.save(consultant);
        System.out.println("컨설턴트 생성: " + consultant.getUsername() + " / " + consultant.getName());

        System.out.println("========== 초기 사용자 데이터 삽입 완료 ==========");
        System.out.println();
        System.out.println("=== 로그인 테스트 정보 ===");
        System.out.println("사원 1: username=employee1, password=1234");
        System.out.println("사원 2: username=employee2, password=1234");
        System.out.println("사원 3: username=employee3, password=1234");
        System.out.println("컨설턴트: username=consultant, password=admin");
        System.out.println("=======================");
    }
}
