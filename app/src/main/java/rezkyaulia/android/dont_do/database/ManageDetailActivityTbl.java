package rezkyaulia.android.dont_do.database;

import com.google.gson.Gson;

import java.util.List;

import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTblDao;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */

public class ManageDetailActivityTbl {

    private Facade facade;


    private DetailActivityTblDao dao;

    ManageDetailActivityTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getDetailActivityTblDao();
    }

    public long add(DetailActivityTbl object) {
        Timber.e("ADD DETAIL ACTIVITY : "+new Gson().toJson(object));
        return dao.insertOrReplace(object);
    }

    public void add(List<DetailActivityTbl > object) {
        dao.insertOrReplaceInTx(object);
    }

    public List<DetailActivityTbl > getAll() {
        return dao.queryBuilder().list();
    }

    public List<DetailActivityTbl > getAll(String activityId) {
        return dao.queryBuilder().where(DetailActivityTblDao.Properties.ActivityId.eq(activityId)).list();
    }

    public DetailActivityTbl getUniqeNew(String activityKey){
        return dao.queryBuilder().where(DetailActivityTblDao.Properties.ActivityId.eq(activityKey)).orderDesc(DetailActivityTblDao.Properties.Timestamp).limit(1).unique();
    }

    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(DetailActivityTbl object) {
        dao.delete(object);
    }

    public long size(){
        return dao.count();
    }

}
