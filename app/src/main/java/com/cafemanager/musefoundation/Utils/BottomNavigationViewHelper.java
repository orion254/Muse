package com.cafemanager.musefoundation.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.cafemanager.musefoundation.Home.HomeActivity;
import com.cafemanager.musefoundation.Likes.LikesActivity;
import com.cafemanager.musefoundation.Profile.ProfileActivity;
import com.cafemanager.musefoundation.R;
import com.cafemanager.musefoundation.Search.SearchActivity;
import com.cafemanager.musefoundation.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 *  Added "callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);"
 *  right before every break;   (+)
 *
 *  Added "final Activity callingActivity" to enableNavigation's parameters.  (+)
 *
 *  Activities that need to be modified because they call the old version of enableNavigation():
 *      1) HomeActivity (under Home)
 *      2) Likes Activity (under Likes)
 *      3) Search Activity (under Search)
 *
 *
 *  Note: Don't forget to copy/paste "anim" folder to Emily-MergedKevin's "res" folder
 */

public class BottomNavigationViewHelper {

    private static  final String TAG = "BottomNavigationViewHel";

    public  static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
//        bottomNavigationViewEx.enableAnimation(false);
//        bottomNavigationViewEx.enableItemShiftingMode(false);
//        bottomNavigationViewEx.enableShiftingMode(false);
//        bottomNavigationViewEx.setTextVisibility(false);


    }



    public  static  void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, HomeActivity.class);    // ACTIVITY_NUM = 0;
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class);  // ACTIVITY_NUM = 1;
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_circle:
                        Intent intent3 = new Intent(context, ShareActivity.class);  // ACTIVITY_NUM = 2;
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_alert:
                        Intent intent4 = new Intent(context, ProfileActivity.class);  // ACTIVITY_NUM = 3;
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_android8:
                        Intent intent5 = new Intent(context, LikesActivity.class);  // ACTIVITY_NUM = 4;
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                }
                return false;
            }
        });
    }

}
