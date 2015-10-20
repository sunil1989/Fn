package adapter;

import com.life_reminder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Priority_dialog_adapter extends BaseAdapter {
private Context priority_context;
private LayoutInflater inflater;
public final static int[] priority_images_arr= {R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one,R.drawable.importance_one};
public final static String[] priority_text_arr={"Sun","Mon","Tues","Wed","Thu","Fri","Sat","1","2","3","4","5","6","7"};


public Priority_dialog_adapter(Context context)
{
	this.priority_context=context;
	this.inflater=(LayoutInflater) priority_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return priority_images_arr.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MainListHolder mainListHolder;
		View v=convertView;
		if(convertView==null)
		{
			mainListHolder=new MainListHolder();
			v=inflater.inflate(R.layout.priority_dialog_list, null);
			mainListHolder.priorityImageView=(ImageView)v.findViewById(R.id.priority_imageView1);
			mainListHolder.priorityTextView=(TextView)v.findViewById(R.id.priority_textView1);
			v.setTag(mainListHolder);
		}else
		{
			mainListHolder=(MainListHolder)v.getTag();
		}
		
		mainListHolder.priorityImageView.setImageResource(priority_images_arr[position]);
		mainListHolder.priorityTextView.setText(""+priority_text_arr[position]);
		
		return v;
	}
 class MainListHolder 
{
	private TextView priorityTextView;
	private ImageView priorityImageView;
}

}
