package com.codepath.android.lollipopexercise.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.codepath.android.lollipopexercise.R;
import com.codepath.android.lollipopexercise.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private Contact mContact;
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvPhone;
    private FloatingActionButton fab;
    private View vPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        fab = findViewById(R.id.fab);
        vPalette = findViewById(R.id.vPalette);

        // Extract contact from bundle
        mContact = (Contact)getIntent().getExtras().getSerializable(EXTRA_CONTACT);
//        mContact = (Contact) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CONTACT)); //ALT: with Parcel

        Log.d("DetailsActivity", "getName() result" + mContact.getName());
        // Fill views with data
        //qq: why is this unecessary?
//        Glide.with(DetailsActivity.this)
//                .load(mContact.getThumbnailDrawable())
//                .centerCrop()
//                .into(ivProfile);
        tvName.setText(mContact.getName());
        tvPhone.setText(mContact.getNumber());

        // Use Glide to get a callback with a Bitmap which can then
        // be used to extract a vibrant color from the Palette.

        // Define an asynchronous listener for image loading
        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                // 1. Instruct Glide to load the bitmap into the `holder.ivProfile` profile image view
                Glide.with(DetailsActivity.this)
                        .load(resource)
                        .into(ivProfile); // load bitmap into Glide
                // 2. Use generate() method from the Palette API to get the vibrant color from the bitmap
                Palette palette = Palette.from(resource).generate(); // get colour
                // Set the result as the background color for `holder.vPalette` view containing the contact's name.
                vPalette.setBackgroundColor(palette.getLightVibrantColor(0)); // set background colour
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                // can leave empty
            }
        };

        // Instruct Glide to load the bitmap into the asynchronous target defined above
        Glide.with(this).asBitmap().load(mContact.getThumbnailDrawable()).centerCrop().into(target);

        // Dial contact's number.
        // This shows the dialer with the number, allowing you to explicitly initiate the call.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + mContact.getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
