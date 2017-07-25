package ren.solid.skinloader.attr;

import android.view.View;

import ren.solid.skinloader.entity.MyImageView;
import ren.solid.skinloader.load.SkinManager;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:17:45
 */
public class ImageViewAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof MyImageView) {
//            System.out.println("我被执行了");

//            Log.i("TabLayoutAttr", "apply");
            MyImageView imageView = (MyImageView) view;
//            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
//                Log.i("imageViewSBBBBBBBBB", "apply color");
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                imageView.setFilterColor(color);
//            } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
//                Log.e("imageView", "apply drawable");
//                //  tv.setDivider(SkinManager.getInstance().getDrawable(attrValueRefId));
//            }
        }
    }

//    private ColorStateList createSelector(int color) {
//        int statePressed = android.R.attr.state_checked;
//        int stateChecked = android.R.attr.state_checked;
//        int[][] state = {{statePressed}, {-statePressed}, {stateChecked}, {-stateChecked}};
//        int color1 = color;
//        int color2 = Color.parseColor("#6E6E6E");
//        int color3 = color;
//        int color4 = Color.parseColor("#6E6E6E");
//        int[] colors = {color1, color2, color3, color4};
//        ColorStateList colorStateList = new ColorStateList(state, colors);
//        return colorStateList;
//    }
}
