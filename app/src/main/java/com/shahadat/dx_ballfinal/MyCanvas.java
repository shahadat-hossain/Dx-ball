package com.shahadat.dx_ballfinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;


/**
 * Created by shaha on 8/26/2017.
 */


class MyCanvas extends View implements Runnable{

   public static boolean gameOver;
   public static boolean newLife;
   public static int life;

    Paint paint;
    Ball myBall;
    Bar myBar;


    public  static int canvasHeight,
                        canvasWidth;


    float barWidth = 300;


    float brickX = 0,
            brickY=50;

    int score = 0;

    float left,
            right,
            top,
            bottom;

    float downX,
            downY,
            upX,
            upY;

    boolean
            leftPos,
            rightPos,
            first = true;

    int min_distance = 50;

    int ballSpeed;


    public static int checkWidth=0;
    Bricks brick1,brick2,brick3,brick4,brick5,brick6,brick7,brick8,brick9,brick10,brick11,brick12,brick13,brick14,brick15;

    Bricks a1,a2;
    ArrayList<Bricks> bricks=new ArrayList<Bricks>();


    public MyCanvas(Context context) {
        super(context);
        paint=new Paint();

        myBar=new Bar();
        life = 2;
        gameOver=false;
        newLife=true;

    }
//Drawing on canvas
    @Override
    protected void onDraw(Canvas canvas) {

        canvasHeight=canvas.getHeight();
        canvasWidth=canvas.getWidth();

        if(first==true) {

            first = false;

            for(int i=0; i<15; i++){
                int color;

                if(brickX>=canvas.getWidth()) {
                    brickX = 0;
                    brickY += 140;
                }


                if(i%2==0)
                    color = Color.GREEN;
                else
                    color = Color.parseColor("#303F9F");

                bricks.add(new Bricks(brickX,brickY,brickX+canvas.getWidth()/5,brickY+140,color));

                brickX += canvas.getWidth() / 5;
            }


            myBall=new Ball( canvas.getWidth()/2, canvas.getHeight()/2 ,Color.GREEN, 30);
            myBall.bounce(canvas);

            left = getWidth() / 2 - (barWidth / 2);
            top = getHeight() - 20;
            right = getWidth() / 2 + (barWidth / 2);
            bottom = getHeight();

            myBar.setBottom(bottom);
            myBar.setLeft(left);
            myBar.setRight(right);
            myBar.setTop(top);
            checkWidth = canvas.getWidth();

            myBall.setDx(6);
            myBall.setDy(6);

        }

        if(newLife){

            newLife = false;
            ballSpeed =9;

            myBall=new Ball(canvas.getWidth()/2,canvas.getHeight()-50,Color.GREEN,20);

            myBall.setDx( ballSpeed );
            myBall.setDy( -ballSpeed );
        }


        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        //Ball
        canvas.drawCircle(myBall.getX(), myBall.getY(), myBall.getRadius(), myBall.getPaint());
        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        canvas.drawText("Score: "+score,10,30,paint);


        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        canvas.drawText("Life: "+life,canvas.getWidth()-110,40,paint);



        canvas.drawRect(myBar.getLeft(), myBar.getTop(), myBar.getRight(), myBar.getBottom(), myBar.getPaint());


        for(int i=0;i<bricks.size();i++){
            canvas.drawRect(bricks.get(i).getLeft(),bricks.get(i).getTop(),bricks.get(i).getRight(),bricks.get(i).getBottom(),bricks.get(i).getPaint());
        }



        if(gameOver){
            ((MainActivity)getContext()).finish();

        }


        this.ballBrickCollision(bricks,myBall,canvas);
        this.ballBarCollision(myBar,myBall, canvas);
        myBall.ballBoundaryChech(canvas);

        myBall.move();

        myBar.moveBar(leftPos,rightPos);
        this.run();


    }
    //collusion
    public void ballBarCollision(Bar myBar,Ball myBall,Canvas canvas){
        if(((myBall.getY()+myBall.getRadius())>=myBar.getTop())&&((myBall.getY()+myBall.getRadius())<=myBar.getBottom())&& ((myBall.getX())>=myBar.getLeft())&& ((myBall.getX())<=myBar.getRight())) {
            myBall.setDy(-(myBall.getDy()));

        }

    }
    public void ballBrickCollision(ArrayList<Bricks> br ,Ball myBall,Canvas canvas){
        for(int i=0;i<br.size();i++) {
            if (((myBall.getY() - myBall.getRadius()) <= br.get(i).getBottom()) && ((myBall.getY() + myBall.getRadius()) >= br.get(i).getTop()) && ((myBall.getX()) >= br.get(i).getLeft()) && ((myBall.getX()) <= br.get(i).getRight())) {

                br.remove(i);
                score+=1;
                myBall.setDy(-(myBall.getDy()));
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                downX=event.getX();
                downY=event.getY();
                return true;

            }
            case MotionEvent.ACTION_UP:{
                upX=event.getX();
                upY=event.getY();

                float deltaX=downX-upX;
                float deltaY=downY-upY;

                if(Math.abs(deltaX) > Math.abs(deltaY)){
                    if(Math.abs(deltaX) > min_distance) {
                        if (deltaX < 0) {
                            leftPos=true;
                            rightPos=false;
                            myBar.moveBar(leftPos, rightPos);
                            return true;
                        }

                        if (deltaX > 0) {
                            leftPos=false;
                            rightPos=true;
                            myBar.moveBar(leftPos,rightPos);
                            return true;

                        }
                    }
                    else{
                        return  false;
                    }
                }
                else{

                    if(Math.abs(deltaY) > min_distance) {
                        if (deltaY < 0) {
                          return true;
                        }
                        if (deltaY > 0) {
                            return true;

                        }
                    }
                    else{
                        return  false;
                    }
                }

            }

        }
        return super.onTouchEvent(event);
    }



    @Override
    public void run() {
        //reDraw
        invalidate();
    }

}
