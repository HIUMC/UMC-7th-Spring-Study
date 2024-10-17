package hello.core.singleton;

public class SingletonService {
    //static으로 선언=>클래스에 하나만 존재
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }
    //생성자를 private으로 해서 객체로 생성 못하게 막음.
    //호출할 방법은 이미 만들어진 싱글톤서비스를 반환하는 getInstance 호출 뿐!
    private SingletonService(){

    }

    public void logic(){

    }
}
