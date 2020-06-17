package OnlineJudge.service.impl;

import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.StatusDaoImpl;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.RankService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankServiceImpl implements RankService {
    StatusDaoImpl statusDao = new StatusDaoImpl();
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    @Override
    public PageBean<RankInfo> findRanks(int type,int pg,int size) {
        PageBean<RankInfo> page = new PageBean<RankInfo>();

        List<User> allUser = userDao.findAllUser();
        List<RankInfo> allRankInfo = new ArrayList<>();
        for(User user:allUser){
            RankInfo rankInfo = new RankInfo();
            rankInfo.setAc_sum(statusDao.findCountByUidAndStatus(user.getId(),"答案正确"));
            rankInfo.setSign_sum(statusDao.findSignCountByUid(user.getId()));
            rankInfo.setCon_sum(statusDao.findContestCountByUid(user.getId()));
            rankInfo.setUser(user);
            rankInfo.setRanting(user.getRanting());
            allRankInfo.add(rankInfo);

        }

        if(type == 1){
            Collections.sort(allRankInfo,new Comparator<RankInfo>() {
                @Override
                public int compare(RankInfo info1, RankInfo info2) {
                    if(info1.getAc_sum()<info2.getAc_sum()) return 1;
                    return -1;
                }
            });
        }else if(type == 2){
            Collections.sort(allRankInfo,new Comparator<RankInfo>() {
                @Override
                public int compare(RankInfo info1, RankInfo info2) {
                    if(info1.getSign_sum() < info2.getSign_sum()) return 1;
                    return -1;
                }
            });
        }else if(type == 3){
            Collections.sort(allRankInfo,new Comparator<RankInfo>() {
                @Override
                public int compare(RankInfo info1, RankInfo info2) {
                    if(info1.getCon_sum() < info2.getCon_sum()) return 1;
                    return -1;
                }
            });
        }else if(type ==4){
            Collections.sort(allRankInfo,new Comparator<RankInfo>() {
                @Override
                public int compare(RankInfo info1, RankInfo info2) {
                    if(info1.getRanting() < info2.getRanting()) return 1;
                    return -1;
                }
            });
        }

        List<RankInfo> info = new ArrayList<>();
        for(int i=0;i<size;i++){
            int in = (pg-1)*size+i;
            if(in >= allRankInfo.size()) break;
            allRankInfo.get(in).setRank(in+1);
            info.add(allRankInfo.get(in));
        }

        page.setCurrentPage(pg);
        page.setPageSize(size);
        page.setTotalCount(allRankInfo.size());

        int tot = 0;

        if(page.getPageSize() !=0 ){
            tot = page.getTotalCount()/page.getPageSize();
            if(page.getTotalCount()%page.getPageSize() != 0) tot++;
        }

        page.setTotalPage(tot);

        page.setList(info);

        return page;
    }
}
