import java.util.concurrent.FutureTask;

/**
 * @author misterWei
 * @create 2019年09月01号:23点15分
 * @mailbox mynameisweiyan@gmail.com
 */
public class ThreadPooleTest {
/*    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,20,200, TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>(5));
          threadPoolExecutor.execute(() ->{
              try {
                  while (true){
                      Thread.sleep(500l);
                      System.out.println("执行了"+""+Thread.currentThread().getName());
                  }

              } catch (InterruptedException e) {
                  e.printStackTrace();
              }


          });
          threadPoolExecutor.shutdown();
        System.out.println(Thread.currentThread().getName());
    }*/
public static void main(String[] args)throws Exception {
    FutureTask<Integer> futureTask1 = new FutureTask<Integer>(() ->{  Thread.sleep(100000l); return 10; });
    FutureTask<Integer> futureTask2 = new FutureTask<Integer>(() ->{ return 30; });
    new Thread(futureTask1).start();
    new Thread(futureTask2).start();
    System.out.println(futureTask1.get()+futureTask2.get());
    System.out.println(Thread.currentThread().getName());

}

}
