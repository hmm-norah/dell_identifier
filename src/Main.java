import java.io.*;
import java.util.*;

public class Main {
    private static Map<String, Map<String, Map<String, Integer>>> map = new HashMap<>();
    private static Scraper scraper = new Scraper();
    private static Map <PC, Integer> systems = new HashMap<>();

    public static void main(String[] args) {
        File file = new File(System.getProperty("user.home") + File.separator + args[0]);
        excel_writer writer = new excel_writer();
        populate(file);
        try {
            writer.write(systems, System.getProperty("user.home") + File.separator);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*
        pretty_print();
        */
    }



    private static void populate(File file){
        Scanner ledger;
        String[] bundle = new String[]{"", "", ""};

        try {
            ledger = new Scanner(file);
        }catch(FileNotFoundException not_found){
            System.out.println("File not found.");
            return;
        }

        // Pump the file
        try {
            String fg_id = ledger.nextLine();
            String dell_id = ledger.nextLine();

            boolean eof = false;
            do {
                scraper.fetch(dell_id, fg_id, bundle);
                PC a_pc = new PC(bundle);


                Set<PC> keys = systems.keySet();
                boolean found = false;
                for(PC pc: keys){
                    keys = systems.keySet();
                    if(pc.equals(a_pc)) {
                        systems.put(pc, systems.get(pc) + 1);
                        found = true;
                        break;
                    }
                }

                if(!found)
                    systems.put(a_pc, 1);

                try {
                    fg_id = ledger.nextLine();
                    dell_id = ledger.nextLine();
                }catch(NoSuchElementException end){
                    eof = true;
                }
            }while(!eof);
        }catch(NoSuchElementException end) {
            System.out.println("File is in unknown format. Should be:\nfg_id\ndell_id");
        }
        scraper.driver.quit();
    }


    private static void tabulate(PC a_pc){
        /*
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
        */

    }


    private static void pretty_print(){
        //ArrayList<String> list = new ArrayList<String>;

        System.out.println("Size\t\t\t\tModel\t\tProcessor\t\tQuantity\t\tTotal");
        int total = 0;
        //int i = 0;
        for (Map.Entry<String, Map<String, Map<String, Integer>>> sub_map : map.entrySet()){
            System.out.print(sub_map.getKey() + "\n");
            //list.add(sub_map.getKey());
            for (Map.Entry<String, Map<String, Integer>> sub_sub_map : sub_map.getValue().entrySet()){
                System.out.print("\t\t\t\t\t" + sub_sub_map.getKey() + "\t\t");
                //list.set(i, list.get(i).concat("," + sub_sub_map.getKey());
                for(Map.Entry<String, Integer> sub_sub_sub_map : sub_sub_map.getValue().entrySet()){
                    System.out.println(sub_sub_sub_map.getKey() + "\t\t\t" + sub_sub_sub_map.getValue());
                    //i = tab[i].concat("," + sub_sub_sub_map.getKey() + "," + sub_sub_sub_map.getValue());
                    total += sub_sub_sub_map.getValue();
                }

            }

        }
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + total);

    }

}
