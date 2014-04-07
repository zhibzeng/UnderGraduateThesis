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
	
	
	//����ͼƬ������ֵ
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
	
	
	
	// ����·�����ͼƬ��ѹ��������bitmap������ʾ
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
	
	
	
	//��bitmapת����String
	public static String bitmapToString(String filePath) {

	        Bitmap bm = getSmallBitmap(filePath);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] b = baos.toByteArray();
	        return Base64.encodeToString(b, Base64.DEFAULT);
	    }
	
	  static Bitmap drawableToBitmap(Drawable drawable) // drawable ת����bitmap
      {
                int width = drawable.getIntrinsicWidth();   // ȡdrawable�ĳ���
                int height = drawable.getIntrinsicHeight();
              Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565; //ȡdrawable����ɫ��ʽ
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // ������Ӧbitmap
                Canvas canvas = new Canvas(bitmap);         // ������Ӧbitmap�Ļ���
                drawable.setBounds(0, 0, width, height);
                drawable.draw(canvas);      // ��drawable���ݻ���������
                return bitmap;
      }
	 public static Drawable zoomDrawable(Drawable drawable, float w, float h)
     {
               int width = drawable.getIntrinsicWidth();
               int height= drawable.getIntrinsicHeight();
               Bitmap oldbmp = drawableToBitmap(drawable); // drawableת����bitmap
               Matrix matrix = new Matrix();   // ��������ͼƬ�õ�Matrix����
               float scaleWidth = ((float)w / width);   // �������ű���
               float scaleHeight = ((float)h / height);
               matrix.postScale(scaleWidth, scaleHeight);         // �������ű���
               Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);// �����µ�bitmap���������Ƕ�ԭbitmap�����ź��ͼ
               return new BitmapDrawable(newbmp);       // ��bitmapת����drawable������
     }

}
