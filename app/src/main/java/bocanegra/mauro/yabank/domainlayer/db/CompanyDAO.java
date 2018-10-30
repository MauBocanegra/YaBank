package bocanegra.mauro.yabank.domainlayer.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CompanyDAO {
    @Insert
    public void insert(CompanyPOJO_DB... companyPOJO_dbs);

    @Update
    public void update(CompanyPOJO_DB... companyPOJO_dbs);

    @Delete
    public void delete(CompanyPOJO_DB... companyPOJO_dbs);

    @Query("SELECT * FROM company")
    public List<CompanyPOJO_DB> getCompanies();

    @Query("SELECT * FROM company WHERE company_name = :companyName")
    public CompanyPOJO_DB getContactWithId(String companyName);
}
