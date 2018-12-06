import org.openqa.selenium.*;
// need to figure out how to execute js in order to get the entries in htmlunitdriver
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scraper {
    WebDriver driver;
    private String processor, size, model_number_entry, fg_id, id;

    Scraper (){
        driver = new ChromeDriver();
    }

    String[] fetch(String id, String fg_id, String[] bundle){
        String URL = "https://www.dell.com/support/home/us/en/04/product-support/servicetag/" + id;
        driver.get(URL);
        processor = "NOT FOUND!";
        size = "NOT FOUND!";
        model_number_entry = "NOT FOUND!";
        this.id = id;
        this.fg_id = fg_id;

        try {
            driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"iframeSurvey\"]")));
            System.out.println("Killing survey window..");

            driver.findElement(By.cssSelector("#buttonsColumn > span:nth-child(2)")).click();
            driver.switchTo().defaultContent();
        }catch(NoSuchElementException not_found){
            //System.out.println("iFrame not found.");
        }

        safeclick("#tab-configuration > a");

        // wild that this mispelling is actually correct
        safeclick("#orginalConfig");

        safeclick("#hrefsubSectionB");

        int i = 1;

        // need to add ability to find how many divs there are instead of this bad method.
        try {
            hunt();
        }catch(NoSuchElementException not_found){
            System.out.println("Ran out of elements! Checking again...");
            try {
                driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
                hunt();
            }catch(NoSuchElementException not_found_again){
                System.out.println("Still couldn't find what I was looking for...");
            }
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

        System.out.println("Model Number: " + model_number_entry + '\n'
                + "Size: " + size + '\n'
                + "Processor: " + processor + '\n'
                + "Free Geek ID: " + fg_id + '\n'
                + "Dell ID: " + id + '\n');

        return bundle;
    }

    private void hunt(){
        int i = 2;

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        //get model number
        try {
            model_number_entry = driver.findElement(By.cssSelector("#pd-support-banner > div > div > div > div > h1 > span")).getText().split(" ")[3];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.print("Model Number not found, please enter: ");
            model_number_entry = new Scanner(System.in).nextLine();
        }

        //get form factor
        String text_entry = driver.findElement(By.cssSelector("#subSectionB > div:nth-child(2) > div > " +
                "div:nth-child(" + 1 + ") > div.bottom-offset-10 > a > span.show-collapsed")).getText();

        if (text_entry.contains("Ultra Small Form Factor EPA") || text_entry.contains("Ultra Small Form  Factor EPA"))
            size = "Ultra Small (US)";
        else if (text_entry.contains("Small Form Facto r") || text_entry.contains("Small Form Factor") || text_entry.contains("Slim Form Factor"))
            size = "Small (S)";
        else if (text_entry.contains("Desktop Base") || text_entry.contains("Desktop EPA") ||
                text_entry.contains("Desktop EPA Base") || text_entry.contains("Desktop"))
            size = "Half-Height (DT)";
        else if (text_entry.contains("MT") || text_entry.contains("Minitower"))

            size = "Mid-Tower (MT)";
        else{
            System.out.println("Model number not found. Select correct type: \n" +
                    "1. Mid-tower\n" +
                    "2. Half-height\n" +
                    "3. Small form factor\n" +
                    "4. Ultra small form factor\n");

            switch(Integer.valueOf(new Scanner(System.in).nextLine())){
                case 1:
                    size = "Mid-Tower (MT)";
                    break;
                case 2:
                    size = "Half-Height (DT)";
                    break;
                case 3:
                    size = "Small (S)";
                    break;
                case 4:
                    size = "Ultra Small (US)";
                    break;
            }
        }


        //search for processor
        try {
            do {

                if(text_entry.toUpperCase().matches("(.*)I5(.*)"))
                    processor = "Core i5";
                else if (text_entry.toUpperCase().matches("(.*)I7(.*)"))
                    processor = "Core i7";
                else if (text_entry.toUpperCase().matches("(.*)I3(.*)"))
                    processor = "Core i3";

                text_entry = driver.findElement(By.cssSelector("#subSectionB > div:nth-child(2) > div > " +
                        "div:nth-child(" + i + ") > div.bottom-offset-10 > a > span.show-collapsed")).getText();
                i += 1;
            } while (processor.equals("NOT FOUND!"));
        }catch(java.util.NoSuchElementException not_found){
            System.out.println("Processor not found, please select a type:\n" +
                    "3. i3\n" +
                    "5. i5\n" +
                    "7. i7\n\n");

            switch(Integer.valueOf(new Scanner(System.in).nextLine())){
                case 3:
                    processor = "Core i3";
                    break;
                case 5:
                    processor = "Core i5";
                    break;
                case 7:
                    processor = "Core i7";
                    break;
            }
        }
    }

    private void safeclick(String selector){
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(By.cssSelector(selector)).click();
        }catch(NoSuchElementException not_found){
            try {
                System.out.println(selector + " not found... waiting");
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
