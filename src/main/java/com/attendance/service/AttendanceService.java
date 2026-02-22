package com.attendance.service;

import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import com.attendance.entity.InCharge;
import com.attendance.repository.AttendanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private static final int MONTHS_TO_RETAIN = 6;

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public Optional<Attendance> markAttendance(Employee employee, LocalDate date, String status, InCharge markedBy) {
        if (attendanceRepository.existsByEmployeeAndAttendanceDate(employee, date)) {
            return Optional.empty(); // Already marked for this day
        }
        Attendance a = new Attendance(employee, date, LocalDateTime.now(), status != null ? status : "PRESENT");
        a.setMarkedBy(markedBy);
        return Optional.of(attendanceRepository.save(a));
    }

    @Transactional
    public Optional<Attendance> updateOrMarkAttendance(Employee employee, LocalDate date, String status, InCharge markedBy) {
        Optional<Attendance> existing = attendanceRepository.findByEmployeeAndAttendanceDate(employee, date);
        if (existing.isPresent()) {
            Attendance a = existing.get();
            a.setStatus(status);
            a.setMarkedAt(LocalDateTime.now());
            a.setMarkedBy(markedBy);
            return Optional.of(attendanceRepository.save(a));
        }
        return markAttendance(employee, date, status, markedBy);
    }

    @Transactional(readOnly = true)
    public Optional<Attendance> getAttendanceForEmployeeOnDate(Employee employee, LocalDate date) {
        return attendanceRepository.findByEmployeeAndAttendanceDate(employee, date);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByEmployee(Employee employee) {
        return attendanceRepository.findByEmployeeOrderByAttendanceDateDesc(employee);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByEmployee(Employee employee, int limit) {
        return attendanceRepository.findByEmployeeOrderByAttendanceDateDesc(employee, PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public long countPresentToday(LocalDate date) {
        return attendanceRepository.countPresentByDate(date);
    }

    @Transactional(readOnly = true)
    public long countAbsentToday(LocalDate date) {
        return attendanceRepository.countAbsentByDate(date);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlyTrends(int months) {
        LocalDate since = LocalDate.now().minusMonths(months);
        List<Attendance> all = attendanceRepository.findAllSince(since);
        Map<YearMonth, long[]> byMonth = new LinkedHashMap<>();
        for (Attendance a : all) {
            YearMonth ym = YearMonth.from(a.getAttendanceDate());
            byMonth.computeIfAbsent(ym, k -> new long[]{0L, 0L});
            if ("PRESENT".equals(a.getStatus())) byMonth.get(ym)[0]++;
            else if ("ABSENT".equals(a.getStatus())) byMonth.get(ym)[1]++;
        }
        List<String> labels = new ArrayList<>();
        List<Long> presentData = new ArrayList<>();
        List<Long> absentData = new ArrayList<>();
        for (Map.Entry<YearMonth, long[]> e : byMonth.entrySet()) {
            labels.add(e.getKey().toString());
            presentData.add(e.getValue()[0]);
            absentData.add(e.getValue()[1]);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("present", presentData);
        result.put("absent", absentData);
        return result;
    }

    /** Data retained for at least six months. History includes who marked. */
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceHistoryForEmployee(Employee employee) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(MONTHS_TO_RETAIN);
        return attendanceRepository.findByEmployeeOrderByAttendanceDateDescWithMarkedBy(employee).stream()
                .filter(a -> !a.getAttendanceDate().isBefore(sixMonthsAgo))
                .collect(Collectors.toList());
    }

    /** Reports: all attendance in date range (visible to everyone). Includes who marked. Minimum 1 month retention. */
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceForReports(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByAttendanceDateBetweenWithMarkedBy(startDate, endDate);
    }
}
