public class Tree {
    private Tree left, right;
    private String data;
    private int amount;
    Tree(String data){
        this.data = data;
        amount = 0;
        left = null;
        right = null;
    }

    public void insert(String data){
       Tree temp = new Tree(data);
       insert(temp);
    }

    private Tree insert(Tree temp){
        int result = this.data.compareTo(temp.data);
        return temp;
    }
}

