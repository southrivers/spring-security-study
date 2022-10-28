package com.websystique.springboot.controller;

import com.websystique.springboot.SimpleMovingAverage;
import com.websystique.springboot.model.BigDemInterfaceRate;
import com.websystique.springboot.model.InterfaceRateModel;
import com.websystique.springboot.model.ResponseBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class HomeController {

    @Autowired
    SimpleMovingAverage simpleMovingAverage;

    @GetMapping("/moving-average/reset")
    public void reset() {
        simpleMovingAverage.resetDataSet();
    }

    @PostMapping("moving-average/{step}/{interval}")
    public ResponseBo testError(@RequestBody ArrayList<InterfaceRateModel> models, @PathVariable("interval") int interval,@PathVariable("step") int step) {
        try {
            models.sort(Comparator.comparing(InterfaceRateModel::getDate));
            LinkedHashMap<Long, Double> map = new LinkedHashMap<>();
            for (InterfaceRateModel model : models) {
                map.put(model.getDate().getTime() / (1000*60), model.getRate());
            }
            LinkedList<InterfaceRateModel> res = new LinkedList<>();
            InterfaceRateModel last = models.get(0);
            long lastDate = last.getDate().getTime() / (1000*60);
            int start = 0;
            simpleMovingAverage.setPeriod(interval);
            while (start < 24) {
                simpleMovingAverage.resetDataSet();
                double meanRes = 0;
                for (int i = 0; i < interval; ++i) {
                    Double simpleData = map.get(lastDate - (i * step * 60));
                    if (null == simpleData) {
                        continue;
                    }
                    simpleMovingAverage.addData(simpleData);
                }
                meanRes = simpleMovingAverage.getMean();
                InterfaceRateModel model = new InterfaceRateModel();
                model.setRate(meanRes);
                model.setDate(new Date((lastDate + 24*60)*60*1000));
                res.addFirst(model);

                lastDate = lastDate - 60;
                start ++;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<BigDemInterfaceRate> finalRes = new LinkedList<>();
            for (InterfaceRateModel model : res) {
                BigDemInterfaceRate bm = new BigDemInterfaceRate();
                bm.setDate(sdf.format(model.getDate()));
                bm.setRate(BigDecimal.valueOf(model.getRate()));
                finalRes.add(bm);
            }

            ResponseBo resb = new ResponseBo();
            resb.setCode(0);
            resb.setMsg("success");
            resb.setRes(finalRes);
            return resb;
        }catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ResponseBo resb = new ResponseBo();
            resb.setCode(1);
            resb.setRes(null);
            try {
                e.printStackTrace(pw);
                resb.setMsg(sw.toString());
                return resb;
            } catch (Exception e1) {
                resb.setMsg("unknown error");
                return resb;
            }
        }
    }
}
