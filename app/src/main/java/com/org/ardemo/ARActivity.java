package com.org.ardemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.ToggleButton;
import com.org.ardemo.objs.Product;
import com.org.ardemo.objs.Shop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.org.ardemo.DemoUtils.convertDpToPixel;
import static com.org.ardemo.DemoUtils.toTitleCase;
import static com.org.ardemo.SearchActivity.deviceWidth;


public class ARActivity extends AppCompatActivity {
    private static final String TAG = ARActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private boolean installRequested;
    ImageButton removeButton, addObjectButton, takeImageButton, addToCartMenu, backButton;
    ArFragment arFragment;
    LinearLayout addToCartMenuLayout, addToCartMenuLayoutSlider, objChooser, variationPanel;
    SingleSelectToggleGroup variationList;
    TextView productTitle;
    ModelRenderable selectedRenderable;
    ViewRenderable selectionVisualiser;
    private boolean capturePicture = false;
    Node selectedAnchorNode;
    boolean addObject, deleteObject;
    private ArSceneView arSceneView;
    private boolean autoadded;
    ArrayList<ModelRenderable> listOfRenderable;
    int noOfNodes = 0;
    Map<String, String> colorMap = new HashMap<String, String>();
    int[] idList;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        fillColorMap();
        setContentView(R.layout.ar_activity_layout);
        removeButton = findViewById(R.id.removeButton);
        takeImageButton = findViewById(R.id.takeImage);
        backButton = findViewById(R.id.backButton);
        addToCartMenu = findViewById(R.id.addToCartMenu);
        productTitle = findViewById(R.id.productTitle);
        variationList = findViewById(R.id.variationList);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        addObject = false;
        product = (Product) getIntent().getParcelableExtra("product");
        listOfRenderable = new ArrayList<>();
        builder(product.getTitle()+".sfb");
        for(Product variation : product.getsuggestedProducts()){
            builder(variation.getTitle()+".sfb");
        }
        autoadded = false;

        arFragment.setOnTapArPlaneListener((HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
            if (selectedRenderable == null || noOfNodes > 0){
                return;
            }
            Anchor anchor = hitresult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.getScaleController().setMaxScale(0.5001f);
            node.getScaleController().setMinScale(0.5f);
            node.setParent(anchorNode);
            node.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
            node.setRenderable(selectedRenderable);
            node.select();
            selectedAnchorNode = node;
            noOfNodes++;
            }
        );

        Scene scene = arFragment.getArSceneView().getScene();

        CustomSelectionVisualiser footprintSelectionVisualizer = new CustomSelectionVisualiser();
        TransformationSystem transformationSystem = arFragment.getTransformationSystem();
        transformationSystem.setSelectionVisualizer(footprintSelectionVisualizer);
        ViewRenderable.builder()
                .setView(this, R.layout.selector_visualiser_layout)
                .build()
                .thenAccept(renderable -> selectionVisualiser = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load any renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

//        Vector3 vector3 = new Vector3(0.01f,0.5f,0.5f);
//        MaterialFactory.makeTransparentWithColor(this,new Color(220,289,59,255))
//                .thenAccept(
//                        material -> {
//                            selectionVisualiser = ShapeFactory.makeCylinder(0.5f,0.01f,Vector3.zero(),material);
//
//                        });

//        ModelRenderable.builder()
//                .setSource(this, Uri.parse("Ring.sfb"))
//                .build()
//                .thenAccept(renderable -> selectionVisualiser = renderable)
//                .exceptionally(throwable -> {
//                    Toast toast =
//                            Toast.makeText(this, "Unable to load any renderable", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    return null;
//                });


        try{
            Session session = new Session(this);
            com.google.ar.core.Config config = new com.google.ar.core.Config(session);
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
            config.setFocusMode(com.google.ar.core.Config.FocusMode.AUTO);
            session.configure(config);
            arFragment.getArSceneView().setupSession(session);
        }catch (UnavailableException e) {
            DemoUtils.handleSessionException(this, e);
        }

        scene.addOnPeekTouchListener(new Scene.OnPeekTouchListener() {
            @Override
            public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                // First call ArFragment's listener to handle TransformableNodes.
                arFragment.onPeekTouch(hitTestResult, motionEvent);

                //We are only interested in the ACTION_UP events - anything else just return
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return;
                }

                // Check for touching a Sceneform node
                if (hitTestResult.getNode() != null) {
                    Log.d(TAG, "handleOnTouch hitTestResult.getNode() != null");
                    Node hitNode = hitTestResult.getNode();
                    if (listOfRenderable.contains(hitNode.getRenderable())) {
                        Toast.makeText(ARActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                        selectedAnchorNode = hitNode;
                    }
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAnchorNode(selectedAnchorNode);
                noOfNodes--;
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        takeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    takePhoto();
                }catch(Exception e){
                    e.printStackTrace();
                    Log.d(TAG, "Cant save image");
                }
            }
        });

        addToCartMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(addToCartMenuLayout.getVisibility() == View.INVISIBLE) addToCartMenuLayout.setVisibility(View.VISIBLE);
                 else addToCartMenuLayout.setVisibility(View.INVISIBLE);
            }
        });
        //arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

        RecyclerView arModelSelector = findViewById(R.id.arModelSelector);
        CustomLayoutManager pickerLayoutManager = new CustomLayoutManager(this, CustomLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setScaleDownBy(0.99f);
        pickerLayoutManager.setScaleDownDistance(0.5f);


        PickerAdapter adapter = new PickerAdapter(this, prepareData(product), arModelSelector);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(arModelSelector);
        arModelSelector.setLayoutManager(pickerLayoutManager);
        arModelSelector.setAdapter(adapter);
        arModelSelector.setHasFixedSize(true);

        arModelSelector.smoothScrollToPosition(0);
        pickerLayoutManager.setOnScrollStopListener(new CustomLayoutManager.onScrollStopListener() {
            @Override
            public void selectedView(View view) {
                int pos = pickerLayoutManager.findFirstVisibleItemPosition();
                if(pos == 0) {
                    selectedRenderable = null;
                    productTitle.setVisibility(View.INVISIBLE);
                    variationList.removeAllViews();
                }
                if(pos > 0){
                    selectedRenderable = listOfRenderable.get(pos-1);
                    productTitle.setVisibility(View.VISIBLE);

                    if(pos == 1) {
                        productTitle.setText(product.getTitle());
                        idList = generateVariations(product);
                        Log.e("Generated Variation", "Generated!");

                    }
                    else{
                        String name = product.getsuggestedProducts()[pos-2].getTitle().replace("_"," ");
                        productTitle.setText(toTitleCase(name));
                        idList = generateVariations(product.getsuggestedProducts()[pos-2]);
                    }
                 }
                if (footprintSelectionVisualizer  != null) {
//                    selectionVisualiser.getMaterial().setFloat3("baseColorTint",
//                            new Color(android.graphics.Color.parseColor("#DC593B")));
                    if(selectionVisualiser!=null) {
                        selectionVisualiser.setHorizontalAlignment(ViewRenderable.HorizontalAlignment.CENTER);
                        selectionVisualiser.setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER);

                        footprintSelectionVisualizer.setFootprintRenderable(selectionVisualiser);
                    }
                }
            }
        });
        variationList.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                String color = getVariation();
                if(selectedAnchorNode != null) {//placed object, then select variation
                    if(color != null){
                        ModelRenderable newColorSelectedRenderable = selectedRenderable.makeCopy();

                        newColorSelectedRenderable.getMaterial().setFloat3("baseColorTint",
                                new Color(android.graphics.Color.parseColor(color)));
                        selectedAnchorNode.setLocalScale(new Vector3(0.001f, 0.001f, 0.001f));
                        selectedAnchorNode.setRenderable(newColorSelectedRenderable);
                    }
                    else{
                        selectedAnchorNode.setRenderable(selectedRenderable);
                    }
                }
                else{//select variation, haven't place object
                    if(color != null) {
                        selectedRenderable.getMaterial().setFloat3("baseColorTint",
                                new Color(android.graphics.Color.parseColor(color)));
                        Log.e("Changed Color","Changed Color");
                    }
                }

            }
        });


    }


    private void onUpdateFrame(FrameTime frameTime) {
        if(!autoadded){
            Frame frame = arFragment.getArSceneView().getArFrame();
            if (frame != null) {
                //get the trackables to ensure planes are detected
                Iterator var3 = frame.getUpdatedTrackables(Plane.class).iterator();
                while(var3.hasNext()) {
                    Plane plane = (Plane)var3.next();

                    //If a plane has been detected & is being tracked by ARCore
                    if (plane.getTrackingState() == TrackingState.TRACKING) {

                        //Hide the plane discovery helper animation
                        arFragment.getPlaneDiscoveryController().hide();


                        //Get all added anchors to the frame
                        Iterator iterableAnchor = frame.getUpdatedAnchors().iterator();

                        //place the first object only if no previous anchors were added
                        if(!iterableAnchor.hasNext()) {
                            //Perform a hit test at the center of the screen to place an object without tapping
                            List<HitResult> hitTest = frame.hitTest(getScreenCenter().x, getScreenCenter().y);

                            //iterate through all hits
                            Iterator hitTestIterator = hitTest.iterator();
                            while(hitTestIterator.hasNext()) {
                                HitResult hitResult = (HitResult) hitTestIterator.next();

                                //Create an anchor at the plane hit
                                Anchor modelAnchor = plane.createAnchor(hitResult.getHitPose());

                                //Attach a node to this anchor with the scene as the parent
                                AnchorNode anchorNode = new AnchorNode(modelAnchor);
                                anchorNode.setParent(arFragment.getArSceneView().getScene());

                                //create a new TranformableNode that will carry our object
                                TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                                transformableNode.setParent(anchorNode);
                                transformableNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
                                transformableNode.setRenderable(ARActivity.this.selectedRenderable);

                                //Alter the real world position to ensure object renders on the table top. Not somewhere inside.
                                transformableNode.setWorldPosition(new Vector3(modelAnchor.getPose().tx(),
                                        modelAnchor.getPose().compose(Pose.makeTranslation(0f, 0.05f, 0f)).ty(),
                                        modelAnchor.getPose().tz()));

                                autoadded = true;
                            }
                        }
                    }
                }
            }
        }

    }

    private int[] generateVariations(Product product){
        int eightDp = (int)convertDpToPixel(8,this);
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(deviceWidth/5,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layout.setMargins(eightDp,eightDp,eightDp,eightDp);
        variationList.removeAllViews();
        int n = product.getVariations().length;
        int[] idList = new int[n];
        CustomToggle[] toggleList = new CustomToggle[n];
        for(int i=0; i<n; i++){
            toggleList[i] =  new CustomToggle(this);
            toggleList[i].setBackgroundColor(0x50000000);
            toggleList[i].setSelectedColor(0xFFDC593B);
            toggleList[i].setSelectedStrokeColor(0xFFDC593B);
            toggleList[i].setUnselectedStrokeColor(0xFFFFFFFF);
            toggleList[i].setSelectedStrokeThickness(2);
            toggleList[i].setUnselectedStrokeThickness(2);
            toggleList[i].setTextColor(0xFFFFFFFF);
            toggleList[i].setcheckedTextColor(0xFFFFFFFF);
        }

        for(int i=0; i<n; i++){
            toggleList[i].setLayoutParams(layout);
            toggleList[i].setText(product.getVariations()[i]);
            int id = View.generateViewId();
            toggleList[i].setId(id);
            idList[i] = id;
            variationList.addView(toggleList[i]);
        }
        toggleList[0].setChecked(true);
        return idList;
    }

    private String getVariation(){
        int childId = variationList.getCheckedId();
        CustomToggle child = (CustomToggle) variationList.findViewById(childId);
        String color = colorMap.get(child.getText());
        if(color != null) return color;
        else return null;
    }
    private Vector3 getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new Vector3((vw.getWidth() / 2f), vw.getHeight() / 2f, 0f);
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void removeAnchorNode(Node nodeToRemove) {
        //Remove an anchor node
        if (nodeToRemove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToRemove);
            //nodeToRemove.getAnchor().detach();
            nodeToRemove.setParent(null);
            nodeToRemove = null;
            //Toast.makeText(MainActivity.this, "Object removed", Toast.LENGTH_SHORT).show();
            selectedAnchorNode = null;
        } else {
            //Toast.makeText(MainActivity.this, "No object was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] prepareData(Product product){
        String[] list = new String[product.getsuggestedProducts().length + 2];
        list[0] = "";
        list[1] = product.getTitle() +".png";
        int i = 2;
        for(Product variation : product.getsuggestedProducts()){
             list[i++] = variation.getTitle()+".png";
        }
        Log.d("TESTING", Arrays.toString(list));
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arSceneView == null) {
            return;
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            DemoUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (arSceneView != null) {
            arSceneView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (arSceneView != null) {
            arSceneView.destroy();
        }
    }

    public void builder(String URI){
        ModelRenderable.builder()
            .setSource(this, Uri.parse(URI))
            .build()
            .thenAccept(renderable -> listOfRenderable.add(renderable))
            //.thenAccept(renderable -> selectedRenderable = renderable)
            .exceptionally(throwable -> {
                Toast toast =
                        Toast.makeText(this, "Unable to load any renderable", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            });
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {
        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String filename = getFilesDir().getAbsolutePath() + File.separator + "/images" + "/Img" + timeStamp + ".jpg";
        /*ArSceneView view = fragment.getArSceneView();*/
        ArSceneView  mSurfaceView = arFragment.getArSceneView();
        Bitmap bitmap = Bitmap.createBitmap(mSurfaceView.getWidth(), mSurfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mSurfaceView.draw(canvas);
        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(mSurfaceView, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            } else {
                Log.d("DrawAR", "Failed to copyPixels: " + copyResult);
                Toast toast = Toast.makeText(this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));

        String path = "file://"+getFilesDir().getAbsolutePath() + File.separator + "images" +  File.separator + "Img" + timeStamp + ".jpg";
        Intent myIntent = new Intent(ARActivity.this, ViewImageActivity.class);
        myIntent.putExtra("imgID", path);
        myIntent.putExtra("product", product);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(myIntent);
            }
        }, 1000);
    }

    public void fillColorMap(){
        colorMap.put("Grey", "#FF898989");
        colorMap.put("Black","#FF000000");
        colorMap.put("White","#FFFFFFFF");
        colorMap.put("Purple","#FF8d32a8");
        colorMap.put("Red","#FFd9251e");
    }

}
