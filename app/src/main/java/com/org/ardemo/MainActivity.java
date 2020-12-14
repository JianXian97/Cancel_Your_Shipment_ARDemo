package com.org.ardemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
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
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.org.ardemo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private boolean installRequested;
    ImageButton removeButton, addObjectButton, takeImageButton, openGalleryButton, earthIcon, paperAirplaneIcon, chairIcon, addToCartMenu;
    Button variationList;
    ArFragment arFragment;
    ImageView selectedProduct, shopPicture;
    TextView price, stock, shopName, shopLastSeen, shopProductCount, shopRating, shopChatResponse;
    LinearLayout addToCartMenuLayout, addToCartMenuLayoutSlider, objChooser;
    RelativeLayout shopDetails;
    ModelRenderable selectedRenderable;
    private boolean capturePicture = false;
    Node selectedAnchorNode;
    boolean addObject, deleteObject;
    private ArSceneView arSceneView;
    private boolean autoadded;
    ArrayList<ModelRenderable> listOfRenderable;
    ArrayList<Uri> imgUriList;

    int[] productURI = {R.drawable.paper_airplane,R.drawable.earth,R.drawable.chair};
    String[] productPrice = {"100.50","70.90","900.00"};
    String[] productStock = {"420","18","7"};
    shop shopA = new shop(R.drawable.company_logo_1, "Houze", 17, 420,73,4.8);
    shop shopB = new shop(R.drawable.company_logo_2, "Repoe", 23, 982,99,4.7);
    shop shopC = new shop(R.drawable.company_logo_3, "Origami", 10, 77,89,4.5);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main);
        removeButton = findViewById(R.id.removeButton);
        takeImageButton = findViewById(R.id.takeImage);
        openGalleryButton = findViewById(R.id.openGallery);
        earthIcon = findViewById(R.id.globe_icon);
        paperAirplaneIcon = findViewById(R.id.paperAeroplane_icon);
        chairIcon = findViewById(R.id.chair_icon);
        addToCartMenu = findViewById(R.id.addToCartMenu);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        addObject = false;
        objChooser = findViewById(R.id.objChooser);
        selectedProduct = findViewById(R.id.selectedProduct);
        price = findViewById(R.id.price);
        stock = findViewById(R.id.stockCount);
        variationList = findViewById(R.id.variationList);
        variationList.setVisibility(View.INVISIBLE);
        addToCartMenuLayout = findViewById(R.id.addToCartMenuLayout);
        addToCartMenuLayoutSlider = findViewById(R.id.addToCartMenuLayoutSlider);
        shopPicture = findViewById(R.id.shopPicture);
        shopName = findViewById(R.id.shopName);
        shopLastSeen = findViewById(R.id.shopLastSeen);
        shopProductCount = findViewById(R.id.shopProductCount);
        shopRating = findViewById(R.id.shopRating);
        shopChatResponse = findViewById(R.id.shopChatResponse);
        shopDetails = findViewById(R.id.shopDetails);
        addToCartMenuLayout.setFocusable(true);
        listOfRenderable = new ArrayList<>();
        builder("Paper Airplane.sfb");
        builder("NOVELO_EARTH.sfb");
        builder("Chair.sfb");
        autoadded = false;
        arFragment.setOnTapArPlaneListener((HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
            if (selectedRenderable == null){
                return;
            }
            Anchor anchor = hitresult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode globe = new TransformableNode(arFragment.getTransformationSystem());
            globe.setParent(anchorNode);
            globe.setRenderable(selectedRenderable);
            globe.select();
            }
        );

        Scene scene = arFragment.getArSceneView().getScene();
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
                        Toast.makeText(MainActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                        selectedAnchorNode = hitNode;
                    }
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAnchorNode(selectedAnchorNode);
            }
        });
        paperAirplaneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectedRenderable = listOfRenderable.get(0);
               activateObject("Paper Airplane",0);
               shopC.displayInformation(shopPicture,shopName,shopLastSeen,shopProductCount,shopRating,shopChatResponse);
            }
        });
        earthIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRenderable = listOfRenderable.get(1);
                activateObject("Earth",1);
                shopB.displayInformation(shopPicture,shopName,shopLastSeen,shopProductCount,shopRating,shopChatResponse);
            }
        });
        chairIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRenderable = listOfRenderable.get(2);
                activateObject("Chair",2);
                shopA.displayInformation(shopPicture,shopName,shopLastSeen,shopProductCount,shopRating,shopChatResponse);
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
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUriList = getImgUriList();
                Intent myIntent = new Intent(MainActivity.this, GalleryActivity.class);
                myIntent.putParcelableArrayListExtra("imgUriList", imgUriList); //Optional parameters
                startActivity(myIntent);
            }
        });
        addToCartMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(addToCartMenuLayout.getVisibility() == View.INVISIBLE) addToCartMenuLayout.setVisibility(View.VISIBLE);
                 else addToCartMenuLayout.setVisibility(View.INVISIBLE);
            }
        });
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);


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
                                transformableNode.setRenderable(MainActivity.this.selectedRenderable);

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

    private void activateObject(String Object, int position){
        shopDetails.setVisibility(View.VISIBLE);
        variationList.setVisibility(View.VISIBLE);
        addToCartMenuLayoutSlider.setVisibility(View.VISIBLE);
        //Toast.makeText(MainActivity.this, Object + " Object Activated", Toast.LENGTH_SHORT).show();
        selectedProduct.setImageResource(productURI[position]);
        price.setText("$"+productPrice[position]);
        stock.setText("Stocks:" + productStock[position]);
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
        Log.d("HELP!@!@!",bitmap.toString());
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
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Rect ContentBounds = new Rect();
//        addToCartMenuLayout.getHitRect(ContentBounds);
//        Rect ContentBounds1 = new Rect();
//        addToCartMenu.getHitRect(ContentBounds1);
//        if (!ContentBounds.contains((int) ev.getX(), (int) ev.getY()) && !ContentBounds1.contains((int) ev.getX(), (int) ev.getY())) {
//            addToCartMenuLayout.setVisibility(View.INVISIBLE);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public ArrayList<Uri> getImgUriList(){
        ArrayList<Uri> list = new ArrayList<>();
        File dir = new File(getFilesDir().getAbsolutePath() + File.separator + "/images");
        for(File f : dir.listFiles()) {
            if (f.isFile()) {
                list.add(Uri.fromFile(f));
            }
        }
        return list;
    }

}