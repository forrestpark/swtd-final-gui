import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
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
        System.out.println("current time: " + currentTimeString);

        calendar.setTime(getCurrentDate());
    }

    @Test
    public void testLogInPage() {
        openMainURL();
        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
    }

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

//    the given dummy provider account credentials do not work
//
//    @Test
//    public void testLogInProvider() {
//        openMainURL();
//        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
//        $(Selectors.byId("username")).sendKeys("provider");
//        $(Selectors.byId("password")).sendKeys("qwerty123");
//        $(Selectors.byXpath("//*[text()='Login']")).click();
//        sleep(5000);
//        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
//        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");
//
//        // check if logged in as provider
//        $(Selectors.byCssSelector("p.navbar-nav.ml-auto.navbar-text")).text().contains("provider");
//
//        // log out
//        $(Selectors.byXpath("//*[text()='Log out']")).click();
//        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
//    }
//
//    the given dummy corporate customer account credentials do not work
//    @Test
//    public void testLogInCorporateCustomer() {
//        openMainURL();
//        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
//        $(Selectors.byId("username")).sendKeys("customer_c");
//        $(Selectors.byId("password")).sendKeys("qwerty123");
//        $(Selectors.byXpath("//*[text()='Login']")).click();
//        sleep(5000);
//        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
//        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");
//
//        // check if logged in as corporate customer
//        $(Selectors.byCssSelector("p.navbar-nav.ml-auto.navbar-text")).text().contains("admin");
//
//        // log out
//        $(Selectors.byXpath("//*[text()='Log out']")).click();
//        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
//    }

//    the given dummy retail customer account credentials do not work
//    @Test
//    public void testLogInAdminRetailCustomer() {
//        openMainURL();
//        assertTrue( $(By.className("card-header")).text().contains("Sign in"));
//        $(Selectors.byId("username")).sendKeys("customer_r");
//        $(Selectors.byId("password")).sendKeys("qwerty123");
//        $(Selectors.byXpath("//*[text()='Login']")).click();
//        sleep(5000);
//        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
//        assertEquals(currentUrl, "https://appointmentscheduler2.herokuapp.com/");
//    }

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
    }

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

    @Test
    public void testDateInYellowEqualsToday() {
        login();
        String today_ymd = $(Selectors.byCssSelector("th.fc-day-header.fc-widget-header.fc-mon.fc-today")).getAttribute("data-date");
        long days_diff = MainPage.calculateDayDifference(currentTime, LocalDateTime.parse(today_ymd + " 00:00:00", dtf_ymdhms_dash));
        assertEquals(0, days_diff);

        // not sure how to test that grid is colored in yellow
    }

    @Test
    public void testMonthModeCalendarView() {

        selectMonthMode();
        boolean time_exists = $(Selectors.byCssSelector("fc-axis.fc-time.fc-widget-content")).exists();
        assertFalse(time_exists);

        boolean month_exists = $(Selectors.byCssSelector("fc-scroller.fc-day-grid-container")).exists();
        assertTrue(month_exists);

    }

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

    private static void selectMonthMode() {
        $(Selectors.byCssSelector("button.fc-month-button.fc-button.fc-state-default.fc-corner-left")).click();
    }

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

    @Test
    public void testBackArrowWeeklyMode() {
        // calculate the time difference between current day and the day of past week in the same slot
        login();
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

    @Test
    public void testFrontArrowWeeklyMode() {

    }

    @Test
    public void testTodayButton() {

    }

    @Test
    public void testFirstScreenIsCurrentWeek() {

    }

    @Test
    public void testMonthMode() {

    }

    @Test
    public void testClickOnCalendarAppointmentRedirects() {

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
