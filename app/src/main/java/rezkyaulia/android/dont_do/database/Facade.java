package rezkyaulia.android.dont_do.database;

import rezkyaulia.android.dont_do.database.entity.DaoSession;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */

public class Facade {
    private static Facade instance;
    final DaoSession session;

    public static void init(DaoSession daoSession) {
        instance = new Facade(daoSession);
    }

    public static Facade getInstance() {
        return instance;
    }

    private ManageActivityTbl manageActivityTbl;
    private ManageUserTbl manageUserTbl;
    private ManageDetailActivityTbl manageDetailActivityTbl;




    Facade(DaoSession daoSession) {
        this.session = daoSession;

        manageActivityTbl = new ManageActivityTbl(this);
        manageUserTbl = new ManageUserTbl(this);
        manageDetailActivityTbl = new ManageDetailActivityTbl(this);


    }

    public DaoSession getDaoSession(){
        return session;
    }

    public ManageActivityTbl getManageActivityTbl() {
        return manageActivityTbl;
    }

    public ManageUserTbl getManagerUserTbl() {
        return manageUserTbl;
    }

    public ManageDetailActivityTbl getManageDetailActivityTbl() {
        return manageDetailActivityTbl;
    }

}
