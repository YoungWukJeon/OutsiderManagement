package com.test.kani.outsidermanagement.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.kani.outsidermanagement.Fragment.CallVisitFragment;
import com.test.kani.outsidermanagement.Fragment.MyInfoFragment;
import com.test.kani.outsidermanagement.Fragment.OutsiderManagementFragment;
import com.test.kani.outsidermanagement.R;
import com.test.kani.outsidermanagement.Fragment.ReportFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    public static HashMap<String, Object> myInfoMap = new HashMap<> ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyInfoFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
        {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.my_info_menu:
                    selectedFragment = new MyInfoFragment();
                    break;
                case R.id.report_menu:
                    selectedFragment = new ReportFragment();
                    break;
                case R.id.call_visit_menu:
                    selectedFragment = new CallVisitFragment();
                    if( !(boolean) MainActivity.myInfoMap.get("officer") )
                    {
                        Toast.makeText(getApplicationContext(), "용사는 이용할 수 없는 서비스입니다.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case R.id.outsider_management_menu:
                    selectedFragment = new OutsiderManagementFragment();
                    if( !(boolean) MainActivity.myInfoMap.get("officer") )
                    {
                        Toast.makeText(getApplicationContext(), "용사는 이용할 수 없는 서비스입니다.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
            }

            if( selectedFragment != null )
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };
}
