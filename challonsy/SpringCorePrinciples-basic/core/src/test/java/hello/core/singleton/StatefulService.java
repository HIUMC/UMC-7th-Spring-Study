package hello.core.singleton;

public class StatefulService {

    //상태를 유지하는 필드
    //private int price;

    public int order(String name, int price){
        System.out.println("name: " + name + ", price: " + price);
        //this.price = price;
        return price;
    }

//    public int getPrice(){
//        return price;
//    }


}
