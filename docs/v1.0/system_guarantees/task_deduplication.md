# Task Deduplication
When modifying the tasks of jobs through the task REST API, some tasks are deduplicated. This only happens to active tasks, when a task is removed
it is soft deleted. For example if you add the same task twice, on the second call you'd get the id of the first call. 

It is impossible for a job to have the same task twice in its active task set. 

## Core rules
Below is a list of the core rules of task deduplication
<li>Deduplication is only applied to ACTIVE tasks</li>
<li>Inactive tasks are excluded</li>
<li>Inactivation is soft-deletion only</li>
<li>Referential integrity is preserved through status, not deletion</li>