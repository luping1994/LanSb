package ren.solid.skinloader.attr;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;

import com.roughike.bottombar.BottomBar;

import ren.solid.skinloader.load.SkinManager;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:17:45
 */
public class BottombarViewAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof BottomBar) {
//            Log.i("TabLayoutAttr", "apply");
            BottomBar bottomBar = (BottomBar) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
//                Log.e("Bottombar", "apply color");
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                bottomBar.setActiveTabColor(color);
            } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
//                Log.e("Bottombar", "apply drawable");
                //  tv.setDivider(SkinManager.getInstance().getDrawable(attrValueRefId));
            }
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
