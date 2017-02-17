package com.java.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class MainActivity
    extends AppCompatActivity
    implements OnTouchListener, OnGlobalLayoutListener
{

  private final List<Bitmap> bmp = new ArrayList<>();

  float dX;

  float dY;

  private ImageView imgToMove;

  private ImageView img1;

  private float defaultX;

  private float defaultY;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    createBitmaps(BitmapFactory.decodeResource(getResources(), R.drawable.img_3));

    imgToMove = (ImageView) findViewById(R.id.imgToMove);
    imgToMove.setImageBitmap(bmp.get(0));
    imgToMove.setOnTouchListener(this);

    defaultX = imgToMove.getX();
    defaultY = imgToMove.getY();

    img1 = (ImageView) findViewById(R.id.img1);
    img1.getViewTreeObserver().addOnGlobalLayoutListener(this);
  }

  @Override
  public boolean onTouch(View view, MotionEvent event)
  {
    switch (event.getAction())
    {

      case MotionEvent.ACTION_DOWN:

        dX = view.getX() - event.getRawX();
        dY = view.getY() - event.getRawY();
        break;

      case MotionEvent.ACTION_MOVE:

        view.animate()
            .x(event.getRawX() + dX)
            .y(event.getRawY() + dY)
            .setDuration(0)
            .start();
        break;

      case MotionEvent.ACTION_UP:
        if (isViewInBounds(img1, (int) event.getRawX(), (int) event.getRawY()))
        {
          img1.setImageBitmap(bmp.get(0));
          imgToMove.setX(defaultX);
          imgToMove.setY(defaultY);
          imgToMove.setImageBitmap(bmp.get(1));
        }
        break;
      default:
        return false;
    }

    return true;
  }

  @Override
  public void onGlobalLayout()
  {
    final LayoutParams layoutParams = imgToMove.getLayoutParams();
    layoutParams.height = img1.getHeight();
    layoutParams.width = img1.getWidth();

    imgToMove.setLayoutParams(layoutParams);
  }

  public void createBitmaps(Bitmap source)
  {
    int width = source.getWidth();
    int height = source.getHeight();
    for (int i = 0; i < 3; i++)
    {
      for (int j = 0; j < 3; j++)
      {
        bmp.add(Bitmap.createBitmap(source, (width * j) / 3, (i * height) / 3, width / 3, height / 3));
      }

    }
  }

  private boolean isViewInBounds(View view, int x, int y)
  {
    final Rect outRect = new Rect();
    int[] location = new int[2];

    view.getDrawingRect(outRect);
    view.getLocationOnScreen(location);
    outRect.offset(location[0], location[1]);
    return outRect.contains(x, y);
  }
}
