package OnlineJudge.service.impl;

import OnlineJudge.dao.impl.QueueDaoImpl;
import OnlineJudge.domain.QueueInfo;
import OnlineJudge.domain.custominput;
import OnlineJudge.domain.info;
import OnlineJudge.domain.source_code;
import OnlineJudge.service.hackService;
import OnlineJudge.util.LanguageUtils;
import OnlineJudge.util.Result;

import java.util.Date;

public class hackServiceImpl implements hackService {
    QueueDaoImpl queueDao = new QueueDaoImpl();
    @Override
    public boolean check(String code, String language, String in, String out) throws InterruptedException {
        QueueDaoImpl queueDao = new QueueDaoImpl();
        QueueInfo queueInfo = new QueueInfo();

        queueInfo.setCode_length(code.getBytes().length);
        queueInfo.setContest_id(0);
        queueInfo.setIn_date(new Date());
        queueInfo.setIp("0.0.0.0");
        queueInfo.setLanguage(LanguageUtils.stringtoid(language));
        queueInfo.setProblem_id(0);
        queueInfo.setResult(0);
        queueInfo.setUser_id("1");

        source_code source_code = new source_code();
        source_code.setSource(code);

        custominput custominput = new custominput();
        custominput.setInput_text(in);

        int solution_id = queueDao.insertInput(queueInfo,source_code,custominput);

        int count = 0;
        while(true){
            int sta = checkEnd(solution_id);

            count++;
            if(count >= 10) return false;
            if(sta == -1) return false;
            if(sta == 1) break;
            Thread.sleep(1000);
        }

        String ans = askRes(solution_id);

        if(ans.equals(out)) return true;
        return false;
    }

    @Override
    public String askRes(int id) {
        String runtimeInfo = queueDao.findRuntimeInfo(id);
        String compileInfo = queueDao.findCompileInfo(id);

        if(runtimeInfo == null && compileInfo == null){
            return null;
        }

        if(runtimeInfo != null) return runtimeInfo;
        return compileInfo;

    }

    @Override
    public int checkEnd(int id) {
        int res = -1;
        try{
            res =queueDao.findResInfoBySid(id);
        }catch (Exception e){
            return 0;
        }

        if (res <= 3){
            return 0;
        }

        if(res == 4) return 1;
        else return -1;
    }
}
