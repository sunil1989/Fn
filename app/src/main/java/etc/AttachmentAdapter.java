package etc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.life_reminder.R;

import java.util.List;

public class AttachmentAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<AttachData> all_data_list;
    ImageView iconImageView;
    TextView mFileName;
    int pos;
    int targetWidth=140;
    int targetHeight=140;
    String fileName;
    public AttachmentAdapter(Activity activity, List<AttachData> all_data_list) {
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

    public int getViewTypeCount() {

        if (getCount() != 0)
            return getCount();

        return 1;
    }
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub

        View view = convertview;
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater
                    .inflate(R.layout.attach_row, null);
        }
        iconImageView = (ImageView) view.findViewById(R.id.icon_img);
        mFileName = (TextView) view.findViewById(R.id.filename);

        pos = position;

        String filepath = all_data_list.get(pos).getFilepath();

        fileName = all_data_list.get(pos).getFile();

        //mFileName.setText("dssdffsd");

        //iconImageView.setImageResource(R.drawable.attachment);
        System.out.println("========fileName==========" + fileName);

        System.out.println("========filepath=========="+filepath);
        convert_graphic_hexagonal();

        return view;
    }

    private void convert_graphic_hexagonal() {
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.attach);
        // get hexagonal shape


        Bitmap b = getHexagonShape(bitmap);
        // set image in imageview
        iconImageView.setImageBitmap(b);
        mFileName.setText(fileName);
    }

    public Bitmap getHexagonShape(Bitmap scaleBitmapImage) {

        // TODO Auto-generated method stub
		/*
		 * int targetWidth = 140; int targetHeight = 140;
		 */
        get_screen_size();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);

        Path path = new Path();
        float stdW = targetWidth;
        float stdH = targetHeight;
        float w3 = stdW / 2;
        float h2 = stdH / 2;

        float radius = stdH / 2 - 10;
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = stdW / 2;
        float centerY = stdH / 2;
        path.moveTo(centerX, centerY + radius);
        path.lineTo(centerX - triangleHeight, centerY + radius / 2);
        path.lineTo(centerX - triangleHeight, centerY - radius / 2);
        path.lineTo(centerX, centerY - radius);
        path.lineTo(centerX + triangleHeight, centerY - radius / 2);
        path.lineTo(centerX + triangleHeight, centerY + radius / 2);
        path.moveTo(centerX, centerY + radius);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
                targetHeight), null);
        return targetBitmap;
    }


    public void get_screen_size() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if (height == 800 && width == 480) {
            targetWidth = 110;
            targetHeight = 110;
        }
        if (height == 1280 && width == 720) {
            targetWidth = 140;
            targetHeight = 140;
        }

    }

}
