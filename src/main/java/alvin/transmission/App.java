package alvin.transmission;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class App 
{
    public void init(int bufferSize, int producerDelaySec, int consumerDelaySec, int producerNumMessage)
    {
        Transmission transmission = new Transmission(bufferSize);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(new Producer(transmission, producerDelaySec, producerNumMessage));
        executorService.submit(new Consumer(transmission, consumerDelaySec));
    }

    public static void main( String[] args )
    {
        App app = new App();
        app.init(5, 1, 2, -1);
        
        while(true)
        {
            try
            {
                Thread.sleep(10000);
                System.out.println("Main sleeping : ");
            }
            catch (InterruptedException e)
            {
                break;
            }
       }
    }

    static class Producer implements Runnable
    {
        private Transmission transmission;
        private MyMessage msg;
        private int delaySec;
        private int numMsg;

        public Producer(Transmission transmission, int delaySec, int numMsg)
        {
            this.transmission = transmission;
            this.msg = new MyMessage();
            this.delaySec = delaySec;
            this.numMsg = numMsg;
        }

        String getRandomString()
        {
            Random random = new Random();
            return random.ints('0', 'z' + 1).filter(i -> (i <= '9' || (i >= 'A' && i <= 'Z') || i >= 'a'))
                    .limit(1000 - random.nextInt(100))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }

        @Override
        public void run()
        {
            int count = 0;

            while(true)
            {
                String s = getRandomString();
                msg.setVariableString(s);

                if (transmission.write(msg) == 0)
                {
                    System.out.println("Produced:" + ":" + s);
                    count++;
                }

                if (numMsg > 0 && count > numMsg)
                {
                    break;
                }

                LockSupport.parkNanos(1000*1000*1000*delaySec);
            }
        }
    }

    static class Consumer implements Runnable, Transmission.MessageMuncher
    {
        private Transmission transmission;
        private int delaySec;

        public Consumer(Transmission transmission, int delaySec)
        {
            this.transmission = transmission;
            this.delaySec = delaySec;
        }

        public boolean on(MyMessage m)
        {
            m.dump("Consumed: ");
            return true;
        }

        @Override
        public void run()
        {
            while(true)
            {
                transmission.read(1, this); // request up to 1 msgs from transmission
                LockSupport.parkNanos(1000*1000*1000*delaySec);
            }
        }
    }
}
