import org.openqa.selenium.*;
// need to figure out how to execute js in order to get the entries in htmlunitdriver
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {
    WebDriver driver;

    Scraper (){
        driver = new ChromeDriver();
    }

    public String[] fetch(String id, String[] bundle){
        String URL = "https://www.dell.com/support/home/us/en/04/product-support/servicetag/" + id;
        driver.get(URL);
        String processor = "NOT FOUND!", size = "NOT FOUND!", model_number_entry = "NOT FOUND!";

        boolean isPresent = driver.findElements(By.cssSelector("#iframeSurvey")).size() > 0;
        if(isPresent){
            driver.findElement(By.cssSelector("#buttonsColumn > span:nth-child(2)")).click();
        }

        safeclick("#tab-configuration > a");

        // wild that this mispelling is actually correct
        safeclick("#orginalConfig");

        safeclick("#hrefsubSectionB");

        int i = 1;

        // need to add ability to find how many divs there are instead of this bad method.
        try {
            while(size.equals("NOT FOUND!") || model_number_entry.equals("NOT FOUND!") || processor.equals("NOT FOUND!")) {
                    String text_entry = driver.findElement(By.cssSelector("#subSectionB > div:nth-child(2) > div > " +
                            "div:nth-child(" + i + ") > div.bottom-offset-10 > a > span.show-collapsed")).getText();

                if (text_entry.contains("Ultra Small Form Factor EPA") || text_entry.contains("Ultra Small Form  Factor EPA"))
                    size = "Ultra Small (US)";
                else if (text_entry.contains("Small Form Facto r") || text_entry.contains("Slim Form Factor"))
                    size = "Small (S)";
                else if (text_entry.contains("Desktop Base") || text_entry.contains("Desktop EPA") ||
                        text_entry.contains("Desktop EPA Base"))
                    size = "Half-Height (DT)";
                else if (text_entry.contains("MT CTO") || text_entry.contains("Minitower EPA Ba se") ||
                        text_entry.contains("Minitower Base"))
                    size = "Mid-Tower (MT)";


                if(text_entry.contains("OptiPlex")){
                    Pattern pattern = Pattern.compile("(?<=\\bOptiPlex\\s)(\\w+)");
                    Matcher matcher = pattern.matcher(text_entry);
                    if (matcher.find())
                        model_number_entry = matcher.group(1);
                }

                if (text_entry.contains("Core i5") || text_entry.contains("CI5"))
                    processor = "Core i5";
                else if (text_entry.contains("Core i7") || text_entry.contains(("CI7")))
                    processor = "Core i7";
                else if (text_entry.contains("Core i3") || text_entry.contains("CORE i3") || text_entry.contains("CI3"))
                    processor = "Core i3";

                ++i;
            }
        }catch(NoSuchElementException not_found){
            System.out.println("Ran out of elements!");
        }

        if(size.equals("NOT FOUND!")){
            System.out.println("Size not found. Select correct size: \n" +
                    "1. Ultra Small Form Factor\n" +
                    "2. Small Form Factor\n" +
                    "3. Half-Height\n" +
                    "4. Mini-Tower\n\n");

            switch(Integer.valueOf(new Scanner(System.in).nextLine())){
                case 1:
                    size = "Ultra Small (US)";
                    break;
                case 2:
                    size = "Small (S)";
                    break;
                case 3:
                    size = "Half-Height (DT)";
                    break;
                case 4:
                    size = "Mid-Tower (MT)";
                    break;

            }

        }

        if(processor.equals("NOT FOUND!")){
            System.out.println("Processor not found. Select correct type: \n" +
                    "1. i7\n" +
                    "2. i5\n" +
                    "3. i3\n\n");

            switch(Integer.valueOf(new Scanner(System.in).nextLine())){
                case 1:
                    processor = "i7";
                    break;
                case 2:
                    processor = "i5";
                    break;
                case 3:
                    processor = "i3";
                    break;
            }

        }

        if(model_number_entry.equals("NOT FOUND!")){
            System.out.println("Model number not found. Enter correct value: ");
            model_number_entry = new Scanner(System.in).nextLine();

        }

        bundle[0] = model_number_entry;
        bundle[1] = size;
        bundle[2] = processor;

        return bundle;
    }

    private void safeclick(String selector){
        try {
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            driver.findElement(By.cssSelector(selector)).click();
        }catch(NoSuchElementException not_found){
            try {
                System.out.println(selector + " not found... waiting");
                driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                driver.findElement(By.cssSelector(selector)).click();
            }catch(NoSuchElementException not_found_again){
                System.out.println("Still couldn't find it... shutting down");
                System.out.println(not_found_again.toString());
                driver.quit();
                System.exit(-1);
            }
        }

    }
}