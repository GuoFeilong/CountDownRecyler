package countdown.com.countdownrecyler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.rlv_countdown)
    RecyclerView mCountDownRecylerView;
    private List<CountDownEntity> mCountDownEntities;
    private CountDownAdapter mCountDownAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        mCountDownRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mCountDownRecylerView.setAdapter(mCountDownAdapter);
        startCountDownTaskByRxAndroid();
    }

    private void startCountDownTaskByRxAndroid() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        modifyRecylerData();
                        mCountDownAdapter.notifyDataSetChanged();
                    }
                });
    }


    /**
     * 修改数据源
     */
    private void modifyRecylerData() {
        for (int i = 0; i < mCountDownEntities.size(); i++) {
            CountDownEntity temp = mCountDownEntities.get(i);
            long currentCountDownTime = temp.getCountDownTimeTotal();
            if (currentCountDownTime >= 1) {
                long tempCT = currentCountDownTime - 1;
                temp.setCountDownTimeTotal(tempCT);
                temp.setCountDownDesc("剩余时间:" + TimeFormat.formatTime(tempCT));
            } else {
                continue;
            }
        }
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void initData() {
        mCountDownEntities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            CountDownEntity countDownEntity = new CountDownEntity();
            countDownEntity.setCountDownTimeTotal(100 + new Random().nextInt(600));
            countDownEntity.setContDownDrawable(R.mipmap.ic_launcher);
            countDownEntity.setCountDownDesc(("剩余时间:") + 100 + new Random().nextInt(600));
            mCountDownEntities.add(countDownEntity);
        }
        mCountDownAdapter = new CountDownAdapter(mCountDownEntities);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class CountDownAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CountDownEntity> recylerData;

        public CountDownAdapter(List<CountDownEntity> recylerData) {
            this.recylerData = recylerData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CountDownVH countDownVH = new CountDownVH(LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_countdown, parent, false));
            return countDownVH;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CountDownVH countDownVH = (CountDownVH) holder;
            CountDownEntity countDownEntity = recylerData.get(position);
            countDownVH.countDownDesc.setText(countDownEntity.getCountDownDesc());
            countDownVH.countDownIcon.setImageResource(countDownEntity.getContDownDrawable());
        }

        @Override
        public int getItemCount() {
            return recylerData.size();
        }

        class CountDownVH extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_countdown_desc)
            TextView countDownDesc;
            @Bind(R.id.iv_countdown_icon)
            ImageView countDownIcon;

            public CountDownVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    /**
     * 模拟的数据源
     */
    class CountDownEntity {
        private String countDownDesc;
        private int contDownDrawable;
        private long countDownTimeTotal;

        public String getCountDownDesc() {
            return countDownDesc;
        }

        public void setCountDownDesc(String countDownDesc) {
            this.countDownDesc = countDownDesc;
        }

        public int getContDownDrawable() {
            return contDownDrawable;
        }

        public void setContDownDrawable(int contDownDrawable) {
            this.contDownDrawable = contDownDrawable;
        }

        public long getCountDownTimeTotal() {
            return countDownTimeTotal;
        }

        public void setCountDownTimeTotal(long countDownTimeTotal) {
            this.countDownTimeTotal = countDownTimeTotal;
        }
    }
}
