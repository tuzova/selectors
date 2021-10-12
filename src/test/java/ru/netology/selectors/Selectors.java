package ru.netology.selectors;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class RegistrationTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
    }

    // Корректное заполнение формы
    @Test
    void shouldSuccessCorrectAllData() {
        open("http://localhost:7777");
        $("[data-test-id=name] input").setValue("Демьян Александров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    // Ошибка в поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
    @Test
    void shouldFailedWrongDataName() {
        open("http://localhost:7777");
        $("[data-test-id=name] input").setValue("Демьян_моё_имя Александров_моя_фамилия");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    // Ошибка в поле телефон - только цифры (11 цифр), символ + (на первом месте)
    @Test
    void shouldFailedWrongDataPhone() {
        open("http://localhost:7777");
        $("[data-test-id=name] input").setValue("Демьян Александров");
        $("[data-test-id=phone] input").setValue("79270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    // Ошибка - все поля пустые
    @Test
    void shouldFailedEmptyAllData() {
        open("http://localhost:7777");
        $("button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    // Ошибка - поле Фамилия и имя пустое
    @Test
    void shouldFailedEmptyDataName() {
        open("http://localhost:7777");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    // Ошибка - поле Телефон пустое
    @Test
    void shouldFailedEmptyDataPhone() {
        open("http://localhost:7777");
        $("[data-test-id=name] input").setValue("Демьян Александров");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    // Ошибка - флажок согласия должен быть выставлен
    @Test
    void shouldFailedEmptyCheckbox() {
        open("http://localhost:7777");
        $("[data-test-id=name] input").setValue("Демьян Александров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("button").click();
        $("#checkbox").find("#input_invalid");
    }
}
