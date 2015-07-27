package com.roalts.hackdelhiclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {

  protected Toolbar toolbar;

  protected abstract int getLayoutResId();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResId());

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
    toolbar.setTitle(getTitle());
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }
}
