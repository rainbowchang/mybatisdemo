package com.example.utils.utils;

public interface IThreadWakeupSignal {

    /**
     * 触发苏醒；
     * 实际需要扩充完成下列示例代码
     *     synchronized(this){
     *         this.notify();
     *     }
     */
    void wakeup();
}
