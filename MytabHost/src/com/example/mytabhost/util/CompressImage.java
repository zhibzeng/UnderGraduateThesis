package com.example.mytabhost.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class CompressImage {
	
	
	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}
	
	
	
	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 320, 400);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    return BitmapFactory.decodeFile(filePath, options);
	    }
	
	
	
	//把bitmap转换成String
	public static String bitmapToString(String filePath) {

	        Bitmap bm = getSmallBitmap(filePath);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] b = baos.toByteArray();
	        return Base64.encodeToString(b, Base64.DEFAULT);
	    }
	
	  static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
      {
                int width = drawable.getIntrinsicWidth();   // 取drawable的长宽
                int height = drawable.getIntrinsicHeight();
              Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565; //取drawable的颜色格式
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应bitmap
                Canvas canvas = new Canvas(bitmap);         // 建立对应bitmap的画布
                drawable.setBounds(0, 0, width, height);
                drawable.draw(canvas);      // 把drawable内容画到画布中
                return bitmap;
      }
	 public static Drawable zoomDrawable(Drawable drawable, float w, float h)
     {
               int width = drawable.getIntrinsicWidth();
               int height= drawable.getIntrinsicHeight();
               Bitmap oldbmp = drawableToBitmap(drawable); // drawable转换成bitmap
               Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象
               float scaleWidth = ((float)w / width);   // 计算缩放比例
               float scaleHeight = ((float)h / height);
               matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
               Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);// 建立新的bitmap，其内容是对原bitmap的缩放后的图
               return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回
     }

}
