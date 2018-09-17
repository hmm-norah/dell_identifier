import java.io.*;
import java.util.*;

public class Main {
    private static Map<String, Map<String, Map<String, Integer>>> map = new HashMap<>();

    public static void main(String[] args) {

        Scraper scraper = new Scraper();
        String[] bundle = new String[] {"", "", ""};
        Scanner ledger;
        List<PC> list = new ArrayList<>();


        File file = new File("/Users/norah/" + args[0]);
        try {
            ledger = new Scanner(file);
        }catch(FileNotFoundException not_found){
            System.out.println("File not found.");
            return;
        }

        String fg_id = ledger.nextLine();
        String dell_id = ledger.nextLine();
        boolean eof = false;

        while(!eof){
            scraper.fetch(dell_id, bundle);
            PC a_pc = new PC(bundle, fg_id, dell_id);
            a_pc.display();
            list.add(a_pc);

            tabulate(a_pc);

            try {
                fg_id = ledger.nextLine();
                dell_id = ledger.nextLine();
            }catch(NoSuchElementException end){
                eof = true;
            }
        }

        scraper.driver.quit();
        System.out.println("Done?");
    }

    private static void tabulate(PC a_pc){
        if(map.containsKey(a_pc.size)){
            if(map.get(a_pc.size).containsKey(a_pc.model_number)){
                if(map.get(a_pc.size).get(a_pc.model_number).containsKey(a_pc.processor)) {
                    map.get(a_pc.size).get(a_pc.model_number).put(a_pc.processor, map.get(a_pc.size).get(a_pc.model_number).get(a_pc.processor) + 1);
                }else{
                    map.get(a_pc.size).get(a_pc.model_number).put(a_pc.processor, 1);
                }
            }else {
                map.get(a_pc.size).put(a_pc.model_number, new HashMap<>() {{ put(a_pc.processor, 1); }});
            }
        }else{
            map.put(a_pc.size, new HashMap<>() {{put(a_pc.model_number, new HashMap<>() {{put(a_pc.processor, 1);}});}});
        }
        System.out.println(map);

    }

}