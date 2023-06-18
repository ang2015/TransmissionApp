package alvin.transmission;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class RingBufferTest 
{
    @Test
    public void SimpleEnqueue()
    {
        RingBuffer rb = new RingBuffer(2);

        MyMessage m = new MyMessage();
        assertTrue(rb.offer(m));
        assertEquals(1, rb.size());
    }

    @Test
    public void SimpleDequeue()
    {
        RingBuffer rb = new RingBuffer(2);

        MyMessage m = new MyMessage();
        assertTrue(rb.offer(m));
        MyMessage res = rb.poll();
        assertEquals("variable_string_initial_value", res.getVariableString());
    }

    @Test
    public void EnqueueTooMuch()
    {
        RingBuffer rb = new RingBuffer(2);
        
        MyMessage m = new MyMessage();
        assertTrue(rb.offer(m));
        assertTrue(rb.offer(m));
        assertFalse(rb.offer(m));
    }

    @Test
    public void EmptyQueueDequeue()
    {
        RingBuffer rb = new RingBuffer(2);

        MyMessage res = rb.poll();
        assertNull(res);
    }

    @Test
    public void FullQueueDequeueEnqueue()
    {
        RingBuffer rb = new RingBuffer(2);

        MyMessage m = new MyMessage();
        m.setVariableString("abc");
        assertTrue(rb.offer(m));
        m.setVariableString("def");
        assertTrue(rb.offer(m));
        MyMessage res = rb.poll();
        assertEquals("abc", res.getVariableString());
        MyMessage res2 = rb.poll();
        assertEquals("def", res2.getVariableString());

        m.setVariableString("ghi");
        assertTrue(rb.offer(m));
        m.setVariableString("jkl");
        assertTrue(rb.offer(m));
        m.setVariableString("mno");
        assertFalse(rb.offer(m));
    }
}
