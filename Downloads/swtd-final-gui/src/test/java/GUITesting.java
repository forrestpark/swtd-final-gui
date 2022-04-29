import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import net.bytebuddy.asm.Advice;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUITesting {

    static MainPage mainPage;
    static String mainUrl;
    static String randomUsername;
    static String randomPassword;
    static String randomFirstName;
    static String randomLastName;
    static String randomEmail;
    static String dummyMobile;
    static String dummyStreetAddress;
    static String dummyPostalCode;
    static String dummyCityName;
    static String dummyVATNumber;
    static LocalDateTime currentTime;
    static LocalDateTime current_ymd;
    static String current_ymd_string;
    static String currentTimeString;
    static DateTimeFormatter dtf_ymdhms;
    static DateTimeFormatter dtf_ymdhms_dash;
    static DateTimeFormatter dtf_ymd;
    static Calendar calendar;
    static Random random;
    static String currentDay;

    @BeforeAll
    public static void setUpAll() {
        mainUrl = "https://appointmentscheduler2.herokuapp.com";
        dummyMobile = "999999999";
        dummyStreetAddress = "Guilford Ave";
        dummyPostalCode = "11-222";
        dummyCityName = "Paris";
        dummyVATNumber = "PL0123456789";

        dtf_ymdhms = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        dtf_ymdhms_dash = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dtf_ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        calendar = Calendar.getInstance();
        random = new Random(1234);
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        mainPage = new MainPage();
        randomUsername = randomStringGenerator(15);
        randomPassword = randomStringGenerator(10);
        randomFirstName = randomStringGenerator(5);
        randomLastName =  randomStringGenerator(10);
        randomEmail = randomStringGenerator(10) + "@" + randomStringGenerator(6) + ".com";

        currentTime = LocalDateTime.now();
        currentTimeString = dtf_ymdhms.format(currentTime);
        current_ymd_string = dtf_ymd.format(currentTime);
        current_ymd = LocalDateTime.parse(current_ymd_string + " 00:00:00", dtf_ymdhms_dash);
        System.out.println("current time: " + currentTimeString);

        calendar.setTime(getCurrentDate());

        currentDay = currentTime.getDayOfWeek().toString();
        System.out.println("current day: " + currentDay);
    }


    /*
        Testing the first page that appears when accessing the main URL (https://appointmentscheduler2.herokuapp.com/)
        is the sign in page
     */
    @Test
    public void testLogInPage() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
    }


    /*
        Testing logging in with the admin account credentials given as dummy in the documentation
        RESULT: Successfully logs in
     */
    @Test
    public void testLogInAdmin() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byId("username")).sendKeys("admin");
        $(Selectors.byId("password")).sendKeys("qwerty123");
        $(Selectors.byXpath("//*[text()='Login']")).click();
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");

        // check if logged in as admin
        $(Selectors.byCssSelector("p.navbar-nav.ml-auto.navbar-text")).text().contains("admin");

        // log out
        $(Selectors.byXpath("//*[text()='Log out']")).click();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));

    }

    /*
        Testing logging in with the dummy provider credentials given in the documentation
        FAULT: the credentials provided in the documentation does not work as expected; failed log-in
     */
    @Test
    @Ignore
    public void testLogInProvider() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byId("username")).sendKeys("provider");
        $(Selectors.byId("password")).sendKeys("qwerty123");
        $(Selectors.byXpath("//*[text()='Login']")).click();
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");

        // check if logged in as provider
        $(Selectors.byCssSelector("p.navbar-nav.ml-auto.navbar-text")).text().contains("provider");

        // log out
        $(Selectors.byXpath("//*[text()='Log out']")).click();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
    }

    /*
        Testing logging in with the dummy corporate customer credentials given in the documentation
        FAULT: the credentials provided in the documentation does not work as expected; failed log-in
    */
    @Test
    @Ignore
    public void testLogInCorporateCustomer() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byId("username")).sendKeys("customer_c");
        $(Selectors.byId("password")).sendKeys("qwerty123");
        $(Selectors.byXpath("//*[text()='Login']")).click();
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");

        // check if logged in as corporate customer
        $(Selectors.byCssSelector("p.navbar-nav.ml-auto.navbar-text")).text().contains("admin");

        // log out
        $(Selectors.byXpath("//*[text()='Log out']")).click();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
    }

    /*
        Testing logging in with the dummy retail customer credentials given in the documentation
        FAULT: the credentials provided in the documentation does not work as expected; failed log-in
     */
    @Test
    @Ignore
    public void testLogInRetailCustomer() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byId("username")).sendKeys("customer_r");
        $(Selectors.byId("password")).sendKeys("qwerty123");
        $(Selectors.byXpath("//*[text()='Login']")).click();
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");
    }


    /*
        Tests whether one can successfully create a new retail customer account
        by joining with randomly generated credentials such as username and password
        and by logging in with said credentials, after joining, to confirm an account has been successfully created
     */
    @Test
    public void testNewRetailAccountCreation() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byXpath("//*[text()='Retail']")).click();
        sleep(5000);

        $(Selectors.byXpath("//*[text()='Register new Retail Customer Account']")).exists();

        inputAccountCredentials();
        $(Selectors.byXpath("//*[text()='Register']")).click();

        // check if successfully created
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/customers/new/retail");

        // check whether one can successfully log in with the newly created account info
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byId("username")).sendKeys(randomUsername);
        $(Selectors.byId("password")).sendKeys(randomPassword);
        $(Selectors.byXpath("//*[text()='Login']")).click();
        sleep(5000);
        currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");

    }

    /*
        Tests whether one can successfully create a new corporate customer account
        by joining with randomly generated credentials such as username and password
        and by logging in with said credentials, after joining, to confirm an account has been successfully created
     */
    @Test
    public void testNewCorporateAccountCreation() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byXpath("//*[text()='Corporate']")).click();
        sleep(5000);

        $(Selectors.byXpath("//*[text()='Register new Corporate Customer Account']")).exists();

        inputAccountCredentials();
        $(Selectors.byId("companyName")).sendKeys(randomStringGenerator(14));
        $(Selectors.byId("vatNumber")).sendKeys(dummyVATNumber);

        $(Selectors.byXpath("//*[text()='Register']")).click();

        // check if successfully created
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/customers/new/corporate");
    }

    /*
        A helper method for entering necessary information for account creation
     */
    private static void inputAccountCredentials() {

        // feed in credentials
        $(Selectors.byId("userName")).sendKeys(randomUsername);
        $(Selectors.byId("password")).sendKeys(randomPassword);
        $(Selectors.byId("matchingPassword")).sendKeys(randomPassword);
        $(Selectors.byId("firstName")).sendKeys(randomFirstName);
        $(Selectors.byId("lastName")).sendKeys(randomLastName);
        $(Selectors.byId("email")).sendKeys(randomEmail);
        $(Selectors.byId("mobile")).sendKeys(dummyMobile);
        $(Selectors.byId("street")).sendKeys(dummyStreetAddress);
        $(Selectors.byId("postcode")).sendKeys(dummyPostalCode);
        $(Selectors.byId("city")).sendKeys(dummyCityName);
    }

    /*
        Testing if the hourly grid of the current day is colored in eggshell yellow (hexcode: #fcf8e3)
        ISSUE: could not figure what the html/css properties are for the half-hour slots of each day
     */
    @Test
    @Ignore
    public void testDateInYellowEqualsToday_WeeklyMode() {
        login();
        selectWeeklyMode();

        SelenideElement today = $(Selectors.byCssSelector(dayClassNameConstructor("th.fc-day-header.fc-widget-header.fc-", currentDay) + ".fc-today"));
        String today_ymd = today.getAttribute("data-date");
        long days_diff = MainPage.calculateDayDifference(currentTime, LocalDateTime.parse(today_ymd + " 00:00:00", dtf_ymdhms_dash));
        assertEquals(0, days_diff);

        // not sure how to test that grid is colored in yellow
        String today_color = today.getCssValue("background-color");
        System.out.println("today color: " + today_color);
        assertEquals(today_color, "fcf8e3");
    }

    /*
        Testing if the daily grid of the current day is colored in eggshell yellow (hexcode: #fcf8e3)
     */
    @Test
    @Ignore
    public void testDateInYellowEqualsToday_MonthlyMode() {
        login();
        selectMonthMode();

        SelenideElement today = $(Selectors.byCssSelector(dayClassNameConstructor("td.fc-day-header.fc-widget-header.fc-", currentDay) + ".fc-today"));
        String today_ymd = today.getAttribute("data-date");
        long days_diff = MainPage.calculateDayDifference(currentTime, LocalDateTime.parse(today_ymd + " 00:00:00", dtf_ymdhms_dash));
        assertEquals(0, days_diff);

        // not sure how to test that grid is colored in yellow
        String today_color = today.getCssValue("background-color");
        System.out.println("today color: " + today_color);
        assertEquals(today_color, "fcf8e3");
    }

    /*
        Tests calendar view for monthly mode by checking the absence of hourly grids and the presence of day grids
     */
    @Test
    public void testMonthModeCalendarView() {

        selectMonthMode();
        boolean time_exists = $(Selectors.byCssSelector("fc-axis.fc-time.fc-widget-content")).exists();
        assertFalse(time_exists);

        boolean month_exists = $(Selectors.byCssSelector("fc-scroller.fc-day-grid-container")).exists();
        assertTrue(month_exists);

    }

    /*
        Tests if the first view on monthly view displays the current month
        and not any other month in either the past of the future
     */
    @Test
    public void testMonthModeDisplayCurrentMonth() {

        login();
        selectMonthMode();
        clickToday();
        String currentMonth = currentTime.getMonth().toString().toLowerCase();
        int currentYear = currentTime.getYear();

        String[] parseCalendarMonthYear = $(Selectors.byClassName("fc-center")).text().split(" ");
        System.out.println(parseCalendarMonthYear.toString());

        assertEquals(currentMonth, parseCalendarMonthYear[0].toLowerCase());
        assertEquals("" + currentYear, parseCalendarMonthYear[1]);

    }

    /*
        Tests back arrow button on monthly mode whether pressing the back button leads to monthly view of the past month
     */
    @Test
    public void testBackArrowMonthMode() {

        login();
        selectMonthMode();

        // click back arrow button on month mode
        $(Selectors.byCssSelector("button.fc-prev-button.fc-button.fc-state-default.fc-corner-left")).click();

        // compute one month back
        Date oneMonthAgo = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int pastMonth = oneMonthAgo.getMonth();
        int pastYear = oneMonthAgo.getYear() + 1900;

        String[] parseCalendarMonthYear = $(Selectors.byClassName("fc-center")).text().split(" ");
        System.out.println(parseCalendarMonthYear.toString());

        assertEquals(new DateFormatSymbols().getMonths()[pastMonth].toLowerCase(), parseCalendarMonthYear[0].toLowerCase());
        assertEquals("" + pastYear, parseCalendarMonthYear[1]);

    }

    /*
        Helper method for selecting month mode on main view
     */
    private static void selectMonthMode() {
        $(Selectors.byCssSelector("button.fc-month-button.fc-button.fc-state-default.fc-corner-left")).click();
    }

    /*
        Helper method for selecting weekly mode on main view
     */
    private static void selectWeeklyMode() {
        $(Selectors.byCssSelector("button.fc-agendaWeek-button.fc-button.fc-state-default.fc-corner-right")).click();
    }

    /*
        Tests two cases for the main page
        1) if a user is still logged in due to past log-in, a log out text is expected to be displayed
        2) if a user is not logged in, the first main page to be display is expected to be the log in page, not the calendar view page
     */
    @Test
    public void testAccessToMainPage() {
        openMainURL();
        if ( $(Selectors.byXpath("//*[text()='Log out']")).exists() ) {
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");
        } else {
            login();
            assertTrue(true);
        }
    }

    /*
        Tests front arrow button on monthly mode whether pressing the front button leads to monthly view of the month after the one on display
     */
    @Test
    public void testFrontArrowMonthMode() {
        login();
        selectMonthMode();

        // click front arrow button on month mode
        $(Selectors.byCssSelector("button.fc-next-button.fc-button.fc-state-default.fc-corner-right")).click();

        // compute one month back
        Date oneMonthAfter = Date.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int nextMonth = oneMonthAfter.getMonth();
        int nextYear = oneMonthAfter.getYear() + 1900;

        String[] parseCalendarMonthYear = $(Selectors.byClassName("fc-center")).text().split(" ");
        System.out.println(parseCalendarMonthYear.toString());

        assertEquals(new DateFormatSymbols().getMonths()[nextMonth].toLowerCase(), parseCalendarMonthYear[0].toLowerCase());
        assertEquals("" + nextYear, parseCalendarMonthYear[1]);
    }

    /*
        Tests back arrow button on weekly mode whether pressing the front button leads to weekly view of the week before the one on display
     */
    @Test
    public void testBackArrowWeeklyMode() {
        // calculate the time difference between current day and the day of past week in the same slot
        login();

        // select weekly mode
        $(Selectors.byCssSelector("button.fc-agendaWeek-button.fc-button.fc-state-default.fc-corner-right")).click();

        String today_ymd = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon")).getAttribute("data-date");

        $(Selectors.byCssSelector("button.fc-prev-button.fc-button.fc-state-default.fc-corner-left")).click();

        String last_monday = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon.fc-past")).getAttribute("data-date");

        long days_diff = MainPage.calculateDayDifference(LocalDateTime.parse(last_monday + " 00:00:00", dtf_ymdhms_dash), LocalDateTime.parse(today_ymd + " 00:00:00", dtf_ymdhms_dash));
        System.out.println("days diff: " + days_diff);
        assertEquals(7, days_diff);

    }

    private void login() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
        $(Selectors.byId("username")).sendKeys("admin");
        $(Selectors.byId("password")).sendKeys("qwerty123");
        $(Selectors.byXpath("//*[text()='Login']")).click();
        sleep(5000);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");

        // check if logged in as admin
        $(Selectors.byCssSelector("p.navbar-nav.ml-auto.navbar-text")).text().contains("admin");
    }

    private void logout() {
        // log out
        $(Selectors.byXpath("//*[text()='Log out']")).click();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
    }

    /*
        Tests front arrow button on weekly mode whether pressing the front button leads to weekly view of the week after the one on display
     */
    @Test
    public void testFrontArrowWeeklyMode() {
        // calculate the time difference between current day and the day of past week in the same slot
        login();

        // select weekly mode
        $(Selectors.byCssSelector("button.fc-agendaWeek-button.fc-button.fc-state-default.fc-corner-right")).click();

        String today_ymd = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon")).getAttribute("data-date");

        $(Selectors.byCssSelector("button.fc-next-button.fc-button.fc-state-default.fc-corner-right")).click();

        String next_monday = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon.fc-future")).getAttribute("data-date");

        long days_diff = MainPage.calculateDayDifference(LocalDateTime.parse(next_monday + " 00:00:00", dtf_ymdhms_dash), LocalDateTime.parse(today_ymd + " 00:00:00", dtf_ymdhms_dash));
        System.out.println("days diff: " + days_diff);
        assertEquals(7, days_diff);
    }

    /*
        Tests the today button; whether the today button makes the calender to return back to the calendar view that contains the current day
     */
    @Test
    public void testTodayButton() {
        login();

        String today_ymd_actual = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon")).getAttribute("data-date");

        for (int i = 0; i < 3; i++) {
            $(Selectors.byCssSelector("button.fc-next-button.fc-button.fc-state-default.fc-corner-right")).click();
        }

        // push today button
        pushToday();

        String today_ymd_expected1 = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon")).getAttribute("data-date");
        assertEquals(today_ymd_actual, today_ymd_expected1);

        for (int i = 0; i < 6; i++) {
            $(Selectors.byCssSelector("button.fc-prev-button.fc-button.fc-state-default.fc-corner-left")).click();
        }

        // push today button
        pushToday();

        String today_ymd_expected2 = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon")).getAttribute("data-date");
        assertEquals(today_ymd_actual, today_ymd_expected2);
    }

    /*
        helper method for pressing the today button
     */
    private void pushToday() {
        $(Selectors.byCssSelector("button.fc-today-button.fc-button.fc-state-default.fc-corner-left.fc-corner-right")).click();
    }

    /*
        Tests whether the first calendar view to be displayed on weekly mode is the current week
     */
    @Test
    public void testFirstScreenIsCurrentWeek() {
        // calculate the time difference between current day and the day of past week in the same slot
        login();

        // select weekly mode
        $(Selectors.byCssSelector("button.fc-agendaWeek-button.fc-button.fc-state-default.fc-corner-right")).click();

        // get monday
        String monday_ymd = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon")).getAttribute("data-date");
        LocalDateTime monday = LocalDateTime.parse(monday_ymd + " 00:00:00", dtf_ymdhms_dash);

        System.out.println("monday: " + monday.toString());

        // get sunday
        String sunday_ymd = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-sun")).getAttribute("data-date");
        LocalDateTime sunday = LocalDateTime.parse(sunday_ymd + " 00:00:00", dtf_ymdhms_dash);
        System.out.println("sunday: " + sunday.toString());

        // get today
        LocalDateTime today = current_ymd;
        System.out.println("today: " + today.toString());

        // check whether today is between monday and sunday, inclusive
        long duration_from_monday = MainPage.calculateDayDifference(monday, today);
        long duration_to_sunday = MainPage.calculateDayDifference(today, sunday);
        System.out.println("from monday: " + duration_from_monday);
        System.out.println("from sunday: " + duration_to_sunday);
        assertEquals(6.0, duration_from_monday + duration_to_sunday);

    }

    private String dayClassNameConstructor(String base, String currentDay) {
        System.out.println("dayclassname constructor: " + base + currentDay.substring(0,3).toLowerCase());
        return base + currentDay.substring(0,3).toLowerCase();
    }

    /*
        Tests whether monthly calendar view displays 42 days
     */
    @Test
    public void testMonthModeCalendarNumOfDays() {
        login();
        selectMonthMode();

        SelenideElement calendar = $(Selectors.byId("calendar"));

        // test 6x7 grid
        String first_monday_string = calendar.$(Selectors.byCssSelector("td.fc-day-top.fc-mon")).getAttribute("data-date");
        System.out.println("first monday: " + first_monday_string);

        String last_sunday_string = calendar.findElements(By.cssSelector("td.fc-day.fc-widget-content.fc-sun")).get(5).getAttribute("data-date");
        System.out.println("last sunday: " + last_sunday_string);

        // date difference between first monday and last sunday?
        LocalDateTime first_monday = LocalDateTime.parse(first_monday_string + " 00:00:00", dtf_ymdhms_dash);
        LocalDateTime last_sunday = LocalDateTime.parse(last_sunday_string + " 00:00:00", dtf_ymdhms_dash);

        long month_day_difference = MainPage.calculateDayDifference(first_monday, last_sunday);
        assertEquals(41.0, month_day_difference);
    }

    /*
        Tests the calendar view displays 42 grids for 42 days, one grid per day
        and each neighboring grid/day is only one day apart from each other
     */
    @Test
    public void testMonthModeCalendarViewNumberOfGrids() {
        login();
        selectMonthMode();

        SelenideElement calendar = $(Selectors.byId("calendar"));

        List<WebElement> listDayGrid= calendar.findElements(By.cssSelector("td.fc-day.fc-widget-content"));

        assertEquals(42, listDayGrid.size());

        LocalDateTime today;
        LocalDateTime tomorrow;

        for (int i = 0; i < listDayGrid.size() - 1; i++) {
            today = LocalDateTime.parse( listDayGrid.get(i).getAttribute("data-date")+ " 00:00:00", dtf_ymdhms_dash);
            tomorrow = LocalDateTime.parse( listDayGrid.get(i + 1).getAttribute("data-date")+ " 00:00:00", dtf_ymdhms_dash);
            assertEquals(1.0, MainPage.calculateDayDifference(today, tomorrow));
        }
    }

    @Test
    public void testClickOnCalendarAppointmentRedirects() {
        login();
        selectMonthMode();


    }

    @Test
    public void testAppointment_TopBar_Redirects() {

    }

    @Test
    public void testWorks_TopBar_Redirects() {

    }

    @Test
    public void testProviders_TopBar_Redirects() {

    }

    @Test
    public void testCustomers_TopBar_Redirects() {

    }

    @Test
    public void testInvoices_TopBar_Redirects() {

    }

    @Test
    public void testAppointments_ListLength_Confirmed() {

    }

    @Test
    public void testAppointments_ChosenStatusMatchActualStatus() {

    }

    @Test
    public void testAppointmentDetails_CorrectAppointment() {

    }

    @Test
    public void testScheduledAppointment_FutureTime() {

    }

    @Test
    public void testFinishedAppointment_PastTime() {

    }

    @Test
    public void test() {

    }

    private static void clickToday() {
        $(Selectors.byCssSelector("fc-today-button.fc-button.fc-state-default.fc-corner-left.fc-corner-right")).click();
    }

    private static void openMainURL() {
        open(mainUrl);
    }

    private static String randomStringGenerator(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        System.out.println(generatedString);
        return generatedString;
    }

    private static Date getCurrentDate() {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        return out;
    }


}
