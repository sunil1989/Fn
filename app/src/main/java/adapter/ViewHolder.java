package adapter;

import com.life_reminder.R;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;



public class ViewHolder {

    public TextView text,text2;
    public CheckBox checkbox;
    public ImageView imageview;
    public ViewHolder(View v) {
        this.text = (TextView)v.findViewById(R.id.text1);
        this.text2 = (TextView)v.findViewById(R.id.text2);
        this.checkbox = (CheckBox)v.findViewById(R.id.cbx1);
        this.imageview = (ImageView)v.findViewById(R.id.image1);
    }

}
