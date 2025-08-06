package hello.core.singleton;

public class SingleTonService {
    // static 영역에 객체가 한 개만 생성되도록 함
    private static final SingleTonService instance = new SingleTonService();

    public static SingleTonService getInstance() {
        return instance;
    }

    // 외부에서 직접 객체를 생성하지 못하게 함
    private SingleTonService() {}

    public void logic () {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
