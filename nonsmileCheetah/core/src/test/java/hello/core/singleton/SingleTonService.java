package hello.core.singleton;

public class SingleTonService {

    private static final SingleTonService instance = new SingleTonService();

    public static SingleTonService getInstance() {
        //접근할 수 있는 것은 이것 뿐
        return instance;
    }
    private SingleTonService() {
        //private 생성자라 외부에서 더이상 생성 못함
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
