package com.example.study.greendao;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.study.R;
import com.example.studysdk.util.DataUtil;
import com.liang.log.MLog;

public class GreenDaoTestActivity extends AppCompatActivity {

    private final String TAG = "GreenDaoTestActivity, ";

    Button insertBtn;
    Button updateBtn;
    Button deleteBtn;
    Button queryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_green_dao_test);
        initialize();
    }

    private void initialize() {
        insertBtn = findViewById(R.id.insert);
        updateBtn = findViewById(R.id.update);
        deleteBtn = findViewById(R.id.delete);
        queryBtn = findViewById(R.id.query);
        insertBtn.setOnClickListener(v -> insert());
//        updateBtn.setOnClickListener(v -> update());
//        deleteBtn.setOnClickListener(v -> delete());
        queryBtn.setOnClickListener(v -> query());
    }

    private void query() {
        DBManager.getInstance().query();
//        boolean tableExists = DBManager.getInstance().isTableExists("STUDENT");
//        MLog.i(TAG + "tableExists: " + tableExists);

    }

    private void insert() {
        DBManager.getInstance().insertStudent(new Student(1,1, DataUtil.generate(3)));
    }
}