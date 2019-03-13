package com.codingbydumbbell.animationdrawable;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private AnimationDrawable animation;
    private ProgressBar bar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setDefaultImg();
    }

    private void setDefaultImg() {
        ImageView view = new ImageView(MainActivity.this);
        view.setImageResource(R.drawable.anim_default);
        animation = (AnimationDrawable) view.getDrawable();
    }

    private void findViews() {
        imgView = findViewById(R.id.imageView);
        button = findViewById(R.id.btn_play);
        bar = findViewById(R.id.progressBar);
    }


    public void downloadImage(View view) {
        new DownloadImgTask().execute();
        bar.setVisibility(View.VISIBLE);
    }

    public void playFrame(View view) {

        imgView.setImageDrawable(animation);

        if (!animation.isRunning()) {
            animation.start();
            button.setText("Stop");
        } else {
            animation.stop();
            button.setText("Start");
        }
    }

    class DownloadImgTask extends AsyncTask<Void, Void, List<Drawable>> {

        // form Unsplash
        String[] urls = {"https://images.unsplash.com/photo-1548690312-e3b507d8c110?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
                "https://images.unsplash.com/photo-1532384816664-01b8b7238c8d?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80",
                "https://images.unsplash.com/photo-1548946822-edfc20f875da?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
                "https://images.unsplash.com/photo-1549060279-94ce292c785d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
                "https://images.unsplash.com/photo-1509009082772-cb9797f8adbf?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=720&q=80"};

        @Override
        protected List<Drawable> doInBackground(Void... voids) {

            List<Drawable> drawables = new ArrayList<>();

            for (String url : urls) {
                try {
                    // by Picasso
                    Bitmap bitmap = Picasso.get().load(url).get();

                    if (bitmap != null)
                        drawables.add(new BitmapDrawable(getResources(), bitmap));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return drawables;
        }

        @Override
        protected void onPostExecute(List<Drawable> drawables) {
            super.onPostExecute(drawables);

            bar.setVisibility(View.GONE);
            if (drawables.size() > 0) {
                animation = new AnimationDrawable();
                for (Drawable drawable : drawables) {
                    animation.addFrame(drawable, 3 * 1000);
                }
                // 設定輪播方式
                animation.setOneShot(false);
            }
        }
    }
}
