/*
DD上的每家店铺有自己的营业时间，以"mon 2:02 pm"或者"wed 00:13 am"这样的格式来显示开门和关门时间。
店铺营业期间，DD会每隔5分钟给外卖小哥发消息，提醒可能会出现的订单；而且发消息时的分钟数一定是5的倍数。例如，店铺A营业期间，DD在xx:00会发一次通知；xx:05的时候再发一次；xx:10再发一次；以此类推。
DD会一直给外卖小哥发消息，直到店铺关门才停止。
以某一家店铺的开门时间为例，如果这家店铺开门时间的分钟数不是5的倍数，DD会选择一个离开门时间最近的时候来发送消息。例如，店铺A今天的开门时间是10:03 AM，DD会在10:05 AM的时候发出第一条消息（因为10:05比10:00距离开门时间更近）。 又比如说店铺A今天的开门时间是10:02 AM，DD会在10:00 AM发出第一条消息，即使店铺A实际上还没有开门。
关门时间同理：例如，店铺A今天的关门时间是11:03 PM，DD会在11:05 PM的时候发出最后一条消息，即使店铺A实际上已经关门。又比如说店铺A今天的关门时间是11:02 PM，DD会在11:00 PM发出最后一条消息。
现在希望能写一个function：
输入是一家店铺的开门时间，"xxx xx:xx xx", 和关门时间，"xxx xx:xx xx"；
要求输出一个List，里面是DD为这家店铺发出通知的所有时间（time intervals）。
例子1：
openTime = "mon 10:01 am", closeTime = "mon 10:10 am";
output = {"11000", "11005", "11010"}.
例子2:
openTime = "sat 11:01 pm", closeTime = "sat 11:10 pm";
output = {"62300", "62305", "62310"}.

*/

import java.util.*;

public class DDNotifications {

    // Map weekday string to int (sun = 0, mon = 1, ..., sat = 6)
    private static final Map<String, Integer> dayToIndex = Map.of(
            "sun", 0, "mon", 1, "tue", 2, "wed", 3,
            "thu", 4, "fri", 5, "sat", 6
    );

    public static List<String> getNotificationTimes(String openTime, String closeTime) {
        int open = parseTime(openTime);
        int close = parseTime(closeTime);

        // Round open and close to the nearest 5-minute boundary
        open = roundToNearestFive(open, true);   // open time: round to nearest
        close = roundToNearestFive(close, true); // close time: round to nearest

        List<String> result = new ArrayList<>();

        for (int time = open; time <= close; time = addFiveMinutes(time)) {
            result.add(String.format("%05d", time));
        }

        return result;
    }

    private static int parseTime(String timeStr) {
        String[] parts = timeStr.split(" ");
        String dayStr = parts[0].toLowerCase();
        String[] hm = parts[1].split(":");
        int hour = Integer.parseInt(hm[0]);
        int minute = Integer.parseInt(hm[1]);
        String ampm = parts[2].toLowerCase();

        if (ampm.equals("pm") && hour != 12) hour += 12;
        if (ampm.equals("am") && hour == 12) hour = 0;

        int day = dayToIndex.get(dayStr);
        return day * 10000 + hour * 100 + minute;
    }

    // Round to nearest multiple of 5 based on proximity (if minute==3 → round to 5)
    private static int roundToNearestFive(int time, boolean roundClosest) {
        int day = time / 10000;
        int hour = (time % 10000) / 100;
        int minute = time % 100;

        int lower = (minute / 5) * 5;
        int upper = lower + 5;

        int roundedMin = 0;
        // Decide which is closer to the original minute
        if (minute - lower <= upper - minute) {
          roundedMin = lower;
        } else {
          roundedMin = upper;
        }

        if (roundedMin == 60) {
            hour += 1;
            roundedMin = 0;
        }
        if (hour == 24) {
            hour = 0;
            day = (day + 1) % 7;
        }

        return day * 10000 + hour * 100 + roundedMin;
    }

    // Add 5 minutes to current time
    private static int addFiveMinutes(int time) {
        int day = time / 10000;
        int hour = (time % 10000) / 100;
        int minute = time % 100;

        minute += 5;
        if (minute >= 60) {
            minute = 0;
            hour += 1;
        }
        if (hour == 24) {
            hour = 0;
            day = (day + 1) % 7;
        }

        return day * 10000 + hour * 100 + minute;
    }

    // Test cases
    public static void main(String[] args) {
        System.out.println(getNotificationTimes("mon 10:01 am", "mon 10:10 am"));
        // Output: [11000, 11005, 11010]

        System.out.println(getNotificationTimes("sat 11:01 pm", "sat 11:10 pm"));
        // Output: [62300, 62305, 62310]
    }
}
