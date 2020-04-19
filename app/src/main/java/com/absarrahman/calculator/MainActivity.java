package com.absarrahman.calculator;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private EditText newNumberStore;
    private TextView operationView;

    //These variables are needed for storing operands and calculation stuffs

    private Double firstOperand;
    private Double secondOperand;
    private String pendingOperation = "=";
    private static final String TAG = "MainActivity"; //logt for generating it
    private final String STATE_PENDING_OPERATION = "PendingOperation";
    private final String STATE_OPERAND1 = "OperandOne";
    private final String STATE_OPERATION_VIEW = "OperationView";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeButtonReference();
        doTask();

    }

    private void initializeButtonReference(){
        result = findViewById(R.id.result);
        operationView = findViewById(R.id.operation);
        newNumberStore = findViewById(R.id.newNumber);
    }

    private void doTask(){
        Button digitHolder[] = {
                findViewById(R.id.button0),
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5),
                findViewById(R.id.button6),
                findViewById(R.id.button7),
                findViewById(R.id.button8),
                findViewById(R.id.button9),
                findViewById(R.id.buttonDot),
                findViewById(R.id.buttonNEG)
        }; //holds digits from button 0-9 and dot

        final Button operationHolder[] = {
                findViewById(R.id.buttonEquals),
                findViewById(R.id.buttonPlus),
                findViewById(R.id.buttonMinus),
                findViewById(R.id.buttonMultiply),
                findViewById(R.id.buttonDivide)
        }; // for holding = + - * and /

        View.OnClickListener listener = new View.OnClickListener() { //for numbers
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if((b.getText().toString().equals("NEG"))) {
                    newNumberStore.append("-");
                } else {
                    newNumberStore.append(b.getText().toString());
                }
            }
        };

        for (Button button:
                digitHolder) {
            button.setOnClickListener(listener);
        }

        View.OnClickListener operationListener = new View.OnClickListener() { //for operation
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String op = b.getText().toString();
                String value = newNumberStore.getText().toString();

                try {
                    Double number = Double.valueOf(value);
                    performOperation(op,number);
                } catch (NumberFormatException e){
                    newNumberStore.setText("");
                }

                pendingOperation = op;
                operationView.setText(pendingOperation);
            }
        };

        for (Button button:
                operationHolder) {
            button.setOnClickListener(operationListener);
        }

    }

    private void performOperation(String operation,Double value){
        if(firstOperand==null){
            firstOperand = value;
        } else {
            secondOperand = value;
            if(pendingOperation.equals("=")){
                pendingOperation = operation;
            }

            switch (pendingOperation){
                case "=":
                    firstOperand = secondOperand;
                    break;
                case "/":
                    if(secondOperand==0){
                        firstOperand = 0.0;
                    } else {
                        firstOperand /= secondOperand;
                    }
                    break;
                case "*":
                    firstOperand*=secondOperand;
                    break;
                case "+":
                    firstOperand+=secondOperand;
                    break;
                case "-":
                    firstOperand-=secondOperand;
                    break;
            }
        }

        result.setText(firstOperand.toString());
        newNumberStore.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(firstOperand!=null) {
            outState.putDouble(STATE_OPERAND1, firstOperand);
        }
        outState.putString(STATE_PENDING_OPERATION,pendingOperation);
        outState.putString(STATE_OPERATION_VIEW,operationView.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        firstOperand = savedInstanceState.getDouble(STATE_OPERAND1);
        operationView.setText(savedInstanceState.getString(STATE_OPERATION_VIEW));
    }
}
