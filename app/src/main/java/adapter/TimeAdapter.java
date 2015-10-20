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

import data.FetchData;


public class TimeAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FetchData> data;
    ImageView impImageView,iconImageView;
	int pos;
   // ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public TimeAdapter(Activity activity, List<FetchData> data) {
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
            convertView = inflater.inflate(R.layout.child_item, null);
 
       /* if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);*/
        TextView title = (TextView) convertView.findViewById(R.id.text1);
        impImageView = (ImageView) convertView.findViewById(R.id.img);
	    iconImageView= (ImageView) convertView.findViewById(R.id.icon_img);
	     //  impImageView.setVisibility(View.VISIBLE);
       // TextView rating = (TextView) convertView.findViewById(R.id.rating);
       // TextView genre = (TextView) convertView.findViewById(R.id.genre);
       // TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
 
        // getting movie data for the row
        FetchData m = data.get(position);
 
        // thumbnail image
       // thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
         
        // title
        title.setText(m.getEventName());
        
        String imp= data.get(position).getImportance();
        
        long id= data.get(position).getId();
        
        
   
        
      //  System.out.println("=======id============="+id);
        
       // pos=position;
      
       set_image(id,position);
       // System.out.println("============="+imp);
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
        	
    //    if(imp!=null&&imp.isEmpty()&&!imp.equals(null)){
        
        
      /*  if(imp!=null){
        	 System.out.println("============="+imp);
        	if(imp.equals("High")){
        		img.setImageResource(R.drawable.importance_three);
        	}
        
          if(imp.equals("Medium")){
        	  img.setImageResource(R.drawable.importance_two);
        	}
         
          if(imp.equals("Low")){
        	  img.setImageResource(R.drawable.importance_one);
      //	}
        }
         
          
        }*/
         
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
    
    
    
    
    public void set_image(long id,int position)
    {
    	 String event=data.get(position).getEvent();
    	 String mail=data.get(position).getTo_mail();
    	 String name=data.get(position).getName();
    	 String notes=data.get(position).getNotes();
    	 String etc=data.get(position).getEtc();
    	 String life=data.get(position).getLife();
    	 
    	 
    	 System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
    //	 System.out.println("========position=========="+position);
    	 System.out.println("========event=========="+event);
    	 System.out.println("========id=========="+id);
    	// System.out.println("========mail=========="+mail);
    	// System.out.println("========name=========="+name);
    	// System.out.println("========notes=========="+notes);
    	// System.out.println("========etc=========="+etc);
    	 //System.out.println("========life=========="+life);
    	// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	
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