/*
 * www.javagl.de - Common
 *
 * Copyright (c) 2012-2014 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.common.concurrent;

import java.util.concurrent.*;



/**
 * Utility methods to create executor service instances
 */
public class ExecutorServices
{
    /**
     * Creates an executor service with a fixed pool size, that will time 
     * out after a certain period of inactivity.<br>
     * <br>
     * The executor service will re-throw exceptions that happen in
     * any of the tasks that it executes.
     * 
     * @param poolSize The core- and maximum pool size
     * @param keepAliveTime The keep alive time
     * @param timeUnit The time unit
     * @param rethrowCancellation Whether CancellationExceptions should
     * also be re-thrown
     * @return The executor service
     */
    public static ExecutorService createFixedTimeoutRethrowingExecutorService(
        int poolSize, long keepAliveTime, TimeUnit timeUnit, 
        final boolean rethrowCancellation)
    {
        ThreadPoolExecutor e = 
            new ThreadPoolExecutor(poolSize, poolSize,
                keepAliveTime, timeUnit, new LinkedBlockingQueue<Runnable>())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                if (t == null && r instanceof Future<?>)
                {
                    try
                    {
                        Future<?> future = (Future<?>) r;
                        if (future.isDone())
                        {
                            future.get();
                        }
                    }
                    catch (CancellationException ce)
                    {
                        if (rethrowCancellation)
                        {
                            t = ce;
                        }
                    }
                    catch (ExecutionException ee)
                    {
                        t = ee.getCause();
                    }
                    catch (InterruptedException ie)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                if (t != null)
                {
                    throw new RuntimeException(t);
                }
            }
        };
        e.allowCoreThreadTimeOut(true);
        return e;
    }

    /**
     * Creates an executor service with a fixed pool size, that will time 
     * out after a certain period of inactivity.
     * 
     * @param poolSize The core- and maximum pool size
     * @param keepAliveTime The keep alive time
     * @param timeUnit The time unit
     * @return The executor service
     */
    public static ExecutorService createFixedTimeoutExecutorService(
        int poolSize, long keepAliveTime, TimeUnit timeUnit)
    {
        ThreadPoolExecutor e = 
            new ThreadPoolExecutor(poolSize, poolSize,
                keepAliveTime, timeUnit, new LinkedBlockingQueue<Runnable>());
        e.allowCoreThreadTimeOut(true);
        return e;
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private ExecutorServices()
    {
        // Private constructor to prevent instantiation
    }
}
