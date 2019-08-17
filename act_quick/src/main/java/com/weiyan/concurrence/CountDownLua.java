package com.weiyan.concurrence;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.concurrence
 * @date 2019/7/24 15:20
 */
public class CountDownLua {
    static final int num = 1000;
    private int s;
    private Lock lock = new ReentrantLock();
    static Jedis jedis = null;

    public void count(){
        lock.lock();
        for (int i = 1; i <=10 ; i++){
            s+= i;
            System.out.println(Thread.currentThread().getName()+"-"+s);
        }
        lock.unlock();
    }

    @Test
    public void lockUn(){
        try{

            s ++;

            Runnable runnable1 = ()->{
              count();

            };
            new Thread(runnable1).start();
            new Thread(runnable1).start();
            new Thread(runnable1).start();

            Thread.sleep(1000);
            System.err.println(s);
        }catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception {
        Lock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {


            int finalI = i;
            Runnable runnable = () -> {

                try {
                    //到这里阻塞线程
                    countDownLatch.await();
                    //高并发测试
                    {
                        Thread.sleep(1000);
                        System.err.println("限流情况:"+isFlag()+" "+ finalI);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            };
            new Thread(runnable).start();

            try {
                //用来递减
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    static {
         jedis = new Jedis("127.0.0.1");
    }

    public static boolean isFlag() throws Exception{
        File file = new File(CountDownLua.class.getResource("/").toURI().getPath()+"redis.lua");
        String text = FileUtils.readFileToString(file);
        String key = "ip:" + System.currentTimeMillis()/1000; // 当前秒
        String limit = "100"; // 最大限制
        List<String> keys = new ArrayList<String>();
        keys.add(key);
        List<String> args = new ArrayList<String>();
        args.add(limit);
        Long result = (Long)(jedis.eval(text, keys, args)); // 执行lua脚本，传入参数
        return result == 1;
    }

    @Test
    public void redisCon(){
      Jedis jedis = new Jedis("127.0.0.1");
        jedis.set("1","12");
        System.out.println(jedis.get("1"));
    }

    @Test
    public void uuidSize(){
        int i = 10;
        for (int i1 = 0; i1 < i; i1++) {
            System.out.println(UUID.randomUUID().toString().toCharArray().length);
        }

    }
}
