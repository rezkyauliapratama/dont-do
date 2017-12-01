package rezkyaulia.android.dont_do.database;

import com.google.gson.Gson;

import java.util.List;

import rezkyaulia.android.dont_do.database.entity.UserTbl;
import rezkyaulia.android.dont_do.database.entity.UserTblDao;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */

public class ManageUserTbl {

    private Facade facade;


    private UserTblDao dao;

    ManageUserTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getUserTblDao();
    }

    public long add(UserTbl object) {
        Timber.e("ADD USER : "+new Gson().toJson(object));
        return dao.insertOrReplace(object);
    }

    public void add(List<UserTbl> object) {
        dao.insertOrReplaceInTx(object);
    }

    public List<UserTbl> getAll() {
        return dao.queryBuilder().list();
    }

    public UserTbl get() {
        List<UserTbl> userTbls =
                dao.queryBuilder().limit(1).list();

        return userTbls.size() == 0 ? null : userTbls.get(0);
    }

    public UserTbl get(String id) {
        return dao.queryBuilder().where(UserTblDao.Properties.UserId.eq(id)).unique();
    }

    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(UserTbl object) {
        dao.delete(object);
    }

    public long size(){
        return dao.count();
    }

}
