package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.newsapp.Models.NewApiResponse;
import com.example.newsapp.Models.NewsHeadlines;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener,View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;

    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView=findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                dialog.setTitle("Fetching news articals of "+ query);
                dialog.show();
                RequestManager manager=new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener,"general",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dialog=new ProgressDialog(this);
        dialog.setTitle("Fecthing news articals..");
        dialog.show();

        b1=findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2=findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3=findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4=findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6=findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7=findViewById(R.id.btn_7);
        b7.setOnClickListener(this);

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }else{
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        RequestManager manager=new RequestManager(this);
        manager.getNewsHeadlines(listener,"general",null);
    }

    private final OnFetchDataListener<NewApiResponse>listener=new OnFetchDataListener<NewApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {

            if (list.isEmpty()) {
                Toast.makeText(MainActivity.this, "No data found!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {

            Toast.makeText(MainActivity.this, "An Error Occured!!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView=findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter=new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {

        startActivity(new Intent(MainActivity.this,DetailsActivity.class)
                .putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        String category=button.getText().toString();

        dialog.setTitle("Fetching news articals of "+category);
        dialog.show();
        RequestManager manager=new RequestManager(this);
        manager.getNewsHeadlines(listener,category,null);
    }
}