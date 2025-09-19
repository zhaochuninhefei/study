package java8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhaochun
 */
public class TestCase06DateAPI {
    public static void main(String[] args) {
        TestCase06DateAPI me = new TestCase06DateAPI();
        me.test01_clockAndTimezone();
        me.test02_local();
    }

    private void test01_clockAndTimezone() {
        // 系统Clock对象 采用系统默认时区
        Clock clock = Clock.systemDefaultZone();
        System.out.println(clock);

        // 系统当前微妙数
        long millis = clock.millis();
        System.out.println(millis);

        // 获取以前的Date对象
        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate);

        // 获取可用时区
        System.out.println(ZoneId.getAvailableZoneIds());
        // 获取指定时区
        ZoneId zoneSh = ZoneId.of("Asia/Shanghai");
        System.out.println(zoneSh.getRules());
        ZoneId zoneTk = ZoneId.of("Asia/Tokyo");
        System.out.println(zoneTk.getRules());
        ZoneId zoneNy = ZoneId.of("America/New_York");
        System.out.println(zoneNy.getRules());
    }

    private void test02_local() {
        // LocalTime 没有年月日和时区信息，只有时分秒及以下
        LocalTime localTimeNowDefault = LocalTime.now(ZoneId.systemDefault());
        System.out.println(localTimeNowDefault);
        LocalTime localTimeNowTk = LocalTime.now(ZoneId.of("Asia/Tokyo"));
        System.out.println(localTimeNowTk);
        // 计算时间差
        long hoursBetween = ChronoUnit.HOURS.between(localTimeNowDefault, localTimeNowTk);
        System.out.println(hoursBetween);
        long minutesBetween = ChronoUnit.MINUTES.between(localTimeNowDefault, localTimeNowTk);
        System.out.println(minutesBetween);
        // 获取一个任意时间的 LocalTime
        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late);
        // 根据格式转换字符串为 LocalTime (因为LocalTime只有小时以下，因此格式有限制，只能用FormatStyle.SHORT)
        DateTimeFormatter dtf_localtime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(Locale.GERMAN);
        LocalTime leetTime = LocalTime.parse("13:37", dtf_localtime);
        System.out.println(leetTime);

        // LocalDate 年月日
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate yesterday = tomorrow.minusDays(2);
        LocalDate new_year_day = LocalDate.of(2020, Month.JANUARY, 1);
        DayOfWeek dayOfWeek = new_year_day.getDayOfWeek();
        System.out.printf("今天是%s，明天是%s，昨天是%s，元旦是%s，%s。 %n", today, tomorrow, yesterday, new_year_day, dayOfWeek);
        // 格式化
        DateTimeFormatter dtf_localdate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
        LocalDate children_day = LocalDate.parse("01.06.2020", dtf_localdate);
        System.out.println(children_day);

        // LocalDateTime 日期加时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        LocalDateTime laborDay = LocalDateTime.of(2020, Month.MAY, 1, 14, 41, 3);
        System.out.println(laborDay);
        System.out.println(laborDay.getDayOfWeek());
        System.out.println(laborDay.getMonth());
        System.out.println(laborDay.getLong(ChronoField.MINUTE_OF_DAY));
        // 通过时间点Instance对象转换为Date
        Instant laborInstant = laborDay.atZone(ZoneId.systemDefault()).toInstant();
        Date laborDate = Date.from(laborInstant);
        System.out.println(laborDate);

        // 自定义格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String strNow = formatter.format(LocalDateTime.now());
        System.out.println(strNow);
        LocalDateTime ldtNow = LocalDateTime.parse(strNow, formatter);
        System.out.println(ldtNow);

        // 计算时间差
        System.out.println(ChronoUnit.DAYS.between(ldtNow, laborDay));
        System.out.println(Duration.between(ldtNow, laborDay).toDays());
    }
}
