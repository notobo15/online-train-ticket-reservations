package com.trainticketbooking.app.Utils;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CanBookUtil {
    public static boolean canBook(Set<Integer> desired, List<Set<Integer>> reservedList, Set<Integer> root) {

        desired = sortSet(desired);
        root = sortSet(root);
        List<Set<Integer>> reservedListSort = new ArrayList<>();
        for (var item: reservedList) {
            reservedListSort.add(sortSet(item));
        }

        reservedList = reservedListSort;


        // Kiểm tra xem tất cả các trạm trong phạm vi desired có nằm trong root không
        for (int tram : desired) {
            if (!root.contains(tram)) {
                System.out.println("Phạm vi đặt chỗ nằm ngoài khoảng hợp lệ.");
                return false; // Phạm vi không hợp lệ
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

            // Kiểm tra sự giao nhau giữa phạm vi mong muốn và các phạm vi đã đặt
            Set<Integer> intersection = new HashSet<>(desired);
            intersection.retainAll(reserved);  // Giữ lại các phần tử chung giữa desired và reserved

            // Nếu có sự giao nhau, kiểm tra kích thước của intersection
            if (!intersection.isEmpty()) {
                // Nếu size của intersection là 1, kiểm tra xem có thể nối tiếp hay không
                if (intersection.size() == 1) {
                    // Kiểm tra điều kiện nối tiếp
                    if ((desiredStart == reservedEnd && intersection.contains(reservedEnd)) ||
                            (desiredEnd == reservedStart && intersection.contains(reservedStart))) {
                        continue;  // Nếu điều kiện nối tiếp thỏa mãn, tiếp tục kiểm tra với phần tử tiếp theo trong reservedList
                    }
                }
                return false;  // Nếu có sự giao nhau nhưng không phải điều kiện nối tiếp hợp lệ, không thể đặt chỗ
            }
        }

        return true; // Nếu không có sự trùng lặp, có thể đặt chỗ
    }

    // Hàm để lấy trạm đầu (nhỏ nhất) của một phạm vi
    public static int getStart(Set<Integer> range) {
        // Chuyển Set sang List và sắp xếp
        return range.stream()
//                .sorted()
                .findFirst()  // Lấy phần tử đầu tiên
                .orElseThrow(() -> new RuntimeException("Empty range provided"));
    }

    // Hàm để lấy trạm cuối (lớn nhất) của một phạm vi
    public static int getEnd(Set<Integer> range) {
        // Chuyển Set sang List và sắp xếp ngược
        return range.stream()
                .max(Integer::compareTo)  // Lấy phần tử lớn nhất
                .orElseThrow(() -> new RuntimeException("Empty range provided"));
    }
    public static Set<Integer> sortSet(Set<Integer> set) {
        // Chuyển Set sang TreeSet để duy trì thứ tự sắp xếp
        return set.stream()
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
