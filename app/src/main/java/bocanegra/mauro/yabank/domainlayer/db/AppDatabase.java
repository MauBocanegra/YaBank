package bocanegra.mauro.yabank.domainlayer.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@Database(entities = {CompanyPOJO_DB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CompanyDAO getCompanyDAO();
    private static AppDatabase instance;

    public synchronized static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = prePopulateDB(context);
        }
        return instance;
    }

    public static AppDatabase prePopulateDB(final Context context){
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "my-database")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).getCompanyDAO().insert(CompanyPOJO_DB.prePopulateDB());
                            }
                        });
                    }
                })
                .build();
    }

}
