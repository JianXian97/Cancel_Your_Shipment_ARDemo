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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.org.ardemo.ViewPagerAdapter;
import com.org.ardemo.ViewProductActivity;
import com.org.ardemo.objs.Product;
import com.org.ardemo.R;
import com.org.ardemo.objs.Shop;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProductDetailsFragment extends Fragment {
    private int pos;
    Product product;
    ConstraintLayout overall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_details_fragment, container, false);
        //Product product = (Product) getArguments().get("data");
        Shop shop = product.getShop();
        ImageView shopPicture = view.findViewById(R.id.shopPicture);
        TextView shopName = view.findViewById(R.id.shopName);
        TextView shopLastSeen = view.findViewById(R.id.shopLastSeen);
        TextView shopProductCount = view.findViewById(R.id.shopProductCount);
        TextView shopRating = view.findViewById(R.id.shopRating);
        TextView shopChatResponse = view.findViewById(R.id.shopChatResponse);
        TextView productDescription = view.findViewById(R.id.description);
        TextView stockCount = view.findViewById(R.id.stockCount);
        TextView brand = view.findViewById(R.id.brand);
        TextView shipsFromAddress = view.findViewById(R.id.shipsFromAddress);
        overall = view.findViewById(R.id.overall);
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

        stockCount.setText(product.getStocks()+"");
        brand.setText(product.getBrand());
        shipsFromAddress.setText(shop.getaddress());
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ProductDetailsFragment(){}

    public static ProductDetailsFragment newInstance(int pos, Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",product);
        bundle.putInt("pageNum", pos);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().get("data");
            pos = getArguments().getInt("pageNum");
        }
    }


}
