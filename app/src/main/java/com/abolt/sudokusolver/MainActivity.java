package com.abolt.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.abolt.sudokusolver.views.SudokuBoardView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button clear;
    private Button solve;
    private int a;
    private SudokuBoardView sudoku;
    private int[][] fin_set;
    private float dim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.n1);
        btn2 = findViewById(R.id.n2);
        btn3 = findViewById(R.id.n3);
        btn4 = findViewById(R.id.n4);
        btn5 = findViewById(R.id.n5);
        btn6 = findViewById(R.id.n6);
        btn7 = findViewById(R.id.n7);
        btn8 = findViewById(R.id.n8);
        btn9 = findViewById(R.id.n9);
        clear = findViewById(R.id.clear);
        sudoku = findViewById(R.id.board);
        solve = findViewById(R.id.solve);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        fin_set = new int[9][9];

        if(width < height){
            dim = width;
        }
        else {
            dim = height;
        }
        a = (int) (dim/6);

        ViewGroup.LayoutParams layoutParams1 = btn1.getLayoutParams();
        layoutParams1.width = a;
        btn1.setLayoutParams(layoutParams1);

        ViewGroup.LayoutParams layoutParams2 = btn2.getLayoutParams();
        layoutParams2.width = a;
        btn1.setLayoutParams(layoutParams2);

        ViewGroup.LayoutParams layoutParams3 = btn3.getLayoutParams();
        layoutParams3.width = a;
        btn1.setLayoutParams(layoutParams3);

        ViewGroup.LayoutParams layoutParams4 = btn4.getLayoutParams();
        layoutParams4.width = a;
        btn1.setLayoutParams(layoutParams4);

        ViewGroup.LayoutParams layoutParams5 = btn5.getLayoutParams();
        layoutParams5.width = a;
        btn1.setLayoutParams(layoutParams5);

        ViewGroup.LayoutParams layoutParams6 = btn6.getLayoutParams();
        layoutParams6.width = a;
        btn1.setLayoutParams(layoutParams6);

        ViewGroup.LayoutParams layoutParams7 = btn7.getLayoutParams();
        layoutParams7.width = a;
        btn1.setLayoutParams(layoutParams7);

        ViewGroup.LayoutParams layoutParams9 = btn9.getLayoutParams();
        layoutParams9.width = a;
        btn1.setLayoutParams(layoutParams9);

        ViewGroup.LayoutParams layoutParams8 = btn8.getLayoutParams();
        layoutParams8.width = a;
        btn1.setLayoutParams(layoutParams8);

        ViewGroup.LayoutParams layoutParams_clear = clear.getLayoutParams();
        layoutParams_clear.width = a;
        btn1.setLayoutParams(layoutParams_clear);

        btn1.setX(dim/50);btn1.setY(dim);
        btn2.setX(dim/5+dim/50);btn2.setY(dim);
        btn3.setX(2*dim/5+dim/50);btn3.setY(dim);
        btn4.setX(3*dim/5+dim/50);btn4.setY(dim);
        btn5.setX(4*dim/5+dim/50);btn5.setY(dim);
        btn6.setX(dim/50);btn6.setY(dim*10/9);
        btn7.setX(dim/5+dim/50);btn7.setY(dim*10/9);
        btn8.setX(2*dim/5+dim/50);btn8.setY(dim*10/9);
        btn9.setX(3*dim/5+dim/50);btn9.setY(dim*10/9);
        clear.setX(4*dim/5+dim/50);clear.setY(dim*10/9);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 1;sudoku.postInvalidate();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 2;sudoku.postInvalidate();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 3;sudoku.postInvalidate();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 4;sudoku.postInvalidate();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 5;sudoku.postInvalidate();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 6;sudoku.postInvalidate();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 7;sudoku.postInvalidate();
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 8;sudoku.postInvalidate();
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number = 9;sudoku.postInvalidate();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.number=-2 ;sudoku.postInvalidate();
            }
        });
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Python.isStarted()){
                    Python.start(new AndroidPlatform(MainActivity.this));
                }

                final Python py = Python.getInstance();
                PyObject pyObj = py.getModule("sudoku_solver");     // here we will give name of our python file

                PyObject obj = null;
                 obj = pyObj.callAttr("main", sudoku.num_set);
                 fin_set = obj.toJava(int[][].class);
                sudoku.postInvalidate();
            }
        });
    }

}