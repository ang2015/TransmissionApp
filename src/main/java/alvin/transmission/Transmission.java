package alvin.transmission;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Transmission
{
    RingBuffer buffer;
    private volatile boolean slowPath;
    ConcurrentLinkedQueue<MyMessage> slowQueue;

    Transmission(int size)
    {
        buffer = new RingBuffer(size);
        slowPath = false;
        slowQueue = new ConcurrentLinkedQueue<MyMessage>();
    }

    interface MessageMuncher
    {
        boolean on(MyMessage m);
    }

    public void read(int howMany, MessageMuncher m)
    {
        int remaining = howMany;

        for (; remaining>0; remaining--)
        {
            MyMessage item = (MyMessage)buffer.poll();
            if (item != null)
            {
                m.on(item);
            }
            else
            {
                break;
            }
        }

        for (; slowPath && remaining>0; remaining--)
        {
            MyMessage item = slowQueue.poll();
            if (item != null)
            {
                m.on(item);
            }
            else
            {
                break;
            }
        }
    }

    public int write(MyMessage m)
    {
        if (!slowPath)
        {
            if (buffer.offer(m))
            {
                return 0;
            }

            System.out.println("Producer queue full. Switching to slow path");
        }

        MyMessage newMsg = new MyMessage();
        newMsg.copyFrom(m);
        slowQueue.add(newMsg);
        slowPath = true;

        return 0;
    }
}
