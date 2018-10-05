class PC {
   String model_number, size , processor;

   PC(String[] pc_bundle){
       model_number = pc_bundle[0];
       size = pc_bundle[1];
       processor = pc_bundle[2];
   }

   public boolean equals(PC to_compare){
      return model_number.equals(to_compare.model_number) && size.equals(to_compare.size) && processor.equals(to_compare.processor);
   }
}
