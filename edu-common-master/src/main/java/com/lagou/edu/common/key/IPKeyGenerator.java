package com.lagou.edu.common.key;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPKeyGenerator implements KeyGenerator {

    private static volatile IPKeyGenerator ipKeyGenerator;

    private IPKeyGenerator() {
    }


    private final DefaultKeyGenerator defaultKeyGenerator = new DefaultKeyGenerator();

    static {
        initWorkerId();
    }

    /**
     * 初始化获取ip地址
     */
    static void initWorkerId() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!", e);
        }
        byte[] ipAddressByteArray = address.getAddress();
        DefaultKeyGenerator.setWorkerId((long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF)));
    }

    @Override
    public Number generateKey() {
        return defaultKeyGenerator.generateKey();
    }


    public static IPKeyGenerator getInstance() {

        if (ipKeyGenerator == null) {

            synchronized (IPKeyGenerator.class) {

                if (ipKeyGenerator == null) {

                    ipKeyGenerator = new IPKeyGenerator();

                }

            }

        }

        return ipKeyGenerator;

    }

//    public static void main(String[] args) throws InterruptedException {
//        final int threadCount = 9000;
//        final int loopCount = 10000;
//
//        final List<Number> idCounter = new ArrayList<>(threadCount * loopCount);
//        KeyGenerator idWorker = KeyGeneratorFactory.createKeyGenerator(IPKeyGenerator.class);
//        final CountDownLatch latch = new CountDownLatch(threadCount * loopCount);
//
//        for (int j = 0; j < threadCount; j++) {
//            new Thread(() -> {
//                for (int i = 0; i < loopCount; i++) {
//                    Number id = idWorker.generateKey();
//                    idCounter.add(id);
//                    latch.countDown();
//                }
//
//            }).start();
//        }
//
//        long st = System.currentTimeMillis();
//        latch.await();
//        long en = System.currentTimeMillis();
//      //  System.out.println("idCounter:" + idCounter.size() + ",TIME:" + (en - st));
//        idCounter.forEach(System.out::println);
//    }
}
