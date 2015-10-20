package data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskListArray
{
	private Context context;
	private static TaskListArray array;
	private ArrayList<Task> tasksArray;
	private TaskListDataBase dataBase;
	
	private TaskListArray(Context context)
	{
		this.context = context;
		dataBase = TaskListDataBase.getInstance(this.context);
		tasksArray = new ArrayList<Task>();
	}
	
	public static TaskListArray getInstance(Context context)
	{
		if (array == null)
			array = new TaskListArray(context);
		return array;
	}
	
	public void addTask(Task newTask)
	{
		tasksArray.add(0,newTask); // Adding new element to the first place in the array
	}
	
	public void removeTask(int position)
	{
		Task task2Del = tasksArray.get(position);
		
		dataBase.deleteTask(task2Del);
		tasksArray.remove(position);
	}
	
	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasksArray = tasks;
	}
	
	public int getSize()
	{
		return tasksArray.size();
	}
	
	public Task getTask(int position)
	{
		return tasksArray.get(position);
	}
	
	public boolean updateTask(Task task, long id)
	{
		for (int i=0 ; i<tasksArray.size(); ++i)
		{
			if (tasksArray.get(i).getId() == id)
			{
				tasksArray.set(i, task);
				dataBase.updateTask(task);
				return true;
			}
		}
		return false;
	}
	
	
	public void sortFirstCreatedTaskFirst()
	{	
		Collections.sort(tasksArray, new Comparator<Task>()
		{
			@Override
            public int compare(Task task1, Task task2)
            {
                return task1.compareTo(task2);
            }
        });
	}
	
	public void sortByTitle()
	{
		Collections.sort(tasksArray, new Comparator<Task>()
		{
			@Override
            public int compare(Task task1, Task task2)
            {
                return task1.getTitle().compareTo(task2.getTitle());
            }
        });
	}
}


