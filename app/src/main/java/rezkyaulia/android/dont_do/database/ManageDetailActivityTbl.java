package rezkyaulia.android.dont_do.database;

import java.util.List;

import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTblDao;

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
        return dao.insertOrReplace(object);
    }

    public void add(List<DetailActivityTbl > object) {
        dao.insertOrReplaceInTx(object);
    }

    public List<DetailActivityTbl > getAll() {
        return dao.queryBuilder().list();
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
