package theArtOfJCP.base;

/**
 * Created by xuran on 16/8/7.
 * 双重检查锁定与延迟初始化
 */
public class DoubleCheckedLocking {
    private static Object instance;     //声明为volatile可真正保证线程安全

    public static Object getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (instance == null) {
                    instance = new Object();     //问题所在
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println("".equals(null));
    }
}
