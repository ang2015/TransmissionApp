package alvin.transmission;

public class RingBuffer {
    private final int size;
    private final MyMessage[] data;
    private volatile int writeIdx, readIdx;

    public RingBuffer(int size) {
        this.size = (size < 1) ? 10 : size;
        this.data = new MyMessage[this.size];
        for (int i = 0; i < this.data.length; i++)
        {
            this.data[i] = new MyMessage();
        }
        this.readIdx = 0;
        this.writeIdx = -1;
    }

    public boolean offer(MyMessage element)
    {
        final boolean isFull = (writeIdx - readIdx) + 1 >= size;

        if (!isFull)
        {
            int nextWriteSeq = writeIdx + 1;
            data[nextWriteSeq % size].copyFrom(element);

            writeIdx++;
            return true;
        }

        return false;
    }

    public MyMessage poll()
    {
        final boolean isEmpty = writeIdx < readIdx;

        if (!isEmpty)
        {
            MyMessage nextValue = data[readIdx % size];
            readIdx++;
            return nextValue;
        }

        return null;
    }

    public int size()
    {
        return (writeIdx - readIdx) + 1;
    }
}
