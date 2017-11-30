package rezkyaulia.android.dont_do.database;

import java.util.List;

import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.ActivityTblDao;

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
        return dao.insertOrReplace(object);
    }

    public void add(List<ActivityTbl> object) {
        dao.insertOrReplaceInTx(object);
    }

    public List<ActivityTbl> getAll() {
        return dao.queryBuilder().list();
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
