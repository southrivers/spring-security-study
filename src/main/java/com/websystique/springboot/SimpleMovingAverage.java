package com.websystique.springboot;

import com.websystique.springboot.utils.Cache;
import org.springframework.stereotype.Service;

@Service
public class SimpleMovingAverage {

    private  int period;
    private  double sum;

    public SimpleMovingAverage() {
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public  void addData(double num) {
        sum += num;
        Cache.dataset.add(num);
        if (Cache.dataset.size() > period) {
            sum -= Cache.dataset.remove();
        }
    }

    public double getMean() {
        return sum / Cache.dataset.size();
    }

    public void resetDataSet() {
        sum = 0;
        Cache.dataset.clear();
    }
}
