package shugal.com.mattendance;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by abhishek on 7/11/15.
 */
public class CustomTabLayoutOnPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {
    private final WeakReference<TabLayout> mTabLayoutRef;
    private Context context;
    public CustomTabLayoutOnPageChangeListener(TabLayout tabLayout, Context context) {
        super(tabLayout);
        this.mTabLayoutRef = new WeakReference<>(tabLayout);
        this.context = context;
    }
    @Override
    public void onPageSelected(int position) {

        super.onPageSelected(position);
        final TabLayout tabLayout = mTabLayoutRef.get();
        if (tabLayout != null) {
            final TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (tab != null) {
                tab.select();
                Toast.makeText(context, tab.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
