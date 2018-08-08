package com.example.xyzreader.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xyzreader.R;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE); }
        setContentView(R.layout.activity_article_detail);

        ArticleDetailFragmentNew articleDetailFragment = ArticleDetailFragmentNew.newInstance(1);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.container_fragment,articleDetailFragment).commit();
    }

}
