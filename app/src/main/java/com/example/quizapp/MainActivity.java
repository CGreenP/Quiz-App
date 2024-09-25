package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizapp.databinding.ActivityMainBinding;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuestionList;
import com.example.quizapp.viewmodel.QuizViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    QuizViewModel quizViewModel;
    List<Question> questionList;

    static int result = 0;
    static int totalQuestions=0;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Data Binding
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);//Resetting the Scores:
        result=0;
        totalQuestions=0;

        //Creating an instance of QuizViewModel
        quizViewModel= new ViewModelProvider(this).get(QuizViewModel.class);

        //Display the first question:
        DisplayFirstQuestion();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayNextQuestions();
            }
        });
    }

    public void DisplayFirstQuestion(){

        //Observing Livedata from a ViewModel
        quizViewModel.getQuestionListLiveData().observe(this, new Observer<QuestionList>() {
            @Override
            public void onChanged(QuestionList questions) {
                if (questions != null && !questions.isEmpty()) {
                    // Questions loaded successfully
                    questionList = questions;
                    totalQuestions = questionList.size();
                    binding.txtQuestion.setText("Question 1:\n" + questions.get(0).getQuestion());
                    binding.radio1.setText(questions.get(0).getOption1());
                    binding.radio2.setText(questions.get(0).getOption2());
                    binding.radio3.setText(questions.get(0).getOption3());
                    binding.radio4.setText(questions.get(0).getOption4());
                    if (questionList.size() == 1) {
                        binding.btnNext.setText("Finish");
                    }

                    // Show questions and hide error
                    binding.errorText.setVisibility(View.GONE);
                    binding.txtQuestion.setVisibility(View.VISIBLE);
                    binding.radioGroup.setVisibility(View.VISIBLE);
                    binding.btnNext.setVisibility(View.VISIBLE);
                    binding.txtResult.setVisibility(View.VISIBLE);
                } else {
                    // Error loading questions
                    binding.errorText.setVisibility(View.VISIBLE);
                    binding.txtQuestion.setVisibility(View.GONE);
                    binding.radioGroup.setVisibility(View.GONE);
                    binding.btnNext.setVisibility(View.GONE);
                    binding.txtResult.setVisibility(View.GONE);
                }
            }
        });
    }

    public void DisplayNextQuestions(){
        totalQuestions= questionList.size();
        int selectedOption=binding.radioGroup.getCheckedRadioButtonId();
        //Displaying the question
        if (selectedOption != -1){
            RadioButton radioButton=findViewById(selectedOption);
            if(binding.btnNext.getText().equals("Finish")){
                if (selectedOption != -1){
                    if(radioButton.getText().toString().equals(
                            questionList.get(i).getCorrectOption()
                    )){
                        result++;
                        binding.txtResult.setText("Correct Answers: "+result);
                    }
                    i++;
                    binding.radioGroup.clearCheck();

                }
                Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(i);
                finish();
            }
            // More Questions to Display??
            if((questionList.size() -i)>0){

                //Check if the chosen option is correct
                if(radioButton.getText().toString().equals(
                        questionList.get(i).getCorrectOption()
                )){
                    result++;
                    binding.txtResult.setText("Correct Answers: "+result);
                }
                i++;
                //Displaying the next question
                binding.txtQuestion.setText("Question "+(i+1)+":\n"+questionList.get(i).getQuestion());
                binding.radio1.setText(questionList.get(i).getOption1());
                binding.radio2.setText(questionList.get(i).getOption2());
                binding.radio3.setText(questionList.get(i).getOption3());
                binding.radio4.setText(questionList.get(i).getOption4());

                //Check if it is last question
                if(i==(questionList.size()-1)){
                    binding.btnNext.setText("Finish");
                }

                binding.radioGroup.clearCheck();
            }
        }else {
            Toast.makeText(this, "You need to make a selection", Toast.LENGTH_SHORT).show();
        }

    }
}