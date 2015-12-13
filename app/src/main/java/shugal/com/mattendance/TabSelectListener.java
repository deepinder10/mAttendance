package shugal.com.mattendance;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by abhishek on 7/11/15.
 */
public class TabSelectListener extends TabLayout.ViewPagerOnTabSelectedListener {

    private boolean preventReselected = false;
    private ViewPager viewPager;
    public  TabSelectListener(ViewPager viewPager) {
        super(viewPager);
        this.viewPager = viewPager;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        preventReselected = true;
        super.onTabSelected(tab);
        preventReselected = false;
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (preventReselected) return;
        super.onTabReselected(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        super.onTabUnselected(tab);
    }
}
