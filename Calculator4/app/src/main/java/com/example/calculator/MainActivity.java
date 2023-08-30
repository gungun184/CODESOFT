package com.example.calculator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.text.DecimalFormat;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonMinus, buttonPlus, buttonEqual;
    MaterialButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    MaterialButton buttonAC, buttonDot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);
        assignId(buttonC, R.id.button_c);
        assignId(buttonAC, R.id.button_AC);
        assignId(buttonDot, R.id.button_dot);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(button0, R.id.button_0);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonEqual, R.id.button_equal);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String datatoCalculate = solutionTv.getText().toString();

        if (buttonText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        if (buttonText.equals("=")) {
            if (datatoCalculate.isEmpty()) {
                resultTv.setText("");
                return;
            } try {
                   String finalResult = calculateResult(datatoCalculate);
                   double value = Double.parseDouble(finalResult);
                   resultTv.setText(formatResult(value));
                }  catch (NumberFormatException e) {
                           resultTv.setText("Error");
                 }  catch (ArithmeticException e) {
                          resultTv.setText("Err: Division by zero");
            }
            return;
        }

        if (buttonText.equals("C")) {
                      datatoCalculate = datatoCalculate.substring(0, datatoCalculate.length() - 1);
        }
        else {
            datatoCalculate = datatoCalculate + buttonText;
        }
        solutionTv.setText(datatoCalculate);

    }
    private String formatResult(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######"); // Format up to 6 decimal places
        String formattedValue = decimalFormat.format(value);
        if (formattedValue.contains(".") && formattedValue.endsWith("0")) {
            formattedValue = formattedValue.substring(0, formattedValue.length() - 1);
            formattedValue = formattedValue.endsWith(".") ? formattedValue.substring(0, formattedValue.length() - 1) : formattedValue;
        }
        return formattedValue;
    }
    private String calculateResult(String expression) {
        try {
            return String.valueOf(eval(expression));
        } catch (Exception e) {
            return "Err";
        }
    }
    public double eval(final String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        x /= divisor;
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                int startPos = this.pos;

                if ((ch >= '0' && ch <= '9') || ch == '.') { // Numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    return Double.parseDouble(expression.substring(startPos, this.pos));
                }

                throw new RuntimeException("Unexpected: " + (char) ch);
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }
        }.parse();
    }
}