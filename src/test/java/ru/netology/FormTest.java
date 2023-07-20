package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldFillTheFormWithSingleName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String endText = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        String expName = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expName, endText.trim());
    }

    @Test
    public void shouldFillTheFormWithSpaces() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл      ");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String endText = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        String expName = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expName, endText.trim());
    }

    @Test
    public void shouldFillTheFormWithValid() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл Кириллов");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String endText = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        String expName = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expName, endText.trim());
    }

    @Test
    public void shouldFillWithDoubleName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл-Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String endText = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        String expName = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expName, endText.trim());
    }

    @Test
    public void shouldBeFailIfNoAgreementButNamePhoneValid() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл Кириллов");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");

        form.findElement(By.className("button_view_extra")).click();

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText());
    }

    @Test
    public void shouldBeFailIfNoAgreementButNameValid() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл Кириллов");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+792700");

        form.findElement(By.className("button_view_extra")).click();

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText());
    }

    @Test
    public void shouldBeFailIfNoAgreementButPhoneValid() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("John");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");

        form.findElement(By.className("button_view_extra")).click();

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText());
    }

    @Test
    public void shouldErrorIfEngName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("cupcake");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldErrorIfUnderlineName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл_");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expName, actName);
    }

    @Test
    public void telephoneShouldBe11More() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000000000");
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expName, actName);
    }

    @Test
    public void telephoneShouldBe11Less() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270");
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expName, actName);
    }

    @Test
    public void telephoneShouldBeWithPlus() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79270000000");
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithNoData() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expName = "Поле обязательно для заполнения";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithFirstFilled() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Поле обязательно для заполнения";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithKoreanName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("龜亀龟");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithNumbersName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("79270000000");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithExpressionName() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("2e^10+42");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expName, actName);
    }


    @Test
    public void shouldNotSuccessWithKoreanPhone() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("龜亀龟");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithCyrillicPhone() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expName, actName);
    }

    @Test
    public void shouldNotSuccessWithExpressionPhone() {
        WebElement form = driver.findElement(By.className("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кирилл");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("2e^10+42");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button_view_extra")).click();

        String actName = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expName, actName);
    }

}