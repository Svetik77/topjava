package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Stream;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );


        List<UserMealWithExceed> mealWith = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);

        Stream<UserMealWithExceed> secondStream = mealWith.stream();

        secondStream.forEach(x -> System.out.println("date: " + x.getDateTime().getMonth() + " calory "+ x.getCalories()));

    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealWithExceed = new ArrayList<>();
        Map<LocalDate, Integer> calloryPerDayMap = new HashMap<>();

        Stream<UserMeal> firstStream = mealList.stream();

        firstStream.forEach(x -> {
            if (calloryPerDayMap.get(x.getDateTime().toLocalDate()) == null) {
                calloryPerDayMap.put(x.getDateTime().toLocalDate(), x.getCalories());
            } else {
                calloryPerDayMap.put(x.getDateTime().toLocalDate(),
                        (calloryPerDayMap.get(x.getDateTime().toLocalDate()) + x.getCalories()));
            }
        });

        Stream<UserMeal> firstStream2 = mealList.stream();

        firstStream2.forEach(x -> {
            boolean exceeded = false;
            if (calloryPerDayMap.get(x.getDateTime().toLocalDate()) > caloriesPerDay) {
                exceeded = true;
            }

            UserMealWithExceed userMealWithExceed = new UserMealWithExceed(x.getDateTime(), x.getDescription(),
                    x.getCalories(), exceeded);
            if (TimeUtil.isBetween(x.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExceed.add(userMealWithExceed);
            }
        });


        return mealWithExceed;
    }
}
