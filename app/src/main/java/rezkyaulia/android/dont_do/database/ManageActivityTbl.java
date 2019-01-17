package rezkyaulia.android.dont_do.database;

import com.google.gson.Gson;

import java.util.List;

import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.ActivityTblDao;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */

public class ManageActivityTbl {

    private Facade facade;


    private ActivityTblDao dao;

    ManageActivityTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getActivityTblDao();
    }

    public long add(ActivityTbl object) {
        Timber.e("add activity tbl : "+new Gson().toJson(object));
        return dao.insertOrReplace(object);
    }

    public void add(List<ActivityTbl> object) {
        dao.insertOrReplaceInTx(object);
    }

    public List<ActivityTbl> getAll() {
        return dao.queryBuilder().orderDesc(ActivityTblDao.Properties.CreatedDate).list();
    }


    public ActivityTbl get(String key) {
        return dao.queryBuilder().where(ActivityTblDao.Properties.ActivityId.eq(key)).orderDesc(ActivityTblDao.Properties.CreatedDate).limit(1).unique();
    }


    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(ActivityTbl object) {
        dao.delete(object);
    }

    public long size(){
        return dao.count();
    }

}
