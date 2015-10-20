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


public class OldStuffAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Task> data;
   // ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ImageView iconImageView;
    public OldStuffAdapter(Activity activity, List<Task> data) {
        this.activity = activity;
        this.data = data;
    }
 
    @Override
    public int getCount() {
        return data.size();
    }
 
    @Override
    public Object getItem(int location) {
        return data.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.old_stuff_child_item, null);
 
    
       /* if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);*/
        TextView title = (TextView) convertView.findViewById(R.id.text1);
         iconImageView = (ImageView) convertView.findViewById(R.id.icon_img);
       // TextView rating = (TextView) convertView.findViewById(R.id.rating);
       // TextView genre = (TextView) convertView.findViewById(R.id.genre);
       // TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
 
        // getting movie data for the row
        Task m = data.get(position);
 
        // thumbnail image
       // thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
         
        // title
        title.setText(m.getTitle());
        
        
        setIcon(position);
         
      /*  // rating
        rating.setText("Rating: " + String.valueOf(m.getRating()));
         
        // genre
        String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);
         
        // release year
        year.setText(String.valueOf(m.getYear()));*/
 
        return convertView;
    }

	private void setIcon(int pos) {
		// TODO Auto-generated method stub
		String event=data.get(pos).getEvent();
		 String mail=data.get(pos).getTo_mail();
		 String name=data.get(pos).getName();
		String notes=data.get(pos).getNotes();
		String etc=data.get(pos).getEtc();
		String life=data.get(pos).getLife();
		
		
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
 
