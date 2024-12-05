package com.trainticketbooking.app;

import com.trainticketbooking.app.Services.impl.TrainService;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class BusBooking {

    public static void main(String[] args) {


        // Khoảng cố định (trạm 1 đến 20)
        final int MIN_TRAM = 1;
        final int MAX_TRAM = 20;

        // Danh sách các phạm vi đã đặt chỗ của nhiều hành khách
        List<Set<Integer>> reservedList = new ArrayList<>();

        // Hành khách 1 đã đặt từ trạm 3 đến 7
        Set<Integer> reserved1 = new HashSet<>();
        for (int i = 3; i <= 7; i++) {
            reserved1.add(i);
        }
        reservedList.add(reserved1); // Thêm phạm vi của hành khách 1 vào danh sách

        // Hành khách 2 đã đặt từ trạm 8 đến 10
        Set<Integer> reserved2 = new HashSet<>();
        for (int i = 8; i <= 10; i++) {
            reserved2.add(i);
        }
        reservedList.add(reserved2); // Thêm phạm vi của hành khách 2 vào danh sách

        // Hành khách 3 muốn đặt từ trạm 7 đến 8
        Set<Integer> desired = new HashSet<>();
        for (int i = 1; i <= 4; i++) {
            desired.add(i);
        }

        // Kiểm tra khả năng đặt chỗ cho hành khách 3
        if (canBook(desired, reservedList, MIN_TRAM, MAX_TRAM)) {
            System.out.println("Hành khách thứ 3 có thể đặt chỗ.");
        } else {
            System.out.println("Hành khách thứ 3 không thể đặt chỗ.");
        }
    }

    // Kiểm tra nếu không có sự trùng lặp giữa phạm vi mong muốn và các phạm vi đã đặt
    // Cũng kiểm tra xem phạm vi có nằm trong khoảng cố định không (MIN_TRAM đến MAX_TRAM)
    public static boolean canBook(Set<Integer> desired, List<Set<Integer>> reservedList, int minTram, int maxTram) {
        // Kiểm tra xem phạm vi của hành khách 3 có nằm trong khoảng hợp lệ không
        for (int tram : desired) {
            if (tram < minTram || tram > maxTram) {
                System.out.println("Phạm vi đặt chỗ của hành khách thứ 3 nằm ngoài khoảng cho phép.");
                return false; // Phạm vi ngoài khoảng hợp lệ
            }
        }

        // Kiểm tra xem phạm vi mong muốn có thể nối tiếp với bất kỳ phạm vi nào đã đặt không
        for (Set<Integer> reserved : reservedList) {
            // Lấy đầu và cuối của phạm vi muốn đặt
            int desiredStart = getStart(desired);
            int desiredEnd = getEnd(desired);

            // Lấy đầu và cuối của phạm vi đã đặt
            int reservedStart = getStart(reserved);
            int reservedEnd = getEnd(reserved);

            // Kiểm tra xem phạm vi mong muốn có thể nối tiếp với phạm vi đã đặt
            if (desiredStart == reservedEnd || desiredEnd == reservedStart) {
                return true;  // Nếu có sự nối tiếp, có thể đặt chỗ
            }

            // Kiểm tra sự giao nhau giữa phạm vi mong muốn và các phạm vi đã đặt
            Set<Integer> intersection = new HashSet<>(desired);
            intersection.retainAll(reserved);  // Giữ lại các phần tử chung giữa desired và reserved

            // Nếu có sự giao nhau, nghĩa là có sự trùng lặp, không thể đặt chỗ
            if (!intersection.isEmpty()) {
                return false;
            }
        }

        return true; // Nếu không có sự trùng lặp, có thể đặt chỗ
    }

    // Hàm để lấy trạm đầu của một phạm vi
    public static int getStart(Set<Integer> range) {
        return range.stream().min(Integer::compareTo).orElseThrow();
    }

    // Hàm để lấy trạm cuối của một phạm vi
    public static int getEnd(Set<Integer> range) {
        return range.stream().max(Integer::compareTo).orElseThrow();
    }
}