package bocanegra.mauro.yabank.domainlayer.db;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "company")
public class CompanyPOJO_DB {

    public static final String key_img1 = "logoClaro";
    public static final String key_img2 = "logoTuenti";
    public static final String key_img3 = "logoEntel";

    @PrimaryKey
    @NonNull
    String company_name;
    String img_id;



    public CompanyPOJO_DB(String company_name, String img_id){
        this.company_name=company_name;
        this.img_id=img_id;
    }

    @NonNull
    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(@NonNull String company_name) {
        this.company_name = company_name;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public static CompanyPOJO_DB[] prePopulateDB(){
        return new CompanyPOJO_DB[]{
                new CompanyPOJO_DB("claro",key_img1),
                new CompanyPOJO_DB("tuenti",key_img2),
                new CompanyPOJO_DB("entel",key_img3)
        };
    }
}
