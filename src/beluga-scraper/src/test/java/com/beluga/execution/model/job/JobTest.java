package com.beluga.execution.model.job;

import com.beluga.execution.model.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JobTest {

    @Test
    void shouldHaveMutableTaskCollection() {
        final List<Task> tasks = List.of();
        final Job job = new Job();
        job.setTasks(tasks);

        assertDoesNotThrow(() -> job.getTasks().clear());
    }

    @Test
    void shouldSetTasks() {
        final Task task = mock();
        final Job job = new Job();

        job.setTasks(List.of(task));

        assertEquals(1, job.getTasks().size());
        assertTrue(job.getTasks().contains(task));
    }
}