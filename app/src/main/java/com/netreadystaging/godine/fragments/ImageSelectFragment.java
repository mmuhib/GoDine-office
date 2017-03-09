package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.callbacks.ImageSelectCallBack;

import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.technobuff.helper.utils.PermissionRequestHandler;


public class ImageSelectFragment extends Fragment {

    private final int PERMISSIONS_REQUEST_CAMERA = 0 ;
    private final int PERMISSIONS_REQUEST_GALLARY = 1 ;
    private static final int PICK_IMAGE = 101;
    private static final int PICK_Camera_IMAGE = 102;
    Uri imageUri;
    private Bitmap bitmap;
    private String bitmapString;
    private String format;
    private ImageSelectCallBack callback;
    AppGlobal appGlobal = AppGlobal.getInatance() ;
    final String check= appGlobal.getIsVerificationImageUploaded();
    public void selectImage(String format , final ImageSelectCallBack callback) {
        this.format  = format ;
        this.callback =  callback ;
        if(check.equalsIgnoreCase("1")) {
            final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
            calldialog(items);
        }
        else  if(check.equalsIgnoreCase("0")) {
            final CharSequence[] items = {"Take Photo","Cancel"};
            calldialog(items);
        }
    }

    private void calldialog(final CharSequence[] items) {

       Log.d("Image",""+appGlobal.getIsVerificationImageUploaded());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Image");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result = Utility.checkPermission(EditUserProfileDetailFragment.this);
                if (items[item].equals("Take Photo")) {
                    // if (result) {
                    if(PermissionRequestHandler.requestPermissionToCamera(getActivity(),ImageSelectFragment.this))
                    {
                        camFunction();
                    }
                    // }

                } else if (items[item].equals("Choose from Gallery")) {
                    // if (result)
                    // {
                    if(PermissionRequestHandler.requestPermissionToGallary(getActivity(),ImageSelectFragment.this))
                    {
                        gallaryFun();
                    }
                    //  }

                } else if (items[item].equals("Cancel")) {
                    if(check.equalsIgnoreCase("0"))
                    {
                        String data=getResources().getString(R.string.info);
                        AlertDialog.Builder builde=new AlertDialog.Builder(getContext());
                        builde.setTitle("Info");
                        builde.setMessage(data);
                        builde.setCancelable(false);
                        builde.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectImage("bitmap", callback);
                            }
                        });
                        builde.create();
                        builde.show();
                    }
                    else {
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private  void gallaryFun(){

        try {

            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            Intent gintent = new Intent();
            gintent.setType("image/*");
            gintent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    i,
                    PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(getActivity(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }
    private void camFunction() {
        // TODO Auto-generated method stub

        // String fileName = "new-photo-name.jpg";

        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //create parameters for Intent with filename
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
        //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, PICK_Camera_IMAGE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    camFunction();
                }
                return;
            }
            case PERMISSIONS_REQUEST_GALLARY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { gallaryFun();}
                return;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    if(check.equalsIgnoreCase("0")) {
                        Fragment fragment = null;
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        fragment = new ProfilePageFragment();
                        transaction.replace(R.id.flContent, fragment);
                        transaction.commit();
                    }
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == Activity.RESULT_OK)
                {
                    //use imageUri here to access the image
                    //*Bitmap mPic = (Bitmap) data.getExtras().get("data");
                    //selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*//*
                    selectedImageUri = imageUri;
                }

                else if (resultCode == Activity.RESULT_CANCELED) {
                    if(check.equalsIgnoreCase("0"))
                    {
                        Fragment fragment = null;
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        fragment = new ProfilePageFragment();
                        transaction.replace(R.id.flContent, fragment);
                        transaction.commit();
                    }
                    else {
                        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                    }

                    //    selectImage("bitmap",callback);
                } else {
                    Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if(selectedImageUri != null){
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getActivity(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                decodeFile(filePath);


            } catch (Exception e) {
                Toast.makeText(getActivity(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        //Bitmap bp=null;
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        /****************/
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif != null ? exif.getAttribute(ExifInterface.TAG_ORIENTATION) : null;
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() , (float) bitmap.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width_tmp, height_tmp, matrix, true);

        /**************/

//        if(bitmap.getWidth()<bitmap.getHeight())
//        {
           // Utility.message(getContext(),"Width is greater");
            bitmapString= Utility.BitMapToString(rotatedBitmap);
            switch(this.format)
            {
                case "base64" :
                    this.callback.success(bitmapString);
                    break ;

                case "bitmap" :
                    this.callback.success(bitmap);
                    break ;

                default :
                    this.callback.success(filePath);
                    break ;
            }
      //  }
//        else
//        {
//        //    Utility.message(getContext(),"Please Take Image Vertically");
//       //  Utility.Alertbox(getContext(),"Info","Please Take Image Vertically","OK");
//            AlertDialog.Builder builde=new AlertDialog.Builder(getContext());
//            builde.setTitle("Info");
//            builde.setMessage("Please Take Image Vertically");
//            builde.setCancelable(false);
//            builde.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    selectImage("bitmap", callback);
//                }
//            });
//            builde.create();
//            builde.show();
//
//        }

    }
}

