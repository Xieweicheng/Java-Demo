package cn.mrxiexie.concurrent.basic.case3;

/**
 * 线程池
 *
 * @author mrxiexie
 * @date 19-10-19 上午9:33
 */
public interface ThreadPool<Job extends Runnable> {

    /**
     * 执行一个 job，这个 job 需要实现 Runnable
     *
     * @param job 任务
     */
    void execute(Job job);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 添加工作者线程
     *
     * @param num 数量
     */
    void addWorkers(int num);

    /**
     * 移除工作者线程数量
     *
     * @param num 数量
     */
    void removeWorkers(int num);

    /**
     * 得到正在等待执行的任务数量
     *
     * @return 任务数量
     */
    int getJobSize();
}
