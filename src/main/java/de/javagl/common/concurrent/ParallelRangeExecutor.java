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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Utility class that can perform an execution on a certain range in
 * parallel. The execution is delegated to implementations of the
 * {@link RangeExecutor} interface.
 */
public class ParallelRangeExecutor
{
    /**
     * An interface that describes an execution that can be applied to
     * a range of elements
     */
    public static interface RangeExecutor
    {
        /**
         * Perform the execution on the specified range
         * 
         * @param taskIndex The index of the task. This is the number of
         * the range that is specified by the given <code>min</code> and
         * <code>max</code> values. This index may be used to store results
         * that are produced by this <code>RangeExecutor</code>, for 
         * example, in a list. 
         * @param min The minimum index, inclusive
         * @param max The maximum index, exclusive
         */
        void execute(int taskIndex, int min, int max);
    }
    
    /**
     * Perform a parallel execution of the given {@link RangeExecutor}
     * with the specified range and parallelism level on the given 
     * executor service.<br>
     * <br>
     * The actual number of tasks that will be created is
     * <code>min(parallelism, globalMax-globalMin</code>.
     * 
     * @param parallelism The parallelism. This is the maximum number of 
     * tasks that will be created in order to process the specified range.
     * (If the range is smaller than this parallelism level, then fewer
     * tasks will be created)
     * @param executorService The executor service
     * @param globalMin The global minimum index of the range
     * @param globalMax The global maximum index of the range
     * @param rangeExecutor The {@link RangeExecutor} to which the
     * computation for the sub-ranges will be delegated.
     * @throws IllegalArgumentException If the parallelism is not positive
     * @throws IllegalArgumentException If the global minimum is larger than
     * the global maximum.
     */
    public static void execute(
        int parallelism, 
        ExecutorService executorService, 
        int globalMin, int globalMax,
        final RangeExecutor rangeExecutor)
    {
        if (parallelism <= 0)
        {
            throw new IllegalArgumentException(
                "Parallelism must be positive, but is " + parallelism);
        }
        if (globalMin > globalMax)
        {
            throw new IllegalArgumentException(
                "The global minimum may not be larger than the global " + 
                "maximum. Global minimum is "+globalMin+", " + 
                "global maximum is "+globalMax);
        }
        int range = globalMax - globalMin;
        if (range == 0)
        {
            return;
        }
        int numTasks = Math.min(range, parallelism);
        int localRange = (range - 1) / numTasks + 1;
        int spare = localRange * numTasks - range;
        int currentIndex = globalMin;
        List<Callable<Object>> tasks =
            new ArrayList<Callable<Object>>(numTasks);
        for (int i = 0; i < numTasks; i++)
        {
            final int taskIndex = i;
            final int min = currentIndex;
            final int max = min + localRange - (i < spare ? 1 : 0);
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    rangeExecutor.execute(taskIndex, min, max);
                }
            };
            tasks.add(Executors.callable(runnable));
            currentIndex = max;
        }
        try
        {
            executorService.invokeAll(tasks);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ParallelRangeExecutor()
    {
        // Private constructor to prevent instantiation
    }
}

