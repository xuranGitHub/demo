package theArtOfJCP.base;

/**
 * Created by xuran on 16/8/7.
 * 延迟初始化的另一种解决方案
 */
public class InstanceFactory {
    private static class InstanceHolder {
        public static Object instance = new Object();
    }

    public static Object getInstance() {
        return InstanceHolder.instance;
    }
}
