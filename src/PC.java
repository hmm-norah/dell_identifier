class PC {
   String model_number, size , processor;
   private String fg_id, serial_id;


   PC(String[] pc_bundle, String fg_id, String serial_id){
       model_number = pc_bundle[0];
       size = pc_bundle[1];
       processor = pc_bundle[2];
       this.fg_id = fg_id;
       this.serial_id = serial_id;
   }

   void display(){
       System.out.println("Model Number: " + model_number + '\n'
       + "Size: " + size + '\n'
       + "Processor: " + processor + '\n'
       + "Free Geek ID: " + fg_id + '\n'
       + "Dell ID: " + serial_id + '\n');
   }
}
