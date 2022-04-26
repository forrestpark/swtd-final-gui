import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {

//    public static SelenideElement upload_page = $(By.xpath("//*[text()='File Upload']"));
    public static long calculateDayDifference(LocalDateTime date1, LocalDateTime date2) {
        long diff = Duration.between(date1, date2).toDays();
        return diff;
    }

}
