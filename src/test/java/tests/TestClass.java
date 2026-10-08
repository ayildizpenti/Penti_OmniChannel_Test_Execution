package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;


import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;



public class TestClass {

    @Test
    void testGoogle() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );

            Page page = browser.newPage();
            page.navigate("https://www.google.com");

            System.out.println(page.title());
            browser.close();
        }
    }

    @Test
    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {

            APIRequestContext request = playwright.request().newContext();

            APIResponse response = request.get(
                    "https://jsonplaceholder.typicode.com/posts/1"
            );
            System.out.println("Status Code: " + response.status());
            System.out.println("Response: " + response.text());
        }
    }
}
