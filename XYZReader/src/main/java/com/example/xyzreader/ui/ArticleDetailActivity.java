package com.example.xyzreader.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;
import com.example.xyzreader.model.Article;

public class ArticleDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";

    private ActivityArticleDetailBinding mBinding;
    private boolean mExpanded = true;

    public static Intent generateLaunchingIntent(Article article, Context context) {
        Intent i = new Intent(context, ArticleDetailActivity.class);
        i.putExtra(ARG_ITEM_ID, article);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);
        if (Build.VERSION.SDK_INT >= 21) {
            postponeEnterTransition();
            mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    mExpanded = verticalOffset == 0;
                }
            });
        }
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding.setArticle((Article) getIntent().getParcelableExtra(ARG_ITEM_ID));
        setupImageAndColors();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mExpanded && Build.VERSION.SDK_INT >= 21) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void setupImageAndColors() {
        Glide.with(this).load(Uri.parse(mBinding.getArticle().imageUrl)).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(mBinding.photo) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (Build.VERSION.SDK_INT >= 21) {
                                    startPostponedEnterTransition();
                                }
                                int defaultColor = 0xFF333333;
                                mBinding.collapsingToolbar.setContentScrimColor(palette.getDarkMutedColor(defaultColor));
                                mBinding.shareFab.setBackgroundTintList(ColorStateList.valueOf(palette.getLightMutedColor(defaultColor)));
                                mBinding.shareFab.setRippleColor(palette.getDarkVibrantColor(defaultColor));
                                if (Build.VERSION.SDK_INT >= 21) {
                                    getWindow().setStatusBarColor(palette.getDarkVibrantColor(defaultColor));
                                }
                                mBinding.shareFab.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new OvershootInterpolator()).start();
                            }
                        });
                    }
                });

    }

    public void onShareClicked(View v) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(ArticleDetailActivity.this)
                .setType("text/plain")
                .setText("Some sample text")
                .getIntent(), getString(R.string.action_share)));
    }
}
