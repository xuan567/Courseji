package com.littlecorgi.leave;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;

/**
 * 学生请假页面
 */
public class StudentLeaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.student_leave, new StudentLeaveFragment());
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getSupportFragmentManager().getFragments();
        if (getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment f : fragments) {
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
