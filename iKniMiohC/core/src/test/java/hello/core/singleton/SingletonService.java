package hello.core.singleton;

public class SingletonService {

    private static final SingletonService INSTANCE = new SingletonService();

    public static SingletonService getInstance() {
        return INSTANCE;
    }

    private SingletonService() {

    }

    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }
}
