package IntervalTimer;

/**
 * for controlling the timing for chocking and un-chocking
 */
public class IntervalTimer extends Thread{

    private int delay;

    public IntervalTimer(int delay){
        this.delay = delay;
    }

    public void run(){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
