package com.abolt.sudokusolver.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import org.w3c.dom.Attr;

public class SudokuBoardView extends View {

    private RectF sudoku_Rect;
    private Paint sudoku_paint;
    private Paint number_paint;
    private Paint highlighter;
    private Paint error;
    private int dimension;
    private float cellLines;
    public float get_x;
    public float get_y;
    public int number;
    private int temp;
    public int[] num_set;
    private int check;
    private int highlight;
    public int done;


    public SudokuBoardView(Context context) {
        super(context);
        init(null);
    }

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        sudoku_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sudoku_Rect = new RectF();
        number_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlighter = new Paint(Paint.ANTI_ALIAS_FLAG);
        error = new Paint((Paint.ANTI_ALIAS_FLAG));
        error.setColor(Color.RED);
        highlighter.setColor(Color.rgb(255,255,102));
        sudoku_paint.setColor(Color.rgb(23,4,66));
        number_paint.setColor(Color.BLACK);
        number = -1;
        done = 0;
        get_x = 0;
        get_y = 0;
        temp = 0;
        highlight = 0;
        check = 0;
        num_set = new int[81];
        for(int i =0; i< 81; i++){
            for(int j = 0; j<2 ; j++){
                num_set[i] = -1;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        } else {
            size = width;
        }
        setMeasuredDimension(size, size);
        dimension = size;
        cellLines = dimension / 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(highlight == 1) {
            Highlight(canvas);
        }

        drawSudokuSquare(canvas);
        drawNumbers(canvas);

    }

    private void drawBorder() {
        sudoku_paint.setStyle(Paint.Style.STROKE);
        sudoku_paint.setStrokeWidth(dimension/33);
    }

    private void drawHeavyLine() {
        sudoku_paint.setStyle(Paint.Style.STROKE);
        sudoku_paint.setStrokeWidth(dimension/108);

    }

    private void drawLightLine() {
        sudoku_paint.setStyle(Paint.Style.STROKE);
        sudoku_paint.setStrokeWidth(dimension/180);
    }

    private void drawSudokuSquare(Canvas canvas) {
        for (int c = 0; c < 10; c++) {
            if (c == 0 || c == 9) {
                drawBorder();
            } else if (c % 3 == 0) {
                drawHeavyLine();
            } else {
                drawLightLine();
            }
            canvas.drawLine(cellLines * c, 0, cellLines * c, dimension, sudoku_paint);
        }

        for (int r = 0; r < 10; r++) {
            if (r == 0 || r == 9) {
                drawBorder();
            } else if (r % 3 == 0) {
                drawHeavyLine();
            } else {
                drawLightLine();
            }
            canvas.drawLine(0, cellLines * r, dimension, cellLines * r, sudoku_paint);
        }

    }

    private int assignNumbers() {
        int i = 0, j = 0,m,n,p;

        for (i = 8; i >= 0; i--) {
            if (get_x > cellLines * i) {
                break;
            }
        }
        for (j = 8; j >= 0; j--) {
            if (get_y > cellLines * j) {
                break;
            }
        }

        if(number == -2 && 9*i+j > 0){         // for clear button case
            get_y = 0; get_x = 0;num_set[9 * i + j] = -1;number = -1;
        }


        if(number != -1 && 9*i+j >= 0){
            m = box_check(i,j);
            if(m != -1){
                return (m);
            }
            n = row_check(i,j);
            if(n != -1){
                return (n);
            }
            p = col_check(i,j);
            if(p != -1){
                return (p);
            }

            num_set[9 * i + j] = number;

        }
        return -1;
    }

    private void drawNumbers(Canvas canvas){
        int x = 0, y = 0, res;
        res = assignNumbers();
        if(res != -1){
            canvas.drawRect(cellLines*(res/9), cellLines*(res%9), cellLines*(res/9 + 1), cellLines*((res%9)+ 1), error );
        }
        number_paint.setTextSize(5*cellLines/7);
        for(int i = 0; i<81; i++){
            if(num_set[i]!= -1 && num_set[i]!= -2){
                x = i/9; y = i%9;
                canvas.drawText("" + num_set[i], cellLines * x + (9 * cellLines / 20) - cellLines / 6,
                        cellLines * y + (9 * cellLines / 20) + cellLines / 3, number_paint);
            }
        }
        number = -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                get_x = event.getX();
                get_y = event.getY();
                highlight = 1;
                postInvalidate();

                return true;
            }

        }
        return value;
    }
    private void Highlight(Canvas c){
        int i = 0, j = 0, k = 0;
        float m = (float)dimension/190, n = (float) dimension/70;


        for (i = 9; i >= 0; i--) {
            if (get_x > cellLines * i) {
                break;
            }
        }
        for (j = 9; j >= 0; j--) {
            if (get_y > cellLines * j) {
                break;
            }
        }

        if(i >= 8 && j>=8){
            sudoku_Rect.top = cellLines*j + m;
            sudoku_Rect.left= cellLines*i + m;
            sudoku_Rect.bottom=cellLines*(j+1) - n ;
            sudoku_Rect.right=cellLines*(i+1) - n;
        } else if (i >= 8 && j < 1) {
            sudoku_Rect.top = cellLines*j + n;
            sudoku_Rect.left= cellLines*i + m;
            sudoku_Rect.bottom=cellLines*(j+1) - m ;
            sudoku_Rect.right=cellLines*(i+1) - n;

        }
        else if (i< 1 && j>=8){
            sudoku_Rect.top = cellLines*j + m;
            sudoku_Rect.left= cellLines*i + n;
            sudoku_Rect.bottom=cellLines*(j+1) - n ;
            sudoku_Rect.right=cellLines*(i+1) - m;

        }
        else if(i<1 && j<1){
            sudoku_Rect.top = cellLines*j + n;
            sudoku_Rect.left= cellLines*i + n;
            sudoku_Rect.bottom=cellLines*(j+1) - m ;
            sudoku_Rect.right=cellLines*(i+1) - m;
        }
        else if( i>=8){
            sudoku_Rect.top = cellLines*j + m;
            sudoku_Rect.left= cellLines*i + m;
            sudoku_Rect.bottom=cellLines*(j+1) - m ;
            sudoku_Rect.right=cellLines*(i+1) - n;
        }
        else if(i==0){
            sudoku_Rect.top = cellLines*j + m;
            sudoku_Rect.left= cellLines*i + n;
            sudoku_Rect.bottom=cellLines*(j+1) - m ;
            sudoku_Rect.right=cellLines*(i+1) - m;
        }
        else if(j>=8){
            sudoku_Rect.top = cellLines*j + m;
            sudoku_Rect.left= cellLines*i + m;
            sudoku_Rect.bottom=cellLines*(j+1) - n ;
            sudoku_Rect.right=cellLines*(i+1) - m;
        }
        else if(j==0){
            sudoku_Rect.top = cellLines*j + n;
            sudoku_Rect.left= cellLines*i + m;
            sudoku_Rect.bottom=cellLines*(j+1) - m ;
            sudoku_Rect.right=cellLines*(i+1) - m;
        }
        else {
            sudoku_Rect.top = cellLines * j + dimension / 160;
            sudoku_Rect.left = cellLines * i + dimension / 160;
            sudoku_Rect.bottom = cellLines * (j + 1) - dimension / 160;
            sudoku_Rect.right = cellLines * (i + 1) - dimension / 160;
        }

        c.drawRect(sudoku_Rect, highlighter);
        highlight = 0;
    }
    private int row_check(int i, int j){
        int n =0;
        for(n = 0; n < 9; n++){
            if(num_set[9*n+j] == number){
                return 9*n+j;
            }
        }
        return -1;
    }

    private int col_check(int i, int j){
        int n =0;
        for(n = 0; n < 9; n++){
            if(num_set[9*i+n] == number){
                return 9*i + n;
            }
        }
        return -1;
    }

    private int box_check(int i, int j){
        int n =0, m= 0;
        if(i < 3 && j < 3){
            for(m=0;m<3;m++){
                for(n=0;n<3;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 6 && j < 3){
            for(m=3;m<6;m++){
                for(n=0;n<3;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 9 && j < 3){
            for(m=6;m<9;m++){
                for(n=0;n<3;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 3 && j < 6){
            for(m=0;m<3;m++){
                for(n=3;n<6;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 6 && j < 6){
            for(m=3;m<6;m++){
                for(n=3;n<6;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 9 && j < 6){
            for(m=6;m<9;m++){
                for(n=3;n<6;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 3 && j < 9){
            for(m=0;m<3;m++){
                for(n=6;n<9;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 6 && j < 9){
            for(m=3;m<6;m++){
                for(n=6;n<9;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        else if(i < 9 && j < 9){
            for(m=6;m<9;m++){
                for(n=6;n<9;n++) {
                    if (num_set[9*m+n] == number){
                        return 9*m+n;
                    }
                }
            }
        }
        return -1;
    }

    private void error_highlight(int i, int j){

    }
}
