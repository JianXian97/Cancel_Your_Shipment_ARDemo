package com.org.ardemo.fragments;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.org.ardemo.Product;
import com.org.ardemo.R;
import com.org.ardemo.shop;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProductDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_details_fragment, container, false);
        Product product = (Product) getArguments().get("data");
        shop shop = product.getShop();

        ImageView shopPicture = view.findViewById(R.id.shopPicture);
        TextView shopName = view.findViewById(R.id.shopName);
        TextView shopLastSeen = view.findViewById(R.id.shopLastSeen);
        TextView shopProductCount = view.findViewById(R.id.shopProductCount);
        TextView shopRating = view.findViewById(R.id.shopRating);
        TextView shopChatResponse = view.findViewById(R.id.shopChatResponse);
        TextView productDescription = view.findViewById(R.id.description);

        AssetManager assetManager = getActivity().getAssets();
        try{
            String path = "shop-images/"+shop.getPicture();
            InputStream is = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            shopPicture.setImageBitmap(bitmap);

            path = "loremipsum.txt";
            is = assetManager.open(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String description = new String(buffer, StandardCharsets.UTF_8);
            Log.d("TEST",description);
            productDescription.setText(description);
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        shopName.setText(shop.getName());
        shopLastSeen.setText("Active " + Integer.toString(shop.getLastSeen()) + " minutes ago");
        shopProductCount.setText(Integer.toString(shop.getProductCount()));
        shopChatResponse.setText(Integer.toString(shop.getChatResponse())+"%");
        shopRating.setText(Double.toString(shop.getRating()));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
