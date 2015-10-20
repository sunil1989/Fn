package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.life_reminder.R;

import java.util.List;

import data.Task;

public class All_Adapter extends BaseAdapter{
	private Activity activity;
	private LayoutInflater layoutInflater;
	private List<Task> all_data_list;
	ImageView impImageView,iconImageView;
	int pos;
	public All_Adapter(Activity activity, List<Task> all_data_list) {
		super();
		this.activity = activity;
		this.all_data_list = all_data_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return all_data_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(layoutInflater==null)
		{
			layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if(convertview==null)
		{
			convertview = layoutInflater.inflate(R.layout.child_item, null);
		       TextView title = (TextView) convertview.findViewById(R.id.text1);
		       impImageView = (ImageView) convertview.findViewById(R.id.img);
		       iconImageView= (ImageView) convertview.findViewById(R.id.icon_img);
		        // get data for row
		        Task task=all_data_list.get(position);
		        // set data for title
		        title.setText(task.getTitle());
		        String imp= all_data_list.get(position).getImportance();
		    pos=position;
		        System.out.println("============="+imp);
		        set_image();
		       if(imp!=null){
		        switch (imp) {
				case "High":
					impImageView.setImageResource(R.drawable.importance_three);
					impImageView.setVisibility(View.VISIBLE);
					break;
			case "Medium":
				impImageView.setImageResource(R.drawable.importance_two);
				impImageView.setVisibility(View.VISIBLE);
					break;
					
			case "Low":
				impImageView.setImageResource(R.drawable.importance_one);
				impImageView.setVisibility(View.VISIBLE);
				break;
				default:
			    
					break;
				}
		        }else
		        {
		        	impImageView.setVisibility(View.INVISIBLE);
		        }
		 
		
	
		}
		return convertview;
	}
	
public void set_image()
{
	 String event=all_data_list.get(pos).getEvent();
	 String mail=all_data_list.get(pos).getTo_mail();
	 String name=all_data_list.get(pos).getName();
	String notes=all_data_list.get(pos).getNotes();
	String etc=all_data_list.get(pos).getEtc();
	String life=all_data_list.get(pos).getLife();
	
    if(event != null && !event.isEmpty() && !event.equals("null")){
		 if(event.equalsIgnoreCase("event")){
			 iconImageView.setImageResource(R.drawable.event_title_icon);
			 iconImageView.setVisibility(View.VISIBLE);
			
		 }
		 }


        if(mail != null && !mail.isEmpty() && !mail.equals("null")){
			 iconImageView.setImageResource(R.drawable.email_title_icon);
			 iconImageView.setVisibility(View.VISIBLE);
		 }

       if(name != null && !name.isEmpty() && !name.equals("null")){
       if(name.equals("Name")){
       	iconImageView.setImageResource(R.drawable.faces_page_title_icon);
      	 iconImageView.setVisibility(View.VISIBLE); 
       	
		       }
       
		 }
        if(notes != null && !notes.isEmpty() && !notes.equals("null")){
       if(notes.equals("NOTES")){
       	iconImageView.setImageResource(R.drawable.phone_title_icon);
      	 iconImageView.setVisibility(View.VISIBLE);
			
		 }
		 }
        if(etc != null && !etc.isEmpty() && !etc.equals("null")){
        if(etc.equals("etc")){
       	 iconImageView.setImageResource(R.drawable.new_etc_icon_active);
       	 iconImageView.setVisibility(View.VISIBLE);
	
		 }
        }
        
        if(life != null && !life.isEmpty() && !life.equals("null")){
            if(life.equals("life")){
           	 iconImageView.setImageResource(R.drawable.new_etc_icon_active);
           	 iconImageView.setVisibility(View.VISIBLE);
			
		 }
		    }
}


}
