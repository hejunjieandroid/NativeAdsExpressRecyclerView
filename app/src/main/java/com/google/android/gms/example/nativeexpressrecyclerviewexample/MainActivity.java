package com.google.android.gms.example.nativeexpressrecyclerviewexample;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple activity showing the use of {@link NativeExpressAdView} ads in
 * a {@link RecyclerView} widget.
 * <p>The {@link RecyclerView} widget is a more advanced and flexible version of
 * ListView. This widget helps simplify the display and handling of large data sets
 * by allowing the layout manager to determine when to reuse (recycle) item views that
 * are no longer visible to the user. Recycling views improves performance by avoiding
 * the creation of unnecessary views or performing expensive findViewByID() lookups.</p>
 */
public class MainActivity extends AppCompatActivity implements RecItemClickListener {

    // A Native Express ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 8;

    // The Native Express ad height.
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;

    // The Native Express ad unit ID.
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";

    // The RecyclerView that holds and displays Native Express ads and menu items.
    private RecyclerView mRecyclerView;

    // List of Native Express ads and MenuItems that populate the RecyclerView.
    private List<Object> mRecyclerViewItems;

    private boolean isdrag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//git
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView.
        mRecyclerView.setHasFixedSize(true);

        // Specify a linear layout manager.
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,6);
        mRecyclerView.setLayoutManager(layoutManager);

        DefaultItemAnimator ani=new DefaultItemAnimator();
        ani.setSupportsChangeAnimations(true);
        ani.setAddDuration(300);
        ani.setChangeDuration(300);
        ani.setMoveDuration(300);
        ani.setRemoveDuration(300);

        SimpleItemAnimator sani=new SimpleItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {

                return false;
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {

                return false;
            }

            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {

                return false;
            }

            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {

                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };

        mRecyclerView.setItemAnimator(ani);

        ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition=target.getAdapterPosition();

//                if(fromPosition < toPosition){
//                    for(int i=fromPosition;i<toPosition;i++){
//                        Collections.swap(mRecyclerViewItems,i,i+1);
//                    }
//                    recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
//                    recyclerView.getAdapter().notifyItemRangeChanged(fromPosition,toPosition-fromPosition+1);
//                }else{
//                    for(int i=fromPosition;i>toPosition;i--){
//                        Collections.swap(mRecyclerViewItems,i,i-1);
//                    }
//                    recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
//                    recyclerView.getAdapter().notifyItemRangeChanged(toPosition,fromPosition-toPosition+1);
//                }

                Collections.swap(mRecyclerViewItems,fromPosition,toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
//                if(fromPosition < toPosition){
//                    recyclerView.getAdapter().notifyItemRangeChanged(fromPosition,toPosition-fromPosition+1);
//                }else{
//                    recyclerView.getAdapter().notifyItemRangeChanged(toPosition,fromPosition-toPosition+1);
//                }

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mRecyclerViewItems.remove(viewHolder.getAdapterPosition());
                mRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                mRecyclerView.getAdapter().notifyItemRangeChanged(viewHolder.getAdapterPosition(),mRecyclerViewItems.size()-viewHolder.getAdapterPosition());
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.i("MainActivity","onselectedchanged====="+actionState);
                if(actionState==2){
                    isdrag=true;
                }
                if(actionState==0&&isdrag){
                    isdrag=false;
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        };


        ItemTouchHelper.Callback callback1=new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

        };



ItemTouchHelper itm=new ItemTouchHelper(callback);
        itm.attachToRecyclerView(mRecyclerView);
        // Create a list containing menu items and Native Express ads.
        mRecyclerViewItems = new ArrayList<>();
        addMenuItems();
//        addNativeExpressAds();
//        setUpAndLoadNativeExpressAds();

        // Specify an adapter.
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mRecyclerViewItems);
        adapter.setRecItemClickListener(this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    /**
     * Adds {@link MenuItem}'s to the items list.
     */
    private void addMenuItems() {

        // Add the menu items.
        mRecyclerViewItems.add(new MenuItem("Cherry Avocado", "cherries, garlic, serrano, mayo",
                "$7.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Fried Baby Onions", "maple syrup, walnut salsa, sauce",
                "$11.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Fried Rice", "red onion, kale, puffed wild rice",
                "$10.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Beet Fries", "cilantro, raw beet, feta, sumac",
                "$9.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Sautéed Spaghetti", "garlic, poached egg, almonds",
                "$12.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Grape Toast", "red cabbage, sweet onion, beef",
                "$14.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Fresh Acorn Squash", "pumplin mole, pomegranate, seed",
                "$11.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Quad Burgers", "biscuits, bacon, honey butter",
                "$7.00", "Dinner", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("The Mister Burger", "pepperoni, cheese, lettuce fries",
                "$16.00", "Dinner", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Deep Fried String Cheese", "dipped in a honey mustard"
                + " aioli", "$7.00", "Dinner", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Cheese Plate", "a bunch of different types of cheeses",
                "$16.00", "Dinner", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Pear and Jicama", "cheese, chardonnay vinaigrette",
                "$12.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("The Caesar", "garlic dressing, egg, olive oil, walnut"
                + " breadcrumbs", "$10.00", "Dinner - Veggies, Grains, Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Cold Brussels Sprouts", "shaved, raisins", "$10.00",
                "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Deep Fried Brussels Sprouts", "smoked yogurt and tea",
                "$12.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Bread & Whipped Cream", "bread with whipped cream",
                "$3.00", "Dinner - Salads", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Lamb Steaks", "fregola, cucumber, lemon, rosemary",
                "$15.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Grilled Pork Chop", "scallion-cilantro salad, chili",
                "$14.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Lobster Dumplings", "chili oil, garlic, celery",
                "$16.00", "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Grilled Scallops", "hummus, crispy fries", "$16.00",
                "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("White Tuna", "crispy garlic, green olives, oil",
                "$15.00", "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Oysters", "cilantro, kale, lemons", "$3.50",
                "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Crab legs", "quinoa, kumquat, black garlic", "$15.00",
                "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Lobster Rolls and Tuna", "ahi tuna, cucumber, tomato",
                "$14.00", "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("King Salmon", "maple glaze, tomato, cucumber, lemon",
                "$30.00", "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Tempura Tacos", "olive aioli, radish salad", "$16.00",
                "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Grilled Pork Belly", "sweet soya, charred scallion",
                "$15.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Singaporean Fried Chicken", "green onion, sweet heat",
                "$7.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Live Quail", "crisp potatoes, cumin syrup, mushrooms",
                "$15.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Raw Beef", "raw beef, beech mushrooms, coddled egg",
                "$13.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Mac 'n Cheese", "fried mortadella, white cheddar",
                "$10.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Blackened Ribeye Steak", "beef, cheese, beans, lemon",
                "$56.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Braised Beef Tacos", "salsa salad, corn tortillas",
                "$15.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Smoked Duck", "blood orange, carrot, cocoa, raisins",
                "$13.00", "Dinner - Meat", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Shrimp & Grits", "shrimp, grits, fries, lemon",
                "$16.00", "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Mussels", "spiced butter, toasted bread herb salad",
                "$14.00", "Dinner - Seafood", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Beer Burger", "beer of the day on a burger", "$10.00",
                "Dinner", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Sundae", "maple ice cream, blueberry, graham cracker",
                "$7.00", "Dinner - Sweet", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Honey Ice Cream", "toasted nuts, dried fruits",
                "$7.00", "Dinner - Sweet", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Chocolate Cake", "orange almonds", "$7.00",
                "Dinner - Sweet", "menu_item_image"));
        mRecyclerViewItems.add(new MenuItem("Toffee Pudding", "dates, toffee, seeds", "$7.00",
                "Dinner - Sweet", "menu_item_image"));
    }

    /**
     * Adds Native Express ads to the items list.
     */
    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            mRecyclerViewItems.add(i, adView);
        }
    }

    /**
     * Sets up and loads the Native Express ads.
     */
    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float density = MainActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) mRecyclerViewItems.get(i);
                    AdSize adSize = new AdSize(
                            (int) (mRecyclerView.getWidth() / density),
                            NATIVE_EXPRESS_AD_HEIGHT);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(AD_UNIT_ID);
                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(0);
            }
        });
    }

    /**
     * Loads the Native Express ads in the items list.
     */
    private void loadNativeExpressAd(final int index) {

        if (index >= mRecyclerViewItems.size()) {
            return;
        }

        Object item = mRecyclerViewItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onclick(View v, int position) {
//        ViewCompat.animate(v).rotationXBy(180).withLayer().setInterpolator(new FastOutSlowInInterpolator()).setDuration(2000).start();
//        ViewCompat.animate(v).rotationX(180).withLayer().setInterpolator(new FastOutSlowInInterpolator()).setDuration(2000).start();
//        ViewCompat.animate(v).rotation(180).withLayer().setInterpolator(new FastOutSlowInInterpolator()).setDuration(2000).start();
//        ViewCompat.animate(v).scaleXBy(1).withLayer().setInterpolator(new FastOutSlowInInterpolator()).setDuration(2000).start();
        ViewCompat.animate(v).translationXBy(10).withLayer().setInterpolator(new FastOutSlowInInterpolator()).setDuration(2000).start();
        Toast.makeText(MainActivity.this,""+position+((MenuItem)mRecyclerViewItems.get(position)).getName(),Toast.LENGTH_SHORT).show();

        if(((GridLayoutManager) mRecyclerView.getLayoutManager()).getSpanCount()==6){
            ((GridLayoutManager)mRecyclerView.getLayoutManager()).setSpanCount(3);
        }else{
            ((GridLayoutManager)mRecyclerView.getLayoutManager()).setSpanCount(6);
        }
    }
}
